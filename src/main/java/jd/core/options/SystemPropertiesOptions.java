package jd.core.options;

import static jd.core.JavaDecompilerConstants.SystemProperties.DEBUG;
import static jd.core.JavaDecompilerConstants.SystemProperties.DEBUG_DEFAULT;
import static jd.core.JavaDecompilerConstants.SystemProperties.DISCARD_LOCATION;
import static jd.core.JavaDecompilerConstants.SystemProperties.DISCARD_LOCATION_DEFAULT;
import static jd.core.JavaDecompilerConstants.SystemProperties.LINE_NUMBERS;
import static jd.core.JavaDecompilerConstants.SystemProperties.LINE_NUMBERS_DEFAULT;
import static jd.core.JavaDecompilerConstants.SystemProperties.METADATA;
import static jd.core.JavaDecompilerConstants.SystemProperties.METADATA_DEFAULT;
import jd.core.IOUtils;

public class SystemPropertiesOptions implements DecompilerOptions {

	@Override
	public boolean isDebug() {
		return IOUtils.getBoolean(DEBUG, DEBUG_DEFAULT);
	}

	@Override
	public boolean isDisplayLineNumbers() {
		return IOUtils.getBoolean(LINE_NUMBERS, LINE_NUMBERS_DEFAULT);
	}

	@Override
	public boolean isDisplayMetadata() {
		return IOUtils.getBoolean(METADATA, METADATA_DEFAULT);
	}

	@Override
	public boolean isDiscardLocation() {
		return IOUtils.getBoolean(DISCARD_LOCATION, DISCARD_LOCATION_DEFAULT);
	}

}
