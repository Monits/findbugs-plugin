package samples.findbugs;

public class EqualsHashCodeDifferentFields {

	private static final int LONG_SHIFT_VALUE = 32;
	private final long id;
	private final long version;
	private final long type;

	public EqualsHashCodeDifferentFields(final long id, final long version, final long type) {
		this.id = id;
		this.version = version;
		this.type = type;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (type ^ (type >>> LONG_SHIFT_VALUE));
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
		final EqualsHashCodeDifferentFields other = (EqualsHashCodeDifferentFields) obj;
		if (id != other.id) {
			return false;
		}
		if (type != other.type) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "EqualsHashCodeDifferentFields [id=" + id + ", version=" + version + ", type=" + type + "]";
	}

}
