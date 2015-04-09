package samples.findbugs;

import java.util.List;

import javax.annotation.Nonnull;

@SuppressWarnings({ "PMD.OverrideBothEqualsAndHashcode", "checkstyle:equalshashcode" })
public class OnlyEqualsImplementation {

	private final List<String> versions;

	public OnlyEqualsImplementation(@Nonnull final List<String> versions) {
		this.versions = versions;
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
		final OnlyEqualsImplementation other = (OnlyEqualsImplementation) obj;
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
		return "OnlyEqualsImplementation [versions=" + versions + "]";
	}

}
