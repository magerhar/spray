package org.eclipselabs.spray.generator.graphiti.templates;

import org.eclipselabs.spray.generator.graphiti.util.SprayOutputConfigurationProvider;
import org.eclipselabs.spray.xtext.generator.IFileSystemAccessUtil;

import com.google.inject.Inject;

public class JavaGenFile extends GenFile {
    @Inject
    public JavaGenFile(IFileSystemAccessUtil fsaUtil) {
        super(fsaUtil);
    }

    protected String className;

    public String getClassName() {
        return className;
    }

    protected String packageName;

    public void setPackageAndClass(String pack, String cls) {
        this.packageName = pack;
        this.className = cls;
    }

    public void setPackageAndClass(String qualifiedName) {
        int idx = qualifiedName.lastIndexOf('.');
        if (idx < 0)
            throw new IllegalArgumentException("Not a qualified class name: " + qualifiedName);
        this.packageName = qualifiedName.substring(0, idx);
        this.className = qualifiedName.substring(idx + 1);
    }

    public String getBaseClassName() {
        return className + "Base";
    }

    public String getBaseFileName() {
        return getBaseClassName() + ".java";
    }

    public String getFileName() {
        return getClassName() + ".java";
    }

    public String getPathName() {
        return packageName.replaceAll("\\.", "/") + "/" + getFileName();
    }

    public String getBasePathName() {
        return packageName.replaceAll("\\.", "/") + "/" + getBaseFileName();
    }

    public String getPackageName() {
        return packageName;
    }

    @Override
    public boolean extensionFileExists() {
        return fsaUtil.fileExists(fsa, SprayOutputConfigurationProvider.OUTPUTCONFIG_SRCMAN, getPathName());
    }

    @Override
    public void generateFile(String fileName, CharSequence contents) {
        fsa.generateFile(fileName, SprayOutputConfigurationProvider.OUTPUTCONFIG_SRCGEN, contents);
    }

    public void generateBaseFile(String fileName, CharSequence contents) {
        if (fsaUtil.fileExists(fsa, SprayOutputConfigurationProvider.OUTPUTCONFIG_SRCMAN, getPathName())) {
            fsa.generateFile(fileName, SprayOutputConfigurationProvider.OUTPUTCONFIG_SRCMAN, contents);
        } else {
            fsa.generateFile(fileName, SprayOutputConfigurationProvider.OUTPUTCONFIG_SRCGENCOND, contents);
        }
    }
}
