package samples.jsr305.nullness;


public class UnannotatedEnumLookAlike {
	
	/*
	 * The signature of this method matches that of an enum's values(),
	 * but it's not and should be reported
	 */
    public static UnannotatedEnumLookAlike[] values() {
        return null;
    }
    
    /*
	 * The signature of this method matches that of an enum's valueOf(String),
	 * but it's not and should be reported
	 */
    public static UnannotatedEnumLookAlike valueOf(final String s) {
        return null;
    }
}
