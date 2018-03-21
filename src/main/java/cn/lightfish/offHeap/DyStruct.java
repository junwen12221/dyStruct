package cn.lightfish.offHeap;

import cn.lightfish.offHeap.memory.IntDebugAllocInterfaceImpl;
import cn.lightfish.offHeap.memory.MemoryInterface;

import java.util.HashMap;
import java.util.Map;

public class DyStruct {
    static MemoryInterface memory = new MemoryInterface(new IntDebugAllocInterfaceImpl());
    final static Map<Long, StructInfo> map = new HashMap<>();
    static boolean isDebug = true;

    public static void setDebug(boolean debug) {
        isDebug = debug;
    }

    public static long $malloc(StructInfo structInfo) {
        long address = memory.allocateMemory(structInfo.size);
        if (isDebug) {
            map.put(address, structInfo);
        }
        return address;
    }

    public static void setMemoryInterface(MemoryInterface memoryInterface) {
        memory = memoryInterface;
    }


    public static void $free(long address) {
        if (isDebug) {
            map.remove(address);
        }
        memory.freeMemory(address);
    }

    public static StructInfo build(String name, Object... map) {
        return Memory.build(name, map);
    }

    public static <T> T $(Long address, String name) {
        StructInfo structInfo = map.get(address);
        if (structInfo == null) return null;
        Member member = structInfo.map.get(name);
        long offset = member.offset;
        long finalOffset = offset + address;
        Object result = null;
        String op = "";
        switch (member.type) {
            case Int:
                result = memory.getInt(finalOffset);
                op = "memory.getInt(address+%d);";
                break;
            case Long:
                result = memory.getLong(finalOffset);
                op = "memory.getLong(address+%d);";
                break;
            case Short:
                result = memory.getShort(finalOffset);
                op = "memory.getShort(address+%d);";
                break;
            case Byte:
                result = memory.getByte(finalOffset);
                op = "memory.getByte(address+%d);";
                break;
            case Float:
                result = memory.getFloat(finalOffset);
                op = "memory.getFloat(address+%d);";
                break;
            case Double:
                result = memory.getDouble(finalOffset);
                op = "memory.getDouble(address+%d);";
                break;
            case Char:
                result = memory.getChar(finalOffset);
                op = "memory.getChar(address+%d);";
                break;
            case Booelean:
                result = memory.getBoolean(finalOffset);
                op = "memory.getBoolean(address+%d);";
                break;
            case Struct:
                result = finalOffset;
                break;
            case Address:
                result = memory.getAddress(finalOffset);
                op = "memory.getAddress(address+%d);";
                break;
        }
        return (T) result;
    }

    public static boolean $booelan(long address, long offset) {
        return memory.getBoolean(address + offset);
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
        memory.putBoolean(address + offset, value);
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

    public static boolean $booelan(Long address, String name) {
        return $(address, name);
    }

    public static int $int(Long address, String name) {
        return $(address, name);
    }

    public static short $short(Long address, String name) {
        return $(address, name);
    }

    public static float $float(Long address, String name) {
        return $(address, name);
    }

    public static double $double(Long address, String name) {
        return $(address, name);
    }

    public static char $char(Long address, String name) {
        return $(address, name);
    }

    public static byte $byte(Long address, String name) {
        return $(address, name);
    }

    public static long $long(Long address, String name) {
        return $(address, name);
    }

    public static long $address(Long address, String name) {
        return $(address, name);
    }

    public static long $$address(Long address, String name, String name2) {
        return $($(address, name), name2);
    }

    public static void $byte(long address, String name, byte value) {
        $(address, name, value);
    }

    public static void $char(long address, String name, char value) {
        $(address, name, value);
    }

    public static void $short(long address, String name, short value) {
        $(address, name, value);
    }

    public static void $int(long address, String name, int value) {
        $(address, name, value);
    }

    public static void $long(long address, String name, long value) {
        $(address, name, value);
    }

    public static void $boolean(long address, String name, boolean value) {
        $(address, name, value);
    }

    public static void $float(long address, String name, float value) {
        $(address, name, value);
    }

    public static void $double(long address, String name, double value) {
        $(address, name, value);
    }

    public static <R> void $(Long address, String name, R value) {
        StructInfo structInfo = map.get(address);
        Member member = structInfo.map.get(name);
        long offset = member.offset;
        long finalAddress = offset + address;
        switch (member.type) {
            case Booelean:
                info("memory.putBoolean(address+%d, (boolean) value);", offset);
                memory.putBoolean(finalAddress, (Boolean) value);
                break;
            case Int:
                info("memory.putInt(address+%d, (int) value);", offset);
                memory.putInt(finalAddress, (Integer) value);
                break;
            case Long:
                info("memory.putLong(address+%d, (long) value);", offset);
                memory.putLong(finalAddress, (Long) value);
                break;
            case Short:
                info("memory.putShort(address+%d, (short) value);", offset);
                memory.putShort(finalAddress, (Short) value);
                break;
            case Byte:
                info("memory.putByte(address+%d, (byte) value);", offset);
                memory.putByte(finalAddress, (Byte) value);
                break;
            case Float:
                info("memory.putFloat(address+%d, (float) value);", offset);
                memory.putFloat(finalAddress, (Float) value);
                break;
            case Double:
                info("memory.putDouble(address+%d, (double) value);", offset);
                memory.putDouble(finalAddress, (Double) value);
                break;
            case Char:
                info("memory.putChar(address+%d, (char) value);", offset);
                memory.putChar(finalAddress, (Character) value);
                break;
            case Address:
                info("memory.putAddress(address+%d, (long) value);", offset);
                memory.putLong(finalAddress, (Long) value);
                break;
            case Struct:
                map.get(address);
                long start = member.offset + address;
                long size = map.get(address).size;
                long copyAddress = address;
                for (int i = 0; i < size; i++) {
                    info("memory.copyMemory(address,copyAddress,%s", size);
                    memory.putByte(start++, memory.getByte(copyAddress++));
                }
                break;
        }
    }

    private static void info(String templeate, Object... args) {
//        System.out.printf(templeate + "%n", args);
//        List<StackWalker.StackFrame> list = new ArrayList<>();
//        StackWalker.getInstance().forEach(list::add);
//        System.out.println(list.get(2));
    }


}
