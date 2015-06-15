package samples.effectivejava.item10;

import javax.annotation.Nonnull;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

public class GoodToStringIgnoredFieldClass {

	@SuppressFBWarnings(value = "MISSING_FIELD_IN_TO_STRING",
			justification = "Testing ignored fields")
	private final int number;
	
	private final String name;
	
	public GoodToStringIgnoredFieldClass(final int number, @Nonnull final String name) {
		super();
		this.number = number;
		this.name = name;
	}

	@Override
	public String toString() {
		// Good toString uses all fields not being ignored 
		return "GoodToStringClass [name=" + name + "]";
	}
	
}
