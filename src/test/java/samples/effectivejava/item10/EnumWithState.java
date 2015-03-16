package samples.effectivejava.item10;

public enum EnumWithState {
	ONE_VALUE(true),
	SECOND_VALUE(false);
	
	private final boolean innerVal;
	
	private EnumWithState(final boolean val) {
		innerVal = val;
	}

	public boolean isInnerVal() {
		return innerVal;
	}
}
