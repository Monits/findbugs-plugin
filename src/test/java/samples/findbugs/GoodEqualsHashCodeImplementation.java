package samples.findbugs;

import java.util.List;

import javax.annotation.Nonnull;

public class GoodEqualsHashCodeImplementation {

	private static final int LONG_SHIFT_VALUE = 32;
	private final long id;
	private final List<String> versions;

	public GoodEqualsHashCodeImplementation(final long id, @Nonnull final List<String> versions) {
		this.id = id;
		this.versions = versions;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> LONG_SHIFT_VALUE));
		result = prime * result + ((versions == null) ? 0 : versions.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final GoodEqualsHashCodeImplementation other = (GoodEqualsHashCodeImplementation) obj;
		if (id != other.id) {
			return false;
		}
		if (versions == null) {
			if (other.versions != null) {
				return false;
			}
		} else if (!versions.equals(other.versions)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "GoodEqualsHashCodeImplementation [id=" + id + ", versions=" + versions + "]";
	}

}
