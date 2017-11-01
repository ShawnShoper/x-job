package org.shoper.log.util.constans;

public enum Tag {
    T("t", 23), L("l", 5), P("p", -5), MN("mn", 30), LN("ln", -5), CN("cn", 30), C("c", 0);

    Tag(String name, int offset) {
        this.name = name;
        this.offset = offset;
    }

    public String name;
    public int offset;

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}