package cn.lightfish.offHeap;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class StructInfo {
    long size = 0;
    String name;
    Map<String, Member> map = new HashMap<>();

    public StructInfo(String name) {
        this.name = name;
    }

    public void put(String name, Type type) {
        map.put(name, new Member(size += type.size, type, name));
    }

    public void print() {
        System.out.println(map.entrySet()
                .stream()
                .map(Map.Entry::getValue)
                .map((entry -> String.format("member name:%s type:%s size:%d offset:%d", entry.name, entry.type.name(), entry.type.getSize(), entry.offset)))
                .collect(Collectors.toList()));
    }
}