package fr.noctu.jrd.javaobjects;

import one.helfy.JVM;

public class JavaMethod {
    private JVM jvm;
    private JavaKlass owner;
    private long address;

    private ConstantPool constantPool;
    private int methodNameIndex;

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
    }

    public ConstantPool getConstantPool(){
        return constantPool;
    }

    public String getMethodName(){
        return ConstantPool.Symbol.asString(constantPool.getInfoAt(methodNameIndex));
    }
}
