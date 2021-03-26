package fr.noctu.jrd.utils;

import one.helfy.Field;
import one.helfy.JVM;
import one.helfy.Type;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;

public class JVMUtils {
    public static int getOopSize(){
        return new JVM().intConstant("oopSize");
    }

    public static long getDataOfArray(String arrayName){
        return new JVM().type(arrayName).offset("_data");
    }

    public static int getFieldSlot(){
        return new JVM().intConstant("FieldInfo::field_slots");
    }

    public static String getSymbol(JVM jvm, long symbolAddress) {
        Type symbolType = jvm.type("Symbol");
        long symbol = jvm.getAddress(symbolAddress);
        long body = symbol + symbolType.offset("_body");
        int length = jvm.getShort(symbol + symbolType.offset("_length")) & 0xffff;
        byte[] b = new byte[length];
        for (int i = 0; i < length; i++) {
            b[i] = jvm.getByte(body + i);
        }
        return new String(b, StandardCharsets.UTF_8);
    }

    public static Map<String, Type> getJvmTypes(JVM jvm){
        try {
            java.lang.reflect.Field field = jvm.getClass().getDeclaredField("types");
            field.setAccessible(true);
            return (Map<String, Type>) field.get(jvm);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Map<String, Number> getJvmConstants(JVM jvm){
        try {
            java.lang.reflect.Field field = jvm.getClass().getDeclaredField("constants");
            field.setAccessible(true);
            return (Map<String, Number>) field.get(jvm);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void listTypes(JVM jvm){
        Objects.requireNonNull(getJvmTypes(jvm)).forEach((s, type) -> {
            System.out.println(s + "  " + type.name);
        });
    }

    public static void listConstants(JVM jvm){
        Objects.requireNonNull(getJvmConstants(jvm)).forEach((s, constant) -> {
            System.out.println(s);
        });
    }

    public static void listFieldsOfType(Type type){
        for (Field field : type.fields) {
            System.out.println(field.name + "  " + field.typeName + (field.isStatic ? "  static" : ""));
        }
    }
}
