package samples.effectivejava.item10;

import javax.annotation.Nonnull;

public class BadEnumCompositeClass {

	private final EnumWithState data;
	private final String name;
	
	public BadEnumCompositeClass(@Nonnull final String name,
			@Nonnull final EnumWithState data) {
		super();
		this.name = name;
		this.data = data;
	}

	@Override
	public String toString() {
		// Good toString uses all fields 
		return "BadEnumCompositeClass [name=" + name + "]";
	}

	@Nonnull
	public EnumWithState getData() {
		return data;
	}
	
}
