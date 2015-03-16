package samples.effectivejava.item10;

import javax.annotation.Nonnull;

public class IncompleteToStringClass {

	private final int number;
	private final String name;
	
	public IncompleteToStringClass(final int number, @Nonnull final String name) {
		super();
		this.number = number;
		this.name = name;
	}
	
	@Override
	public String toString() {
		// Incomplete toString, not including name 
		return "IncompleteToStringClas [number=" + number + "]";
	}

	public int getNumber() {
		return number;
	}

	@Nonnull
	public String getName() {
		return name;
	}
}
