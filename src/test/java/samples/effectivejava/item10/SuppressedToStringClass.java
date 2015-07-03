package samples.effectivejava.item10;

import javax.annotation.Nonnull;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

@SuppressFBWarnings(value = "MISSING_TO_STRING_OVERRIDE", justification = "This is a test")
public class SuppressedToStringClass {

	private final int number;
	private final String name;
	
	public SuppressedToStringClass(final int number, @Nonnull final String name) {
		super();
		this.number = number;
		this.name = name;
	}

	public int getNumber() {
		return number;
	}

	public String getName() {
		return name;
	}
}
