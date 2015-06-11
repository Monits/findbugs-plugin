package samples.effectivejava.item10;

import javax.annotation.Nonnull;

import de.ruedigermoeller.heapoff.structs.structtypes.StructString;

public class ToStringLibraryFieldClass {

	// A field of type StructString, a library with a toString() method.
	private final StructString aLibraryWithToString = new StructString(1);

	@Nonnull
	public StructString getaLibraryWithToString() {
		return aLibraryWithToString;
	}
}
