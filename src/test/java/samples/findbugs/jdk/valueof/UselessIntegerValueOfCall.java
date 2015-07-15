package samples.findbugs.jdk.valueof;

import javax.annotation.Nonnull;

public class UselessIntegerValueOfCall {

	@Nonnull
	public Integer getIntegerValueOfPrimitiveInteger() {
		return Integer.valueOf(2);
	}
}