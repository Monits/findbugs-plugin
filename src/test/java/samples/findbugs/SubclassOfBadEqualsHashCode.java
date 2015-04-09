package samples.findbugs;

import javax.annotation.Nonnull;

public class SubclassOfBadEqualsHashCode extends HashCodeContainsEquals {

	private final String description;

	public SubclassOfBadEqualsHashCode(final long id, final long version, @Nonnull final String description) {
		super(id, version);
		this.description = description;
	}

	@Nonnull
	public String getDescription() {
		return description;
	}

	@Override
	@Nonnull
	public String toString() {
		return "SubclassOfBadEqualsHashCode [description=" + description + "]";
	}

}
