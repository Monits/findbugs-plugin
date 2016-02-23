package jp.co.worksap.oss.findbugs.jsr305.nullness;


import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.bcel.classfile.Method;
import org.apache.bcel.generic.ReferenceType;
import org.apache.bcel.generic.Type;

import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.BytecodeScanningDetector;
import edu.umd.cs.findbugs.ba.XClass;
import edu.umd.cs.findbugs.ba.XMethod;
import edu.umd.cs.findbugs.ba.jsr305.JSR305NullnessAnnotations;
import edu.umd.cs.findbugs.ba.jsr305.TypeQualifierAnnotation;
import edu.umd.cs.findbugs.ba.jsr305.TypeQualifierApplications;
import edu.umd.cs.findbugs.ba.jsr305.TypeQualifierValue;
import edu.umd.cs.findbugs.classfile.CheckedAnalysisException;
import edu.umd.cs.findbugs.classfile.ClassDescriptor;
import edu.umd.cs.findbugs.classfile.Global;

public class UnknownNullnessDetector extends BytecodeScanningDetector {

	private static final TypeQualifierValue<?> NULLNESS_QUALIFIER
		= TypeQualifierValue.getValue(JSR305NullnessAnnotations.NONNULL, null);

	private static final Pattern ANONYMOUS_CLASSNAME_PATTERN = Pattern.compile("\\$[0-9]+$");

	private final BugReporter bugReporter;

	/**
	 * Creates a new UnknownNullnessDetector.
	 * @param bugReporter the bug reporter to use.
	 */
	public UnknownNullnessDetector(@Nonnull final BugReporter bugReporter) {
		this.bugReporter = bugReporter;
	}

	@Override
	public void visit(final Method method) {
		final XMethod xMethod = getXMethod();
		if (xMethod.isSynthetic()) {
			// Ignore methods not created by the developer himself
			return;
		}

		// Ignore constructors for anonymous classes, they can't be declared / overridden
		if ("<init>".equals(xMethod.getName())) {
			final Matcher matcher = ANONYMOUS_CLASSNAME_PATTERN.matcher(getClassDescriptor().getClassName());
			if (matcher.find()) {
				return;
			}
		}

		// Enums have several false positives we need to ignore...
		if (isEnumIgnoredMethod(xMethod)) {
			return;
		}
		
		/*
		 * Ignore inherited methods.. Nullness should be declared upstream
		 * This also prevents us from reporting on methods whose expected nullness we don't control,
		 * such as Object.equals and List.add.
		 */
		if (findSuperMethods(xMethod).isEmpty()) {
			// Make sure our own annotations are in place
			detectUnknownNullnessOfParameter(method, NULLNESS_QUALIFIER);
			detectUnknowNullnessOfReturnedValue(method, NULLNESS_QUALIFIER);
		}
	}

	private boolean isEnumIgnoredMethod(@Nonnull final XMethod xMethod) {
		boolean checkForEnum = false;
		final String methodName = xMethod.getName();
		final String signature = xMethod.getSignature();
		
		// public static CCC[] values()
		if ("values".equals(methodName) && xMethod.isStatic() && xMethod.getNumParams() == 0) {
			checkForEnum = true;
		}
		
		// public static CCC valueOf(String)
		if ("valueOf".equals(methodName) && xMethod.isStatic()
				&& signature.equals("(Ljava/lang/String;)L" + xMethod.getClassDescriptor().getClassName() + ";")) {
			checkForEnum = true;
		}
		
		if (checkForEnum) {
			return isCurrentClassAnEnum();
		}
		
		return false;
	}

	private boolean isCurrentClassAnEnum() {
		// enums can't be put into hierarchies, they must extend java.lang.Enum directly

		final XClass xclass = getXClass();
		if (xclass == null) {
			return false;
		}
		final ClassDescriptor superCD = xclass.getSuperclassDescriptor();
		if ("java.lang.Enum".equals(superCD.getDottedClassName())) {
			return true;
		}

		return false;
	}

	private void detectUnknownNullnessOfParameter(@Nonnull final Method method,
			@Nonnull final TypeQualifierValue<?> nullness) {
		Type[] argumentTypes = method.getArgumentTypes();
		int initialIndex = 0;

		if ("<init>".equals(method.getName())) {
			if (method.getSignature().startsWith("(Ljava/lang/String;I")) {
				// This may be an enum, in which case the first arg is inherited and can't be checked
				if (isCurrentClassAnEnum()) {
					initialIndex = 2;
				}
			} else if (!getXClass().isStatic() && method.getSignature()
					.startsWith("(L" + getXClass().getContainingScope().getClassDescriptor().getClassName() + ";")) {
				initialIndex = 1;
			}
		}

		for (int i = initialIndex; i < argumentTypes.length; ++i) {
			if (!(argumentTypes[i] instanceof ReferenceType)
					|| getXMethod().isVariableSynthetic(i)
					|| (i == argumentTypes.length - 1 && getXMethod().isVarArgs())) {
				continue;
			}

			TypeQualifierAnnotation annotation = TypeQualifierApplications.getEffectiveTypeQualifierAnnotation(
					getXMethod(), i, nullness);
			if (annotation == null) {
				bugReporter.reportBug(new BugInstance("UNKNOWN_NULLNESS_OF_PARAMETER", NORMAL_PRIORITY)
					.addClassAndMethod(this));
			}
		}
	}

	@Nonnull
	private static Set<XMethod> findSuperMethods(@Nonnull final XMethod m) {
	 /*
	    We can't use {@code Hierarchy2.findMatchingMethod} since it considers return types, which is incorrect
	    for Java, and just doesn't work with generics.
	 */
		final Set<XMethod> result = new HashSet<XMethod>();
		findSuperMethods(m.getClassDescriptor(), m, result, Collections.<String, String>emptyMap());
		result.remove(m);
		return result;
	}

