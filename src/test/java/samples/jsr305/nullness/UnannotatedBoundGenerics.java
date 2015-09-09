package samples.jsr305.nullness;

public class UnannotatedBoundGenerics implements Comparable<UnannotatedBoundGenerics> {

	@Override
	public int compareTo(final UnannotatedBoundGenerics o) {
		return 0;
	}
}
