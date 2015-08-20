package samples.jsr305.nullness;

import java.io.IOException;

import de.ruedigermoeller.heapoff.FSTCompressed;
import de.ruedigermoeller.serialization.FSTConfiguration;


public class UnannotatedExtendingLibClass extends FSTCompressed<String> {

	@Override
	public void set(String object) throws IOException {
		super.set(object);
	}
	
	@Override
	protected void storeArray(byte[] buffer, int written) {
	}

	@Override
	protected FSTConfiguration getConf() {
		return null;
	}

	@Override
	public byte[] getArray() {
		return null;
	}

	@Override
	public int getLen() {
		return 0;
	}

	@Override
	public int getOffset() {
		return 0;
	}
}
