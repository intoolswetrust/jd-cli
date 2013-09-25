package jd.core;

public final class JavaDecompilerConstants {

	public static final String TMP_DIR = System.getProperty("java.io.tmpdir");

	public static final String NATIVE_LIB_NAME = "libjd-intellij";
	public static final String NATIVE_LIB_TMP_PREFIX = NATIVE_LIB_NAME + "-tmp";

	public static class SystemProperties {
		public static final String LINE_NUMBERS = "jd.lineNumbers";
		public static final boolean LINE_NUMBERS_DEFAULT = false;
		public static final String METADATA = "jd.metadata";
		public static final boolean METADATA_DEFAULT = true;
		public static final String DISCARD_LOCATION = "jd.discardLocation";
		public static final boolean DISCARD_LOCATION_DEFAULT = true;
		public static final String DEBUG = "debug";
		public static final boolean DEBUG_DEFAULT = false;
	}

}
