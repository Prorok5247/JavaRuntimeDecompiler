package fr.noctu.jrd.javaobjects;

import fr.noctu.jrd.utils.JVMUtils;
import one.helfy.JVM;
import sun.reflect.ConstantPool;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class JavaConstantPool {
    public static JVM jvm;

    private final ConstantPool constantPoolObject;
    private final long address;
    private final long headerSize;

    public JavaConstantPool(JVM jvm, JavaKlass ownerClass, long address){
        this.jvm = jvm;
        this.address = address;
        this.headerSize = jvm.type("ConstantPool").size;
        this.constantPoolObject = JVMUtils.getConstantPoolOfClass(ownerClass.getBaseClass());
    }

    public int tagAt(int index){
        long tagsDataAddress = getTagsDataAddress();
        for(int i = 0; i<getLength(); i++){
            if(i == index)
             return jvm.getByte(tagsDataAddress + i);
        }
        return -1;
    }

     public int getLength(){
        long lengthOffset = jvm.type("ConstantPool").offset("_length");
        return jvm.getInt(address + lengthOffset);
    }

    public long getInfoAt(int index){
        return jvm.getAddress(address + headerSize + index * JVMUtils.getOopSize());
    }

    public ConstantPool getConstantPoolObject(){
        return constantPoolObject;
    }

    public long getAddress() {
        return address;
    }

    private long getTagsDataAddress(){
        long tagsArrayAddress = jvm.getAddress(address + jvm.type("ConstantPool").offset("_tags"));
        long datasOffset = JVMUtils.getDataOfArray("Array<u1>");
        return tagsArrayAddress + datasOffset;
    }

    static class Symbol {
        static final long _length = jvm.type("Symbol").offset("_length");
        static final long _body = jvm.type("Symbol").offset("_body");

        static String asString(long symbol) {
            int length = jvm.getShort(symbol + _length) & 0xffff;
            byte[] data = new byte[length];
            for (int i = 0; i < data.length; i++) {
                data[i] = jvm.getByte(symbol + _body + i);
            }
            return new String(data, StandardCharsets.UTF_8);
        }
    }
}
