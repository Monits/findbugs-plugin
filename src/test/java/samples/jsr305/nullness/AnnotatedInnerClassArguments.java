package samples.jsr305.nullness;

import javax.annotation.Nonnull;


public class AnnotatedInnerClassArguments {
	
	public class Inner {
		public void method(@Nonnull Object value) {}
	}
}
