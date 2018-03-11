package cn.lightfish.offHeap;

interface OffHeapInterface {
    long allocateMemory(long size);

    long reallocateMemory(long address, long bytes);

    void freeMemory(long address);

    int getInt(long address);

    void putInt(long address, int value);

    byte getByte(long address);

    void putByte(long address, byte value);

    short getShort(long address);

    void putShort(long address, short value);

    long getLong(long address);

    void putLong(long address, long value);

    float getFloat(long address);

    void putFloat(long address, float value);

    double getDouble(long address);

    void putDouble(long address, double value);

    boolean getBoolean(long address);

    void putBoolean(long address, boolean value);

    char getChar(long address);

    void putChar(long address, char value);

    long getAddress(long address);

    void putAddress(long address, long value);

    void setMemory(long address, long bytes, byte value);

    void copyMemory(long srcAddress, long destAddress, int bytes);
}
