package samples.effectivejava.item10;

import javax.annotation.Nonnull;

public class GoodToStringClass {

	private final int number;
	private final String name;
	
	public GoodToStringClass(final int number, @Nonnull final String name) {
		super();
		this.number = number;
		this.name = name;
	}

	@Override
	public String toString() {
		// Good toString uses all fields 
		return "GoodToStringClass [number=" + number + ", name=" + name + "]";
	}
	
}
