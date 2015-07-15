package samples.findbugs.jdk.valueof;

import javax.annotation.Nonnull;

public class UselessShortValueOfCall {

	/**
	 * Test primitive short
	 * @return a Short value
	 */
	@Nonnull
	public Short getShortValueOfPrimitiveShort() {
		final short sh = 2;
		return Short.valueOf(sh);
	}

	/**
	 * Test primitive short whit left shift
	 * @return a Short value
	 */
	@Nonnull
	public Short getShortValueOfPrimitiveShortWithLeftShift() {
		short sh = 2;
		sh <<= 2;
		return Short.valueOf(sh);
	}

	/**
	 * Test primitive character as Short
	 * @return a Short value
	 */
	@Nonnull
	public Short getShortValueOfPrimitiveCharacterAsShort() {
		final short sh = 'a';
		return Short.valueOf(sh);
	}

	/**
	 * Test primitive integer as Short
	 * @return a Short value
	 */
	@Nonnull
	public Short getShortValueOfPrimitiveIntegerAsShort() {
		final short sh = 2;
		return Short.valueOf(sh);
	}

	@Nonnull
	public Short getShortValueOfString() {
		return Short.valueOf("6");
	}
}