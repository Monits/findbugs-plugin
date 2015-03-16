package samples.effectivejava.item10;

import java.util.Arrays;

import javax.annotation.Nonnull;

public class ArrayMissingToStringClass {

	private final byte[] rawData;
	private final String name;
	
	public ArrayMissingToStringClass(@Nonnull final byte[] rawData, @Nonnull final String name) {
		super();
		this.rawData = Arrays.copyOf(rawData, rawData.length);
		this.name = name;
	}
	
	@Override
	public String toString() {
		// Incomplete toString, not including rawData
		return "ArrayMissingToStringClass [name=" + name + "]";
	}

	@Nonnull
	public byte[] getRawData() {
		return Arrays.copyOf(rawData, rawData.length);
	}

	@Nonnull
	public String getName() {
		return name;
	}
}
