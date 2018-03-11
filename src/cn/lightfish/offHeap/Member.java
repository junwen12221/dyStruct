package cn.lightfish.offHeap;

public class Member {
    public long offset;
    public Type type;
    public String name;

    public Member(long offset, Type type, String name) {
        this.offset = offset;
        this.type = type;
        this.name = name;
    }

}