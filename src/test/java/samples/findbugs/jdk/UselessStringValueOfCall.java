package samples.findbugs.jdk;

import javax.annotation.Nonnull;

public class UselessStringValueOfCall {

	private final DummyObject dummy = new DummyObject();

	@Nonnull
	public String getStringValueOfString() {
		return String.valueOf("some string");
	}

	@Nonnull
	public String getStringValueOfDummyString() {
		return String.valueOf(dummy.getString());
	}

	@Nonnull
	public String getString() {
		return "other string";
	}

	@Nonnull
	public String getStringValueOfPrimitiveInteger() {
		return String.valueOf(2);
	}

	/**
	 * Test concatenated string
	 * @param text the text to concatenate
	 */
	public void concatenatedStringFromParam(@Nonnull final String text) {
		new StringBuilder("some text").append(text + " other text");
	}

	/**
	 * Test concatenated local string
	 */
	public void concatenatedLocalString() {
		final String text = " dummy ";
		new StringBuilder("some").append(text + "text");
	}

	/**
	 * Test string from parameter in a String.valueOf
	 * @param text the text to concatenate
	 * @return the string concatenated
	 */
	@Nonnull
	public String stringFromParamInValueOf(@Nonnull final String text) {
		return String.valueOf(text + " some text").toString();
	}

	private static class DummyObject {
		public String getString() {
			return "dummy string";
		}
	}
}