package samples.jsr305.nullness;

import java.util.HashMap;


public class UnannotatedIndirectGenericBinding<A, B> extends HashMap<A, B> {

	private static final long serialVersionUID = 7026748794809094126L;

	public static class Bounded extends UnannotatedIndirectGenericBinding<String, Long> {

		private static final long serialVersionUID = 3142640311424915721L;

		@Override
		public Long put(final String key, final Long value) {
			return super.put(key, value);
		}
	}
}
