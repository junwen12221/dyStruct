package cn.lightfish.offHeap;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ParseUtil {
    public Map<String, StructInfo> parse(String code) {
        Map<String, StructInfo> map = new HashMap<>();
        for (int i = 0; i < code.length(); i++) {
            i = findStructInfo(code, i);
            if (i != -1) {
                StructInfo info = parseStructInfo(code, i, map);
                if (info != null) {
                    map.put(info.name, info);
                }
            } else {
                break;
            }
        }
        return map;
    }

    private int findStructInfo(String code, int start0) {
        return code.indexOf("DyStruct.build", start0);
    }

    private StructInfo parseStructInfo(String code, int start0, Map<String, StructInfo> map) {
        int start = code.indexOf('(', start0) + 1;
        int end = code.indexOf(')', start);
        String[] arrays = code.substring(start, end).split(",");
        String structName = arrays[0].replaceAll("\"", "");
        List args = new ArrayList<>();
        for (int i = 1; i < arrays.length - 1; i++) {
            String memberName = arrays[i];
            String type = arrays[i + 1].trim();
            type = type.replace("Type.", "");
            switch (type) {
                case "Booelean":
                case "Int":
                case "Long":
                case "Short":
                case "Byte":
                case "Float":
                case "Double":
                case "Char":
                case "Address":
                case "Struct":
                    args.add(memberName.replaceAll("\"", "").trim());
                    args.add(type);
                    break;
                default:
                    StructInfo structInfo = map.get(type);
                    if (structInfo != null) {
                        args.add(memberName.trim());
                        args.add(structInfo);
                    }
                    break;
            }
        }
        try {
            return Memory.build(structName, args.toArray());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String replaceMemberName(Map<String, StructInfo> info, String code,boolean isOffset) {
        Map<String, Member> map = info.values().stream().flatMap(i -> i.map.values().stream()).distinct().collect(Collectors.toMap(k -> k.name, v -> v));
        Set<String> keys = info.values().stream().flatMap(i -> i.map.values().stream()).map((i) -> "\\\"" + i.name + "\\\"").collect(Collectors.toSet());
        String memberNames = keys.stream().collect(Collectors.joining("|"));
        String findReg = "\\${1}(int|short|char|long|byte|float|double|boolean|address|malloc|free|)\\(\\s*.*?, *?(" + memberNames + ") *?(,|\\))";
//        String nameReg = "\".*?\"";
        Pattern $pattern = Pattern.compile(findReg);
        Matcher seg = $pattern.matcher(code);
        StringBuffer stringBuffer = new StringBuffer();
        while (seg.find()) {
            String str = seg.group();
            int start = str.indexOf("\"") + 1;
            int end = str.lastIndexOf("\"");
            String name = str.substring(start, end);
            Member member = map.get(name);
            if (member != null) {
                String untypeStr;
                if (isOffset){
                    untypeStr= str.replaceAll("\"" + name + "\"", "/*" + member.name + "*/" + String.valueOf(member.offset));
                }else {
                    untypeStr= str.replaceAll("\"" + name + "\"", name.toUpperCase());
                }
                int startleft=untypeStr.indexOf("(");
                if (!untypeStr.substring(startleft+1,startleft+2).startsWith("/*")&&!untypeStr.contains("*/$")){
                    untypeStr=untypeStr.replace("(","(/*"+member.structName+"*/");
                }
                untypeStr = untypeStr.replace("$(", "$" + member.type.name().toLowerCase() + "(");
                seg.appendReplacement(stringBuffer, Matcher.quoteReplacement(untypeStr));
            }
        }
        seg.appendTail(stringBuffer);
        return stringBuffer.toString();
    }
}
