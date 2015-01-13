package samples.jsr305.nullness;

import javax.annotation.Nonnull;

public enum AnnotatedArgumentsEnum {
	VALUE("whatever");

	private AnnotatedArgumentsEnum(@Nonnull final String value) {
	}

}
