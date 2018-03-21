package cn.lightfish.offHeap.memory;

public interface IntAllocInterface {
    int allocateMemory(int size);

    int reallocateMemory(int address, int bytes);

    void freeMemory(int address);

    int getInt(int address);

    void putInt(int address, int value);

    byte getByte(int address);

    void putByte(int address, byte value);

    short getShort(int address);

    void putShort(int address, short value);

    long getLong(int address);

    void putLong(int address, long value);

    float getFloat(int address);

    void putFloat(int address, float value);

    double getDouble(int address);

    void putDouble(int address, double value);

    boolean getBoolean(int address);

    void putBoolean(int address, boolean value);

    char getChar(int address);

    void putChar(int address, char value);

    int getAddress(int address);

    void putAddress(int address, int value);

    void setMemory(int address, int bytes, byte value);

    void copyMemory(int srcAddress, int destAddress, int bytes);
}
