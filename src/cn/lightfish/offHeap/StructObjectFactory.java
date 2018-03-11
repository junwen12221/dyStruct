package cn.lightfish.offHeap;

import java.util.HashMap;
import java.util.Map;

public class StructObjectFactory {
    final MemoryInterface memory;
    final Map<Long, StructInfo> map = new HashMap<>();

    public StructObjectFactory(MemoryInterface memory) {
        this.memory = memory;
    }

    public StructObject create(StructInfo meta) {
        StructObject structObject = new StructObject();
        structObject.setStructInfo(meta);
        long address = this.memory.allocateMemory(meta.size);
        structObject.setAddress(address);
        structObject.setMemory(this.memory);
        map.put(address, meta);
        return structObject;
    }

    public void delete(StructObject structObject) {
        map.remove(structObject.address);
        memory.freeMemory(structObject.address);
    }

    public StructInfo getStructInfo(long address) {
        return map.get(address);
    }
}