package samples.effectivejava.item10;

import javax.annotation.Nonnull;

public class BadToStringCompositeInterestingClass {

	private final int number;
	private final String name;
	private final GoodToStringClass gtsc;
	
	public BadToStringCompositeInterestingClass(final int number, @Nonnull final String name,
			@Nonnull final GoodToStringClass gtsc) {
		super();
		this.number = number;
		this.name = name;
		this.gtsc = gtsc;
	}

	@Override
	public String toString() {
		// Good toString uses all fields 
		return "GoodToStringCompositeClass [number=" + number + ", name=" + name + "]";
	}

	@Nonnull
	public GoodToStringClass getGtsc() {
		return gtsc;
	}
	
}
