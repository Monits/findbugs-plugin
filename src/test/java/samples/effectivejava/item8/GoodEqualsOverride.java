package samples.effectivejava.item8;

import javax.annotation.Nonnull;


public class GoodEqualsOverride extends BaseNonEqualsConcreteClass {
	private String code;

	public GoodEqualsOverride() {
		super(GoodEqualsOverride.class.getSimpleName());
		code = "example";
	}

	@Nonnull
	public String getCode() {
		return code;
	}

	public void setVersion(@Nonnull final String code) {
		this.code = code;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + code.hashCode();
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
		final GoodEqualsOverride other = (GoodEqualsOverride) obj;
		if (!code.equals(other.code)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "GoodEqualsOverride [code=" + code + "]";
	}
}
