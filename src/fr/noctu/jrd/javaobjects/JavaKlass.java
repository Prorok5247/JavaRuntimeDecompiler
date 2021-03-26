package fr.noctu.jrd.javaobjects;

import fr.noctu.jrd.utils.JVMUtils;
import one.helfy.JVM;

import java.util.ArrayList;

public class JavaKlass {
    private Class<?> baseClass;
    private JVM jvm = new JVM();

    private long javaKlassAddress;
    private ConstantPool constantPool;
    private int majorVersion, minorVersion;
    private int sourceFileNameIndex;

    private ArrayList<JavaMethod> methods;
    private ArrayList<JavaField> fields;

    public JavaKlass(Class<?> baseClass){
        this.baseClass = baseClass;
        setupClazz();
    }

    private void setupClazz(){
        long klassOffset = jvm.getInt(jvm.type("java_lang_Class").global("_klass_offset"));
        javaKlassAddress = JVMUtils.getOopSize() == 8 ? JVM.unsafe.getLong(baseClass, klassOffset) : JVM.unsafe.getInt(baseClass, klassOffset) & 0xffffffffL;

        long constantPoolOffset = jvm.type("InstanceKlass").offset("_constants");
        constantPool = new ConstantPool(jvm, jvm.getAddress(getJavaKlassAddress() + constantPoolOffset));

        long minorVersionAddress = jvm.type("InstanceKlass").offset("_minor_version");
        long majorVersionAddress = jvm.type("InstanceKlass").offset("_major_version");
        majorVersion = jvm.getShort(getJavaKlassAddress() + majorVersionAddress);
        minorVersion = jvm.getShort(getJavaKlassAddress() + minorVersionAddress);

        long sourceFileNameIndexOffset = jvm.type("InstanceKlass").offset("_source_file_name_index");
        sourceFileNameIndex = jvm.getShort(getJavaKlassAddress() + sourceFileNameIndexOffset);

        methods = new ArrayList<>();
        long methodsArrayAddress = jvm.getAddress(getJavaKlassAddress() + jvm.type("InstanceKlass").offset("_methods"));
        int methodCount = jvm.getInt(methodsArrayAddress);
        long methodsData = methodsArrayAddress + jvm.type("Array<Method*>").offset("_data");

        for(int i = 0; i<methodCount; i++){
            methods.add(new JavaMethod(this, jvm.getAddress(methodsData + i * JVMUtils.getOopSize())));
        }

        fields = new ArrayList<>();
        long fieldsArrayAddress = jvm.getAddress(getJavaKlassAddress() + jvm.type("InstanceKlass").offset("_fields"));
        long fieldsArrayDatas = JVMUtils.getDataOfArray("Array<u2>");
        int javaFieldsCount = jvm.getShort(getJavaKlassAddress() +  jvm.type("InstanceKlass").offset("_java_fields_count"));
        for(int i = 0; i<javaFieldsCount; i++){
            fields.add(new JavaField(this, fieldsArrayAddress + fieldsArrayDatas + i * JVMUtils.getFieldSlot() * 2));
        }
    }

    public Class<?> getBaseClass(){
        return baseClass;
    }

    public long getJavaKlassAddress(){
        return javaKlassAddress;
    }

    public ConstantPool getConstantPool() {
        return constantPool;
    }

    public int getMajorVersion() {
        return majorVersion;
    }

    public int getMinorVersion() {
        return minorVersion;
    }

    public String getSourceFileName(){
        return ConstantPool.Symbol.asString(constantPool.getInfoAt(sourceFileNameIndex));
    }

    public ArrayList<JavaMethod> getMethods(){
        return methods;
    }

    public ArrayList<JavaField> getFields(){
        return fields;
    }
}
