package jd.core.options;

public class OptionsManager {

	private static final OptionsManager INSTANCE = new OptionsManager();

	private ThreadLocal<DecompilerOptions> localOptions = new ThreadLocal<DecompilerOptions>() {

		private final DecompilerOptions INITIAL = new SystemPropertiesOptions();

		@Override
		protected DecompilerOptions initialValue() {
			return INITIAL;
		}

	};

	private OptionsManager() {
	}

	public static void setOptions(DecompilerOptions options) {
		if (options != null) {
			INSTANCE.localOptions.set(options);
		} else {
			INSTANCE.localOptions.remove();
		}
	}

	public static DecompilerOptions getOptions() {
		return INSTANCE.localOptions.get();
	}

}
