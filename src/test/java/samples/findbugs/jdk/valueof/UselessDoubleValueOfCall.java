package samples.findbugs.jdk.valueof;

import javax.annotation.Nonnull;

public class UselessDoubleValueOfCall {

	@Nonnull
	public Double getDoubleValueOfPrimitiveDouble() {
		return Double.valueOf(2D);
	}

	@Nonnull
	public Double getDoubleValueOfString() {
		return Double.valueOf("3");
	}
}