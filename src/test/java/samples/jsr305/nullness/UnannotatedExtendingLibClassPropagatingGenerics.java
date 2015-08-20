package samples.jsr305.nullness;

import de.ruedigermoeller.heapoff.FSTByteBufferOffheap;
import de.ruedigermoeller.heapoff.FSTOffHeapMap;


public class UnannotatedExtendingLibClassPropagatingGenerics extends FSTOffHeapMap<String, Long> {

	public UnannotatedExtendingLibClassPropagatingGenerics(
			final FSTByteBufferOffheap heap) {
		super(heap);
	}

	@Override
	public Long put(String key, Long value) {
		return super.put(key, value);
	}
}
