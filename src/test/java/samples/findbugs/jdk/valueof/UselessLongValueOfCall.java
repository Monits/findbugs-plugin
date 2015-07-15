package samples.findbugs.jdk.valueof;

import javax.annotation.Nonnull;

public class UselessLongValueOfCall {

	@Nonnull
	public Long getLongValueOfPrimitiveLong() {
		return Long.valueOf(2L);
	}

	@Nonnull
	public Long getLongValueOfString() {
		return Long.valueOf("2");
	}
}