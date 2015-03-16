package samples.effectivejava.item10;

public class GoodToStringWithStaticClass {

	public final static String NAME = "GoodToStringWithStaticClass";
	
	private final int number;
	
	public GoodToStringWithStaticClass(final int number) {
		super();
		this.number = number;
	}

	@Override
	public String toString() {
		// Good toString uses all non-static fields
		return "GoodToStringClass [number=" + number + "]";
	}
}
