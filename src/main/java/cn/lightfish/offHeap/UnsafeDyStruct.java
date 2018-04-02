package cn.lightfish.offHeap;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

public class UnsafeDyStruct {

    static Unsafe memory;

    static {
        try {
            Field f = Unsafe.class.getDeclaredField("theUnsafe");
            f.setAccessible(true);
            Unsafe unsafe = (Unsafe) f.get(null);
            memory = unsafe;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static long $malloc(StructInfo structInfo) {
        long address = memory.allocateMemory(structInfo.size);
        return address;
    }

    public static long $malloc(long size) {
        long address = memory.allocateMemory(size);
        return address;
    }

    public static void $free(long address) {
        memory.freeMemory(address);
    }

    public static boolean $booelan(long address, long offset) {
        return memory.getByte(address + offset) == 1;
    }

    public static int $int(long address, long offset) {
        return memory.getInt(address + offset);
    }

    public static short $short(long address, long offset) {
        return memory.getShort(address + offset);
    }

    public static float $float(long address, long offset) {
        return memory.getFloat(address + offset);
    }

    public static double $double(long address, long offset) {
        return memory.getDouble(address + offset);
    }

    public static char $char(long address, long offset) {
        return memory.getChar(address + offset);
    }

    public static byte $byte(long address, long offset) {
        return memory.getByte(address + offset);
    }

    public static long $long(long address, long offset) {
        return memory.getLong(address + offset);
    }

    public static long $address(long address, long offset) {
        return memory.getAddress(address + offset);
    }


    public static void $byte(long address, long offset, byte value) {
        memory.putByte(address + offset, value);
    }

    public static void $char(long address, long offset, char value) {
        memory.putChar(address + offset, value);
    }

    public static void $short(long address, long offset, short value) {
        memory.putShort(address + offset, value);
    }

    public static void $int(long address, long offset, int value) {
        memory.putInt(address + offset, value);
    }

    public static void $long(long address, long offset, long value) {
        memory.putLong(address + offset, value);
    }

    public static void $boolean(long address, long offset, boolean value) {
        memory.putByte(address + offset, (byte) (value ? 1 : 0));
    }

    public static void $float(long address, long offset, float value) {
        memory.putFloat(address + offset, value);
    }

    public static void $double(long address, long offset, double value) {
        memory.putDouble(address + offset, value);
    }

    public static void $address(long address, long offset, long value) {
        memory.putAddress(address + offset, value);
    }

    public static void main(String[] args) {

//    Map<Integer,Integer> map = new HashMap<>();
//        IntStream.range(1,16).forEach((i)->map.put(i,1));
//        Map<Integer,Integer> map2 = new HashMap<>(,map);
//        System.out.println(map);

    }
}
