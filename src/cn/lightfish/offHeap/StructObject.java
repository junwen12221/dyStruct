package cn.lightfish.offHeap;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class StructObject {
    StructInfo StructInfo;
    long address;
    MemoryInterface memory;

    public <T> T $(String name) {
        try {
            Member member = StructInfo.map.get(name);
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
            //System.out.printf(address + ".%s is %s%n", name, Objects.toString(result));
            info(op, offset);
            return (T) result;
        }catch (Exception e){
            e.printStackTrace();
        }
  return null;
    }

    public <R> void $(String name, R value) {
        Member member = StructInfo.map.get(name);
        long offset = member.offset;
        long finalAddress = offset + address;
        System.out.printf("%s = %s %n", address + "." + name, value.toString());
        if (value instanceof Boolean) {
            info("memory.putBoolean(address+%d, (boolean) value);", offset);
            memory.putBoolean(finalAddress, (Boolean) value);
        } else if (value instanceof Integer) {
            info("memory.putInt(address+%d, (int) value);", offset);
            memory.putInt(finalAddress, (Integer) value);
        } else if (value instanceof Short) {
            info("memory.putShort(address+%d, (short) value);", offset);
            memory.putShort(finalAddress, (Short) value);
        } else if (value instanceof Long) {
            info("memory.putLong(address+%d, (long) value);", offset);
            memory.putLong(finalAddress, (Long) value);
        } else if (value instanceof Double) {
            info("memory.putDouble(address+%d, (double) value);", offset);
            memory.putDouble(finalAddress, (Double) value);
        } else if (value instanceof Float) {
            info("memory.putFloat(address+%d, (float) value);", offset);
            memory.putFloat(finalAddress, (Float) value);
        } else if (value instanceof Character) {
            info("memory.putChar(address+%d, (char) value);", offset);
            memory.putChar(finalAddress, (Character) value);
        } else if (value instanceof Byte) {
            info("memory.putByte(address+%d, (byte) value);", offset);
            memory.putByte(finalAddress, (Byte) value);
        } else {
            StructObject v = (StructObject) value;
            long start = member.offset + address;
            long size = v.StructInfo.size;
            long copyAddress = v.address;
            for (int i = 0; i < size; i++) {
                info("memory.copyMemory(address,copyAddress,%s", size);
                memory.putByte(start++, v.memory.getByte(copyAddress++));
            }
        }

    }

    private void info(String templeate, Object... args) {
//        System.out.printf(templeate + "%n", args);
//        List<StackWalker.StackFrame> list = new ArrayList<>();
//        StackWalker.getInstance().forEach(list::add);
//        System.out.println(list.get(2));
    }

    public void setStructInfo(StructInfo StructInfo) {
        this.StructInfo = StructInfo;
    }

    public MemoryInterface getMemory() {
        return memory;
    }

    public void setMemory(MemoryInterface memory) {
        this.memory = memory;
    }

    public long getAddress() {
        return address;
    }

    public void setAddress(long address) {
        this.address = address;
    }
}
