package cn.lightfish.offHeap;


public class Member {
    public long offset;
    public Type type;
    public String name;
    public String structName;

    public Member(long offset, Type type, String name, String structName) {
        this.offset = offset;
        this.type = type;
        this.name = name;
        this.structName = structName;
    }

    @Override
    public String toString() {
        return "Member{" +
                "offset=" + offset +
                ", type=" + type +
                ", name='" + name + '\'' +
                ", structName='" + structName + '\'' +
                '}';
    }

    public long getOffset() {
        return offset;
    }

    public Type getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getStructName() {
        return structName;
    }
}