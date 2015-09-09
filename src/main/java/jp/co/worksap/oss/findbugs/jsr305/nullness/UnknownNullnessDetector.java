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

	public UnknownNullnessDetector(BugReporter bugReporter) {
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
		if (xMethod.getName().equals("<init>")) {
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

	private boolean isEnumIgnoredMethod(final XMethod xMethod) {
		boolean checkForEnum = false;
		final String methodName = xMethod.getName();
		final String signature = xMethod.getSignature();
		
		// public static CCC[] values()
		if (methodName.equals("values") && xMethod.isStatic() && xMethod.getNumParams() == 0) {
			checkForEnum = true;
		}
		
		// public static CCC valueOf(String)
		if (methodName.equals("valueOf") && xMethod.isStatic()
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
		final ClassDescriptor superCD = getXClass().getSuperclassDescriptor();
		if (superCD.getDottedClassName().equals("java.lang.Enum")) {
			return true;
		}

		return false;
	}

	private void detectUnknownNullnessOfParameter(Method method,
			TypeQualifierValue<?> nullness) {
		Type[] argumentTypes = method.getArgumentTypes();
		int initialIndex = 0;

		if (method.getName().equals("<init>")) {
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

	/*
	 * We can't use {@code Hierarchy2.findMatchingMethod} since it considers return types, which is incorrect
	 * for Java, and just doesn't work with generics. 
	 */
	public static Set<XMethod> findSuperMethods(final XMethod m) {
		final Set<XMethod> result = new HashSet<XMethod>();
		findSuperMethods(m.getClassDescriptor(), m, result, Collections.<String, String>emptyMap());
		result.remove(m);
		return result;
	}

	private static Map<String, String> getBoundGenericsInClassSignature(@Nonnull final ClassDescriptor c,
			@Nonnull final Map<String, String> parentBoundGenerics) {
		try {
			final XClass xc = c.getXClass();
			if (xc.getSuperclassDescriptor() == null) {
				return Collections.emptyMap();
			}

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

				// Get generics definitions on parent
				final XClass superxc = xc.getSuperclassDescriptor().getXClass();
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

			return generics;
		} catch (final CheckedAnalysisException e) {
			return Collections.emptyMap();
		}
	}
	
	private static void findSuperMethods(final ClassDescriptor c, final XMethod m,
			final Set<XMethod> accumulator, final Map<String, String> parentBoundGenerics) {
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
	
	private static boolean signaturesMatches(final XMethod superm, final XMethod m,
			final Map<String, String> boundGenerics) {
		// Are there generics?
		if (superm.getSourceSignature() == null) {
			return getArgumentSignature(superm).equals(getArgumentSignature(m));
		}
		
		// Replace all generics
		String signature = superm.getSourceSignature();
		for (final Entry<String, String> entry : boundGenerics.entrySet()) {
			signature = signature.replaceAll("T" + entry.getKey(), entry.getValue());
		}
		
		return m.getSourceSignature().equals(signature);
	}

	private static String getArgumentSignature(final XMethod xm) {
		final String signature = xm.getSignature();
		return signature.substring(0, signature.indexOf(')') + 1);
	}

	private void detectUnknowNullnessOfReturnedValue(Method method,
			TypeQualifierValue<?> nullness) {
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
