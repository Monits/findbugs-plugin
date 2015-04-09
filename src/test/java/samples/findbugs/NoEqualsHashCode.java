package samples.findbugs;

import javax.annotation.Nonnull;

public class NoEqualsHashCode {

	private final long id;
	private final String description;

	public NoEqualsHashCode(final long id, @Nonnull final String description) {
		this.id = id;
		this.description = description;
	}

	public long getId() {
		return id;
	}

	@Nonnull
	public String getDescription() {
		return description;
	}

	@Override
	public String toString() {
		return "NoEqualsHashCode [id=" + id + ", description=" + description + "]";
	}

}
