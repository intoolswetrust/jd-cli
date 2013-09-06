package jd.ide.intellij.config;

import static jd.core.JavaDecompilerConstants.PROP_LINE_NUMBERS;
import static jd.core.JavaDecompilerConstants.PROP_LINE_NUMBERS_DEFAULT;
import static jd.core.JavaDecompilerConstants.PROP_METADATA;
import static jd.core.JavaDecompilerConstants.PROP_METADATA_DEFAULT;
import jd.core.IOUtils;

public class JDPluginComponent {

    public boolean isShowLineNumbersEnabled() {
        return IOUtils.getBoolean(PROP_LINE_NUMBERS, PROP_LINE_NUMBERS_DEFAULT);
    }

    public boolean isShowMetadataEnabled() {
        return IOUtils.getBoolean(PROP_METADATA, PROP_METADATA_DEFAULT);
    }

}
