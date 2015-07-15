package samples.findbugs.jdk.valueof;

import javax.annotation.Nonnull;

public class UselessByteValueOfCall {

	/**
	 * Test primitive byte
	 * @return a Byte value
	 */
	@Nonnull
	public Byte getByteValueOfPrimitiveByte() {
		final byte bt = 1;
		return Byte.valueOf(bt);
	}

	/**
	 * Test primitive byte with left shift
	 * @return a Byte value
	 */
	@Nonnull
	public Byte getByteValueOfPrimitiveByteWithLeftShift() {
		byte bt = 'a';
		bt <<= 2;
		return Byte.valueOf(bt);
	}

	/**
	 * Test primitive character as byte
	 * @return a Byte value
	 */
	@Nonnull
	public Byte getByteValueOfPrimitiveCharacterAsByte() {
		final byte bt = 'a';
		return Byte.valueOf(bt);
	}

	/**
	 * Test primitive integer as byte
	 * @return a Byte value
	 */
	@Nonnull
	public Byte getByteValueOfPrimitiveIntegerAsByte() {
		final byte bt = 2;
		return Byte.valueOf(bt);
	}

	@Nonnull
	public Byte getByteValueOfString() {
		return Byte.valueOf("5");
	}
}