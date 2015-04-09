package samples.findbugs;

public class HashCodeContainsEquals {

	private static final int LONG_SHIFT_VALUE = 32;
	private final long id;
	private final long version;

	public HashCodeContainsEquals(final long id, final long version) {
		this.id = id;
		this.version = version;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> LONG_SHIFT_VALUE));
		result = prime * result + (int) (version ^ (version >>> LONG_SHIFT_VALUE));
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
		final HashCodeContainsEquals other = (HashCodeContainsEquals) obj;
		if (id != other.id) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "HashCodeContainsEquals [id=" + id + ", version=" + version + "]";
	}

}
