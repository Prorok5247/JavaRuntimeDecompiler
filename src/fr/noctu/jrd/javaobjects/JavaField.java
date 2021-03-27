package fr.noctu.jrd.javaobjects;

import one.helfy.JVM;

public class JavaField {
    private JVM jvm;
    private JavaKlass owner;
    private long address;

    private int fieldNameIndex, fieldSignatureIndex, fieldFlags;

    public JavaField(JavaKlass owner, long address){
        this.jvm = new JVM();
        this.owner = owner;
        this.address = address;
        setupField();
    }

    private void setupField() {
        long fieldNameIndexConstant = jvm.intConstant("FieldInfo::name_index_offset");
        fieldNameIndex = jvm.getShort(address + fieldNameIndexConstant * 2);

        long fieldSignatureIndexConstant = jvm.intConstant("FieldInfo::signature_index_offset");
        fieldSignatureIndex = jvm.getShort(address + fieldSignatureIndexConstant * 2);

        long fieldFlagsConstant = jvm.intConstant("FieldInfo::access_flags_offset");
        fieldFlags = jvm.getShort(address + fieldFlagsConstant * 2);
    }

    public String getFieldName(){
        return owner.getConstantPool().getConstantPoolObject().getUTF8At(fieldNameIndex);
    }

    public String getFieldSignature(){
        return owner.getConstantPool().getConstantPoolObject().getUTF8At(fieldSignatureIndex);
    }

    public int getFieldFlags(){
        return fieldFlags;
    }
}
