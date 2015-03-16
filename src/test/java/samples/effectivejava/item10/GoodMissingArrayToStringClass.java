package samples.effectivejava.item10;

import java.util.Arrays;

import javax.annotation.Nonnull;

public class GoodMissingArrayToStringClass {

	private final NoFieldClass[] rawData;
	private final String name;
	
	public GoodMissingArrayToStringClass(@Nonnull final NoFieldClass[] rawData, @Nonnull final String name) {
		super();
		this.rawData = Arrays.copyOf(rawData, rawData.length);
		this.name = name;
	}
	
	@Override
	public String toString() {
		// valid toString, NoFieldClass has no inner state, and needs not be present
		return "GoodMissingArrayToStringClass [name=" + name + "]";
	}

	@Nonnull
	public NoFieldClass[] getRawData() {
		return Arrays.copyOf(rawData, rawData.length);
	}

	@Nonnull
	public String getName() {
		return name;
	}
}
