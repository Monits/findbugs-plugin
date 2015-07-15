package samples.findbugs.jdk.valueof;

import javax.annotation.Nonnull;

public class UselessCharacterValueOfCall {

	@Nonnull
	public Character getCharacterValueOfPrimitiveCharacter() {
		return Character.valueOf('a');
	}

	/**
	 * Test primitive Character
	 * @return a Character value
	 */
	@Nonnull
	public Character getCharacterValueOfPrimitiveCharacterWithLeftShift() {
		char ch = 'a';
		ch <<= 2;
		return Character.valueOf(ch);
	}

	/**
	 * Test primitive integer as Character
	 * @return a Character value
	 */
	@Nonnull
	public Character getCharacterValueOfPrimitiveIntegerAsCharacter() {
		final char ch = 1;
		return Character.valueOf(ch);
	}
}