	@Nonnull
	private static Map<String, String> getBoundGenericsInClassSignature(@Nonnull final ClassDescriptor c,
			@Nonnull final Map<String, String> parentBoundGenerics) {
		try {
			final XClass xc = c.getXClass();

			final ClassDescriptor superClassDescriptor = xc.getSuperclassDescriptor();
			if (superClassDescriptor == null) {
				return Collections.emptyMap();
			}
			final XClass superxc = superClassDescriptor.getXClass();

			final String sourceSignature = xc.getSourceSignature();

			if (sourceSignature == null) {
				return Collections.emptyMap();
			}

			final Map<String, String> generics = new HashMap<String, String>(parentBoundGenerics);

			final int genericsStart = sourceSignature.indexOf('<', 2);
			if (genericsStart != -1) {	// child class bounds generics?
				// Get generics bound on this class
				final String[] boundValues = sourceSignature.substring(
						genericsStart + 1, sourceSignature.indexOf('>', genericsStart)).split(";");

				// Get generics definitions on parent class
				extractGenericsFromSuperclass(parentBoundGenerics, generics,
						boundValues, superxc);
				
				for (final ClassDescriptor i : xc.getInterfaceDescriptorList()) {
					extractGenericsFromSuperclass(parentBoundGenerics, generics,
							boundValues, i.getXClass());
				}
			}

			return generics;
		} catch (final CheckedAnalysisException e) {
			return Collections.emptyMap();
		}
	}

	private static void extractGenericsFromSuperclass(
			@Nonnull final Map<String, String> parentBoundGenerics,
			@Nonnull final Map<String, String> generics, @Nonnull final String[] boundValues,
			@Nonnull final XClass superxc) {
		final String superSourceSignature = superxc.getSourceSignature();
		if (superSourceSignature != null) {
			final String[] configValues = superSourceSignature.substring(
					1, superSourceSignature.indexOf('>') - 1).split(";");

			for (int i = 0; i < configValues.length; i++) {
				final int genericNameStart = configValues[i].indexOf(':');
				
				if (genericNameStart == -1) {
					// The generics are statically set (i.e Comparable<MyObject> instead of Comparable<T>)
					continue;
				}
				
				final String actualValue;
				if (boundValues[i].startsWith("T") && parentBoundGenerics.containsKey(boundValues[i].substring(1))) {
					// It's a generic, get it's value from parent!
					actualValue = parentBoundGenerics.get(boundValues[i].substring(1));
				} else {
					actualValue = boundValues[i];
				}
				
				generics.put(configValues[i].substring(0, genericNameStart), actualValue);
			}
		}
	}
	
	private static void findSuperMethods(@Nullable final ClassDescriptor c, @Nonnull final XMethod m,
			@Nonnull final Set<XMethod> accumulator, @Nonnull final Map<String, String> parentBoundGenerics) {
		if (c == null) {
			return;
		}
		
		try {
			final XClass xc = Global.getAnalysisCache().getClassAnalysis(XClass.class, c);
			
			// TODO : Test this when generics are propagated upstream
			final Map<String, String> boundGenerics = getBoundGenericsInClassSignature(c, parentBoundGenerics);
			
			for (final XMethod xm : xc.getXMethods()) {
				if (xm.isStatic() == m.isStatic() && xm.getName().equals(m.getName())
						&& signaturesMatches(xm, m, boundGenerics)) {
					if (!accumulator.add(xm)) {
						return;
					}
				}
			}
			
			findSuperMethods(xc.getSuperclassDescriptor(), m, accumulator, boundGenerics);
			for (final ClassDescriptor i : xc.getInterfaceDescriptorList()) {
				findSuperMethods(i, m, accumulator, boundGenerics);
			}
			if (!accumulator.add(m)) {
				return;
			}
		} catch (final CheckedAnalysisException e) {
			return;
		}
	}
	
	private static boolean signaturesMatches(@Nonnull final XMethod superm, @Nonnull final XMethod m,
			@Nonnull final Map<String, String> boundGenerics) {
		// Are there generics?
		String signature = superm.getSourceSignature();
		if (signature == null) {
			return getArgumentSignature(superm).equals(getArgumentSignature(m));
		}
		
		// Replace all generics
		for (final Entry<String, String> entry : boundGenerics.entrySet()) {
			signature = signature.replaceAll("T" + Pattern.quote(entry.getKey()), Matcher.quoteReplacement(entry.getValue()));
		}
		final String actualSignature = m.getSourceSignature() == null ? m.getSignature() : m.getSourceSignature();
		
		return signature.equals(actualSignature);
	}

	@Nonnull
	private static String getArgumentSignature(@Nonnull final XMethod xm) {
		final String signature = xm.getSignature();
		return signature.substring(0, signature.indexOf(')') + 1);
	}

	private void detectUnknowNullnessOfReturnedValue(@Nonnull final Method method,
			@Nonnull final TypeQualifierValue<?> nullness) {
		if (!(method.getReturnType() instanceof ReferenceType)) {
			return;
		}

		TypeQualifierAnnotation annotation = TypeQualifierApplications.getEffectiveTypeQualifierAnnotation(
				getXMethod(), nullness);
		if (annotation == null) {
			bugReporter.reportBug(new BugInstance("UNKNOWN_NULLNESS_OF_RETURNED_VALUE", NORMAL_PRIORITY)
				.addClassAndMethod(this));
		}
	}
}
