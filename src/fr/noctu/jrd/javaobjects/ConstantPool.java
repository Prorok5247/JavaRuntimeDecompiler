package fr.noctu.jrd.javaobjects;

import fr.noctu.jrd.utils.JVMUtils;
import one.helfy.JVM;

import java.nio.charset.StandardCharsets;

public class ConstantPool {
    public static JVM jvm;

    private final long address;
    private final long headerSize;

    public ConstantPool(JVM jvm, long address){
        this.jvm = jvm;
        this.address = address;
        this.headerSize = jvm.type("ConstantPool").size;
    }

    public int getLength(){
        long lengthOffset = jvm.type("ConstantPool").offset("_length");
        return jvm.getInt(address + lengthOffset);
    }

    public long getInfoAt(int index){
        return jvm.getAddress(address + headerSize + index * JVMUtils.getOopSize());
    }

    public long getAddress() {
        return address;
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
