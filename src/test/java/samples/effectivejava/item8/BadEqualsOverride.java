package samples.effectivejava.item8;


public class BadEqualsOverride extends BaseConcreteClass {
	private int version;

	public BadEqualsOverride() {
		super(BadEqualsOverride.class.getSimpleName());
		version = 1;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(final int version) {
		this.version = version;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + version;
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final BadEqualsOverride other = (BadEqualsOverride) obj;
		if (version != other.version) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "BadEqualsOverride [version=" + version + "]";
	}
}
