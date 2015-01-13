package samples.jsr305.nullness;

import java.util.Comparator;

import javax.annotation.Nonnull;



public class AnonymousClassConstructor {
	
	public void method() {
		@SuppressWarnings("unused")
		final Comparator<Long> comparator = new Comparator<Long>() {

			@Override
			public int compare(@Nonnull Long o1, @Nonnull Long o2) {
				return 0;
			}
		};
	}
}
