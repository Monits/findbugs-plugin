package samples.findbugs.jdk.valueof;

import javax.annotation.Nonnull;

public class UselessFloatValueOfCall {

	@Nonnull
	public Float getFloatValueOfPrimitiveFloat() {
		return Float.valueOf(2F);
	}

	@Nonnull
	public Float getFloatValueOfString() {
		return Float.valueOf("4");
	}
}