package cn.lightfish.offHeap;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class HeapInterfaceImpl implements HeapInterface {
    ByteBuffer byteBuffer = ByteBuffer.allocate(8 * 1024);
    List<Integer> list = new ArrayList<>();

    public int allocateMemory(int size) {
        int address = byteBuffer.position();
        list.add(address);
        byteBuffer.position(byteBuffer.position() + size);
        return address;
    }


    public int reallocateMemory(int address, int bytes) {
        int newAddress = this.allocateMemory(bytes);
        this.copyMemory(address, this.allocateMemory(bytes), bytes);
        list.remove(newAddress);
        return newAddress;
    }

    public void freeMemory(int address) {
        list.remove(Integer.valueOf(address));
    }

    public int getInt(int address) {
        return byteBuffer.getInt(address);
    }

    public void putInt(int address, int value) {
        byteBuffer.putInt(address, value);
    }

    public byte getByte(int address) {
        return byteBuffer.get(address);
    }

    public void putByte(int address, byte value) {
        byteBuffer.put(address, value);
    }

    public short getShort(int address) {
        return byteBuffer.getShort(address);
    }

    public void putShort(int address, short value) {
        byteBuffer.putShort(address, value);
    }

    public long getLong(int address) {
        return byteBuffer.getLong(address);
    }

    public void putLong(int address, long value) {
        byteBuffer.putLong(address, value);
    }

    public float getFloat(int address) {
        return byteBuffer.getFloat(address);
    }

    public void putFloat(int address, float value) {
        byteBuffer.putFloat(address, value);
    }

    public double getDouble(int address) {
        return byteBuffer.getDouble(address);
    }

    public void putDouble(int address, double value) {
        byteBuffer.putDouble(address, value);
    }

    public boolean getBoolean(int address) {
        return byteBuffer.get(address) == 1;
    }

    public void putBoolean(int address, boolean value) {
        byteBuffer.put(address, (byte) (value ? 1 : 0));
    }

    public char getChar(int address) {
        return byteBuffer.getChar(address);
    }

    public void putChar(int address, char value) {
        byteBuffer.putChar(address, value);
    }

    public int getAddress(int address) {
        return (int) byteBuffer.getLong(address);
    }

    public void putAddress(int address, int value) {
        byteBuffer.putLong(address, value);
    }

    public void setMemory(int address, int bytes, byte value) {
        int end = address + bytes;
        for (int i = address; i < end; i++) {
            this.byteBuffer.put(i, value);
        }
    }

    public void copyMemory(int srcAddress, int destAddress, int bytes) {
        ByteBuffer byteBuffer = this.byteBuffer;
        for (int i = 0; i < bytes; i++) {
            byteBuffer.put(destAddress + i, byteBuffer.get(srcAddress + i));
        }
    }
}
