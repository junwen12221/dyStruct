package cn.lightfish.offHeap;

import java.io.BufferedReader;
import java.util.*;
import java.util.stream.Collectors;

public class StructInfo {
    long size = 0;
    String name;
    Map<String, Member> map = new HashMap<>();

    public StructInfo(String name) {
        this.name = name;
    }

    public void put(String name, Type type) {
        try {
            map.put(name, new Member(size, type, name,this.name));
            size+= type.size;
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void print() {
        BufferedReader reader;
        map.entrySet()
                .stream()
                .map(Map.Entry::getValue)
                .map((entry -> {
                    String type = entry.getType()==Type.Address?"long":entry.getType().toString().toLowerCase();
                    return String.format("public final static %s %s = %d;%n",type, entry.name.toUpperCase(),entry.offset);}))
                .forEach(System.out::println);
    }

    @Override
    public String toString() {
        return "StructInfo{" +
                "size=" + size +
                ", name='" + name + '\'' +
                ", map=" + map +
                '}';
    }

    public long getSize() {
        return size;
    }

    public String getName() {
        return name;
    }

    public Map<String, Member> getMap() {
        return map;
    }
}