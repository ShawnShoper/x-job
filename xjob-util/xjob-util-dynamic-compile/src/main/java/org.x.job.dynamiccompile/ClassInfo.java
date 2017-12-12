package org.x.job.dynamiccompile;

import org.shoper.commons.core.StringUtil;

public class ClassInfo {
    private String className;
    private String packageName;

    public String getFullName() {
        return StringUtil.isEmpty(packageName) ? className : (packageName + "." + className);
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public ClassInfo(String className, String packageName) {
        this.className = className;
        this.packageName = packageName;
    }
}
