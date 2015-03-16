package samples.effectivejava.item10;

public class GoodToStringCompositeClass {

	private final int number;
	private final String name;
	private final NoFieldClass nfc;
	
	public GoodToStringCompositeClass(final int number, final String name, final NoFieldClass nfc) {
		super();
		this.number = number;
		this.name = name;
		this.nfc = nfc;
	}

	@Override
	public String toString() {
		// Good toString uses all fields 
		return "GoodToStringCompositeClass [number=" + number + ", name=" + name + "]";
	}

	public NoFieldClass getNfc() {
		return nfc;
	}
	
}
