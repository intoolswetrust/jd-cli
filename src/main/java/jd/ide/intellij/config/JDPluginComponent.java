package jd.ide.intellij.config;

import jd.core.options.OptionsManager;

public class JDPluginComponent {

	public boolean isShowLineNumbersEnabled() {
		return OptionsManager.getOptions().isDisplayLineNumbers();
	}

	public boolean isShowMetadataEnabled() {
		return OptionsManager.getOptions().isDisplayMetadata();
	}

}
