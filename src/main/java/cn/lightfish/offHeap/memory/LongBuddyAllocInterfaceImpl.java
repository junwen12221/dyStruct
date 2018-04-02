//package cn.lightfish.offHeap.memory;
//
//import cn.lightfish.offHeap.DyStruct;
//import sun.misc.Unsafe;
//
//import java.lang.reflect.Field;
//
//import static cn.lightfish.offHeap.DyStruct.$address;
//
//public class LongBuddyAllocInterfaceImpl implements LongAllocInterface {
//    Unsafe unsafe;
//    long b;
//
//    public LongBuddyAllocInterfaceImpl(int level) {
//        try {
//            Field f = Unsafe.class.getDeclaredField("theUnsafe");
//            f.setAccessible(true);
//            Unsafe unsafe = (Unsafe) f.get(null);
//            this.unsafe = unsafe;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        b = Buddy.buddy_new(level);
//    }
//
//    public long allocateMemory(long size) {
//        return Buddy.buddy_alloc(b,size);
//    }
//
//    public long reallocateMemory(long address, long bytes) {
//        return unsafe.reallocateMemory(address, bytes);
//    }
//
//    public void freeMemory(long address) {
//        Buddy.buddy_delete(b);
//    }
//
//    public int getInt(long address) {
//        return unsafe.getInt(address);
//    }
//
//    public void putInt(long address, int value) {
//        unsafe.putInt(address, value);
//
//    }
//
//    public byte getByte(long address) {
//        return unsafe.getByte(address);
//    }
//
//    public void putByte(long address, byte value) {
//        unsafe.putByte(address, value);
//    }
//
//    public short getShort(long address) {
//        return unsafe.getShort(address);
//    }
//
//    public void putShort(long address, short value) {
//        unsafe.putShort(address, value);
//    }
//
//    public long getLong(long address) {
//        return unsafe.getLong(address);
//    }
//
//    public void putLong(long address, long value) {
//        unsafe.putLong(address, value);
//    }
//
//    public float getFloat(long address) {
//        return unsafe.getFloat(address);
//    }
//
//    public void putFloat(long address, float value) {
//        unsafe.putFloat(address, value);
//
//    }
//
//    public double getDouble(long address) {
//        return unsafe.getDouble(address);
//    }
//
//    public void putDouble(long address, double value) {
//        unsafe.putDouble(address, value);
//
//    }
//
//    public boolean getBoolean(long address) {
//        return unsafe.getByte(address) == 1;
//
//    }
//
//    public void putBoolean(long address, boolean value) {
//        unsafe.putByte(address, (byte) (value ? 1 : 0));
//
//    }
//
//    public char getChar(long address) {
//        return unsafe.getChar(address);
//    }
//
//    public void putChar(long address, char value) {
//        unsafe.putChar(address, value);
//    }
//
//    public long getAddress(long address) {
//        return unsafe.getAddress(address);
//    }
//
//    public void putAddress(long address, long value) {
//        unsafe.putAddress(address, value);
//    }
//
//    public void setMemory(long address, long bytes, byte value) {
//        unsafe.setMemory(address, bytes, value);
//
//    }
//
//    public void copyMemory(long srcAddress, long destAddress, int bytes) {
//        unsafe.copyMemory(srcAddress, destAddress, bytes);
//    }
//
//
//}
