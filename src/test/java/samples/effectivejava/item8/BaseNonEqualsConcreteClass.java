package samples.effectivejava.item8;

import javax.annotation.Nonnull;

public class BaseNonEqualsConcreteClass {

	private final String name;
	
	public BaseNonEqualsConcreteClass(@Nonnull final String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "BaseNonEqualsConcreteClass [name=" + name + "]";
	}

}
