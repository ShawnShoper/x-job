package org.x.job.executor.io;

import java.util.List;

public class Treenode {

    private String name;
    private List<Treenode> children;
    private String path;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Treenode> getChildren() {
        return children;
    }

    public void setChildren(List<Treenode> children) {
        this.children = children;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
