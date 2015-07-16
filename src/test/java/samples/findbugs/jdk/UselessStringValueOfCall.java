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

	private static class DummyObject {
		public String getString() {
			return "dummy string";
		}
	}
}