package fr.noctu.jrd.javaobjects;

import one.helfy.JVM;

public class JavaMethod {
    private JVM jvm;
    private JavaKlass owner;
    private long address;

    private ConstantPool constantPool;
    private int methodNameIndex, methodSignatureIndex, flags;

    public JavaMethod(JavaKlass owner, long address){
        this.jvm = new JVM();
        this.owner = owner;
        this.address = address;
        setupMethod();
    }

    private void setupMethod(){
        long constMethodOffset = jvm.type("Method").offset("_constMethod");
        address = jvm.getAddress(address + constMethodOffset);

        long constantPoolOffset = jvm.type("ConstMethod").offset("_constants");
        constantPool = new ConstantPool(jvm, jvm.getAddress(address + constantPoolOffset));

        long methodNameOffset = jvm.type("ConstMethod").offset("_name_index");
        methodNameIndex = jvm.getShort(address + methodNameOffset);

        long methodSignatureIndexOffset = jvm.type("ConstMethod").offset("_signature_index");
        methodSignatureIndex = jvm.getShort(address + methodSignatureIndexOffset);

        long flagsOffset = jvm.type("ConstMethod").offset("_flags");
        flags = jvm.getShort(address + flagsOffset);
    }

    public ConstantPool getConstantPool(){
        return constantPool;
    }

    public String getMethodName(){
        return ConstantPool.Symbol.asString(constantPool.getInfoAt(methodNameIndex));
    }

    public String getMethodSignature(){
        return ConstantPool.Symbol.asString(constantPool.getInfoAt(methodSignatureIndex));
    }

    public int getFlags(){
        return flags;
    }
}
