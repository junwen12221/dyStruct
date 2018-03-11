package cn.lightfish.offHeap;

import java.util.ArrayList;
import java.util.List;

public class DyStruct {
    final static MemoryInterface memory = new MemoryInterface(new HeapInterfaceImpl());
    final static StructObjectFactory globalStructObjectFactory = new StructObjectFactory(memory);

    public static long $malloc(StructInfo structInfo) {
        return globalStructObjectFactory.create(structInfo).address;
    }

    public static boolean $free(long address) {
        if (globalStructObjectFactory.map.remove(address) != null) {
            memory.freeMemory(address);
            return true;
        } else {
            return false;
        }
    }

    public static StructInfo build(String name, Object... map) {
        return Memory.build(name, map);
    }

    public static <T> T $(long address, String name) {
        StructObject structObject = new StructObject();
        structObject.setStructInfo(globalStructObjectFactory.getStructInfo(address));
        structObject.setAddress(address);
        structObject.setMemory(memory);
        return structObject.$(name);
    }

    public static <R> void $(long address, String name, R value) {
        StructInfo structInfo = globalStructObjectFactory.getStructInfo(address);
        Member member = structInfo.map.get(name);
        long offset = member.offset;
        long finalAddress = offset + address;
        System.out.printf("%s = %s %n", address + "." + name, value.toString());
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
                StructObject v = (StructObject) value;
                long start = member.offset + address;
                long size = v.StructInfo.size;
                long copyAddress = v.address;
                for (int i = 0; i < size; i++) {
                    info("memory.copyMemory(address,copyAddress,%s", size);
                    memory.putByte(start++, v.memory.getByte(copyAddress++));
                }
                break;
        }
    }

    private static void info(String templeate, Object... args) {
        System.out.printf(templeate + "%n", args);
        List<StackWalker.StackFrame> list = new ArrayList<>();
        StackWalker.getInstance().forEach(list::add);
        System.out.println(list.get(2));
    }

    public static void main(String[] args) throws Exception {
        StructInfo a = DyStruct.build("test1", "a", Type.Byte);
        StructInfo b = DyStruct.build("test2", "a", a);
        StructInfo c = DyStruct.build("test3", "c", Type.Address);
        long address = $malloc(b);//new struct
        $(address, "a.a", (byte) 1);
        byte aa = $(address, "a.a");// int a = address.a.a;
        long bb = $(address, "a");//struct a = address.a;
        long address2 = $malloc(c);
        $(address2, "c", address);
        $free($(address2, "c"));
        $free(address2);
    }


}
