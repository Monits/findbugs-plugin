package samples.effectivejava.item10;

import javax.annotation.Nonnull;

public class MissingToStringClass {

	private final int number;
	private final String name;
	
	public MissingToStringClass(final int number, @Nonnull final String name) {
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
	
	// No toString method!
}
