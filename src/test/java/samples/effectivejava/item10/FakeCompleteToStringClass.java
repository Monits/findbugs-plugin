package samples.effectivejava.item10;

public class FakeCompleteToStringClass {

	private final int number;
	private final String name;
	
	public FakeCompleteToStringClass(final int number, final String name) {
		super();
		this.number = number;
		this.name = name;
	}

	@Override
	public String toString() {
		System.out.println(name);	// but we ARE accessing name for some other reason
		// Incomplete toString, not including name 
		return "FakeCompleteToStringClass [number=" + number + "]";
	}
}
