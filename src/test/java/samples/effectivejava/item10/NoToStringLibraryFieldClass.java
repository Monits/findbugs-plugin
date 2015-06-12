package samples.effectivejava.item10;

import javax.annotation.Nonnull;

import de.ruedigermoeller.serialization.FSTConfiguration;

public class NoToStringLibraryFieldClass {

	// A field of type FSTConfiguration, a library with no toString() method.
	private final FSTConfiguration aLibraryWithNoToString = FSTConfiguration.getDefaultConfiguration();

	@Nonnull
	public FSTConfiguration getaLibraryWithNoToString() {
		return aLibraryWithNoToString;
	}
}
