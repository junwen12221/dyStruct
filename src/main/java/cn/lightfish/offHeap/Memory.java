package cn.lightfish.offHeap;


import cn.lightfish.offHeap.Member;
import cn.lightfish.offHeap.StructInfo;
import cn.lightfish.offHeap.Type;

import java.util.Map;

public class Memory {
    public static StructInfo build(String name, Object... map) {
        StructInfo meta = new StructInfo(name);
        for (int i = 0; i < map.length; i += 2) {
            Object value = map[i + 1];
            String key = (String) map[i];
            if (value instanceof StructInfo) {
                StructInfo innerStruct = (StructInfo) value;
                meta.map.put(key, new Member(meta.size, Type.Struct, key,name));
                innerStruct.map.forEach((key1, value1) -> {
                    String k = key + "." + key1;
                    meta.map.put(k, new Member(meta.size, value1.type, k,name));
                    meta.size += value1.type.getSize();
                });
            } else {
                meta.put(key, value instanceof String?Type.valueOf((String) value):(Type) value);
            }
        }
        return meta;
    }
}