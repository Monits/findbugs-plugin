package jp.co.worksap.oss.findbugs.jsr305.nullness;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.umd.cs.findbugs.ba.XClass;
import edu.umd.cs.findbugs.classfile.CheckedAnalysisException;
import edu.umd.cs.findbugs.classfile.ClassDescriptor;

public final class GenericsData {
	private final Map<String, String> declaredGenerics;
	private final Map<String, List<String>> superclassesGenerics;
	private final GenericsData enclossingClassData;
	private static final Pattern GENERICS_REFERENCE_PATTERN = Pattern.compile("([<;][-+]?)T([^;]+)(?=;)");
	
	public GenericsData(final XClass clazz) throws CheckedAnalysisException {
		this(clazz, Collections.<String>emptyList());
	}
	
	public GenericsData(final XClass clazz, final List<String> childBoundGenerics) throws CheckedAnalysisException {
		final String signature = clazz.getSourceSignature();
		
		// Anonymous inner classes
		if (signature == null) {
			declaredGenerics = Collections.emptyMap();
			superclassesGenerics = Collections.emptyMap();
			enclossingClassData = null;
			return;
		}
		
		final ClassDescriptor enclosingClass = clazz.getImmediateEnclosingClass();
		if (enclosingClass != null) {
			enclossingClassData = new GenericsData(enclosingClass.getXClass());
		} else {
			enclossingClassData = null;
		}
		
		// Parse signature
		final Map<String, String> declared = new HashMap<String, String>();
		final Map<String, List<String>> superGenerics = new HashMap<String, List<String>>();
		int pos = 0;
		int lastConsumedPos = 0;
		
		// No declared generics
		if (signature.charAt(0) == '<') {
			int openGenerics = 1;
			pos++;
			lastConsumedPos++;
			
			while (openGenerics > 0) {
				switch (signature.charAt(pos++)) {
				case '<':
					openGenerics++;
					break;
					
				case '>':
					openGenerics--;
					break;
					
				case ';':
					if (openGenerics == 1) {
						final String generic[] = signature.substring(lastConsumedPos, pos - 1).split(":");
						final String genericValue;
						
						if (!childBoundGenerics.isEmpty() && childBoundGenerics.size() >= declared.size()) {
							genericValue = childBoundGenerics.get(declared.size());
						} else {
							genericValue = generic[1];
						}
						
						declared.put(generic[0], genericValue);
						lastConsumedPos = pos;
					}
				}
			}
			
			lastConsumedPos = pos;
		}
		
		// From here on, we have "extends", and we are possibly binding generics
		while (pos < signature.length()) {
			switch (signature.charAt(pos++)) {
			case ';':
				// this class defines no generics, skip it
				lastConsumedPos = pos;
				break;
				
			case '<':
				final String className = signature.substring(lastConsumedPos, pos - 1);
				final List<String> genericsList = new ArrayList<String>();
				lastConsumedPos = pos;
				int openGenerics = 1;
				
				while (openGenerics > 0) {
					switch (signature.charAt(pos++)) {
					case '<':
						openGenerics++;
						break;
						
					case '>':
						openGenerics--;
						break;
						
					case ';':
						if (openGenerics == 1) {
							genericsList.add(signature.substring(lastConsumedPos, pos - 1));
							lastConsumedPos = pos;
						}
					}
				}
				
				superGenerics.put(className, genericsList);
			
				break;
			}
		}
		
		
		declaredGenerics = Collections.unmodifiableMap(declared);
		superclassesGenerics = Collections.unmodifiableMap(superGenerics);
	}
	
	private final String keyForSuperclass(final ClassDescriptor cd) {
		return "L" + cd.getClassName();
	}
	
	public List<String> getMappedSuperClassdata(final ClassDescriptor cd) {
		final List<String> list = superclassesGenerics.get(keyForSuperclass(cd));
		if (list == null) {
			return Collections.emptyList();
		}
		
		final List<String> ret = new ArrayList<String>(list.size());
		
		for (final String val : list) {
			if (val.charAt(0) == 'T') {
				final String genericName = val.substring(1);
				final String boundValue = declaredGenerics.get(genericName);
				if (boundValue != null) {
					ret.add(boundValue);
				} else {
					ret.add(getInheritedValue(genericName));
				}
			} else {
				// Has this value any references to generics such as Ljava/util/List<TT;> ??
				final Matcher matcher = GENERICS_REFERENCE_PATTERN.matcher(val);
				String boundValue = val;
				while (matcher.find()) {
					boundValue = boundValue.replaceAll(Pattern.quote(matcher.group(1)) + "T" + matcher.group(2) + ";",
						Matcher.quoteReplacement(matcher.group(1)) + declaredGenerics.get(matcher.group(2)) + ";");
				}
				ret.add(boundValue);
			}
		}
		
		return Collections.unmodifiableList(ret);
	}

	private String getInheritedValue(final String boundValue) {
		if (enclossingClassData == null) {
			return "Ljava/lang/Object"; // not really cool, but a good default
		}
		
		if (enclossingClassData.declaredGenerics.containsKey(boundValue)) {
			return enclossingClassData.declaredGenerics.get(boundValue);
		}
		
		// Keep looking up!
		return enclossingClassData.getInheritedValue(boundValue);
	}
	
	public Map<String, String> getDeclaredGenerics() {
		return declaredGenerics;
	}

	@Override
	public String toString() {
		return "GenericsData [declaredGenerics=" + declaredGenerics
				+ ", superclassesGenerics=" + superclassesGenerics + "]";
	}
}