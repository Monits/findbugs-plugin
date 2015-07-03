package samples.effectivejava.item10;

public class GoodSuppressedToStringComposite {

	private final SuppressedToStringClass field;
	
	public GoodSuppressedToStringComposite(final SuppressedToStringClass field) {
		this.field = field;
	}

	public SuppressedToStringClass getField() {
		return field;
	}
}
