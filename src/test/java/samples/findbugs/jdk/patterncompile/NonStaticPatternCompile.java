package samples.findbugs.jdk.patterncompile;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Nonnull;

public class NonStaticPatternCompile {

	private static final String STRING_REGEX = "[ade]";

	/**
	 * Test non static Pattern.compile
	 * @return a matcher
	 */
	@Nonnull
	public Matcher testReportNonStaticPatternCompile() {
		final String test = "abecedario";
		final Pattern ptrn = Pattern.compile("[abc]");
		return ptrn.matcher(test);
	}

	/**
	 * Test non static Pattern.compile with static regex
	 * @return a matcher
	 */
	@Nonnull
	public Matcher testReportNonStaticPatternCompileWithStaticRegex() {
		final String test = "cabecera";
		final Pattern patcom = Pattern.compile(STRING_REGEX);
		return patcom.matcher(test);
	}

	/**
	 * Test non static Pattern.compile with local regex
	 * @return a matcher
	 */
	@Nonnull
	public Matcher testReportNonStaticPatternCompileWithFinalLocalRegex() {
		final String regex = "[bcd]";
		final String test = "cabecera";
		final String regex2 = regex + "[e]";
		final Pattern pattern = Pattern.compile(regex2);
		return pattern.matcher(test);
	}

	/**
	 * Test non static Pattern.compile with non-final local regex
	 * @return a matcher
	 */
	@SuppressWarnings({"checkstyle:finallocalvariable", "PMD.LocalVariableCouldBeFinal"})
	@Nonnull
	public Matcher testNeverReportNonStaticPatternCompileWithNonFinalLocalRegex() {
		final String regex = "[fgt]";
		String regex2 = regex;
		final String test = "foddgata";
		final Pattern pattern = Pattern.compile(regex2);
		return pattern.matcher(test);
	}

	/**
	 * Test non static Pattern.compile with final regex parameter
	 * @param regex the regex
	 * @return a matcher
	 */
	@Nonnull
	public Matcher testNeverReportNonStaticPatternCompileWithFinalRegexParameter(@Nonnull final String regex) {
		final Pattern ptrn = Pattern.compile(regex);
		return ptrn.matcher("test");
	}

	/**
	 * Test non static Pattern.compile with non final regex parameter
	 * @param regex the regex
	 * @return a matcher
	 */
	@SuppressWarnings("checkstyle:finalparameters")
	@Nonnull
	public Matcher testNeverReportNonStaticPatternCompileWithNonFinalRegexParameter(@Nonnull String regex) {
		final Pattern ptrn = Pattern.compile(regex);
		return ptrn.matcher("test");
	}

	/**
	 * Test non static Pattern.compile with regex from object
	 * @return a matcher
	 */
	@Nonnull
	public Matcher testNeverReportNonStaticPatternCompileWithRegexFromObject() {
		final String test = "vecindario";
		final DummyRegex dummyRegex = new DummyRegex();
		final Pattern pattern = Pattern.compile(dummyRegex.getRegex());
		return pattern.matcher(test);
	}

	/**
	 * Test pattern compile with a concatenated regex
	 * @param email the email
	 * @return The pattern with the compiled regex
	 */
	@Nonnull
	public Pattern testPatternCompileWithAConcatenatedRegex(@Nonnull final String email) {
		final String[] parts = email.split("@");
		return Pattern.compile("^" + parts[0] + "\\+([^@]+)@" + parts[1] + "$");
	}

	private static class DummyRegex {
		public String getRegex() {
			return "[cdv]";
		}
	}
}