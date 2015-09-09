package samples.jsr305.nullness;

import java.util.Iterator;

public class ChildOfUnannotatedBoundGenerics extends UnannotatedBoundGenerics implements Iterable<UnannotatedBoundGenerics> {

	@Override
	public int compareTo(final UnannotatedBoundGenerics o) {
		return 0;
	}

	@Override
	public Iterator<UnannotatedBoundGenerics> iterator() {
		return null;
	}
}
