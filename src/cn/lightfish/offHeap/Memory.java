package cn.lightfish.offHeap;


import java.util.Map;

public class Memory {
    public static StructInfo build(String name, Object... map) {
        StructInfo meta = new StructInfo(name);
        for (int i = 0; i < map.length; i += 2) {
            Object value = map[i + 1];
            String key = (String) map[i];
            if (value instanceof StructInfo) {
                StructInfo innerStruct = (StructInfo) value;
                meta.map.put(key, new Member(meta.size, Type.Struct, key));
                innerStruct.map.forEach((key1, value1) -> {
                    String k = key + "." + key1;
                    meta.map.put(k, new Member(meta.size += value1.type.getSize(), value1.type, k));
                });
            } else {
                meta.put(key, (Type) value);
            }
        }
        Map.Entry[] entries = meta.map.entrySet()
                .stream().map((e) -> Map.entry(e.getKey(), e.getValue())).toArray(Map.Entry[]::new);
        meta.map = Map.ofEntries(entries);
        return meta;
    }
}