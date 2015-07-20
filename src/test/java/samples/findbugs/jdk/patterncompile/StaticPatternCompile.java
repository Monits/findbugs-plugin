package samples.findbugs.jdk.patterncompile;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Nonnull;

public class StaticPatternCompile {

	private static final String STRING_REGEX = "[def]";
	private static final Pattern PATTERN = Pattern.compile(STRING_REGEX);

	/**
	 * Test static Pattern.compile
	 * @return a matcher
	 */
	@Nonnull
	public Matcher testStaticPatternCompile() {
		final String test = "defecto";
		return PATTERN.matcher(test);
	}
}