package samples.effectivejava.item10;

import javax.annotation.Nonnull;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

public class GoodToStringAllIgnoredFieldClass {

	@SuppressFBWarnings(value = "MISSING_FIELD_IN_TO_STRING",
			justification = "Testing ignored fields")
	private final int number;
	
	@SuppressFBWarnings(value = "MISSING_FIELD_IN_TO_STRING",
			justification = "Testing ignored fields")
	private final String name;
	
	public GoodToStringAllIgnoredFieldClass(final int number, @Nonnull final String name) {
		super();
		this.number = number;
		this.name = name;
	}

	// No toString override, all fields are ignored
}
