package jd.core.output;

public interface JDOutput {
	void init(String basePath);

	void processClass(String className, String src);

	void commit();
}
