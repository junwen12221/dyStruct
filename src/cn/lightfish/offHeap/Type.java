package cn.lightfish.offHeap;

public enum Type {
    Booelean(1), Int(4), Long(8), Short(4), Byte(1), Float(4), Double(8), Char(8), Address(8), Struct(0);
    final int size;

    Type(int size) {
        this.size = size;
    }

    public int getSize() {
        return size;
    }
}
    