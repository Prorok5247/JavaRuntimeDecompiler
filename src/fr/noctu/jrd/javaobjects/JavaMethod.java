package fr.noctu.jrd.javaobjects;

import fr.noctu.jrd.javaobjects.utils.JavaOpcode;
import one.helfy.JVM;

import java.nio.ByteBuffer;
import java.util.ArrayList;

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

        parseMethodCode();
    }

    private int codeSize;
    private long codeStartAddress;
    private byte[] codeBytes;
    private ArrayList<JavaOpcode> opcodes;

    private void parseMethodCode(){
        codeSize = jvm.getShort(address + jvm.type("ConstMethod").offset("_code_size"));
        codeBytes = new byte[codeSize];
        codeStartAddress = address + jvm.type("ConstMethod").size;

        //Reading bytes
        for(int i = 0; i<codeSize; i++){
            codeBytes[i] = jvm.getByte(codeStartAddress + i);
        }

        //Interpreting
        opcodes = new ArrayList<>();
        int bytesToSkip = 0;
        for(int i = 0; i<codeSize; i++){
            byte codeByte = codeBytes[i];
            if(bytesToSkip == 0){
                for (JavaOpcode value : JavaOpcode.values()) {
                    if((codeByte & 0xFF) == value.getOpcodeValue()){
                        opcodes.add(value);
                        if(value.getArgsNumber() != 0) {
                            bytesToSkip += value.getArgsNumber();
                            //Argumetns start
                            /*switch (value){
                                case GETSTATIC:
                                    byte firstByte = codeBytes[i+1];
                                    byte secByte = codeBytes[i+2];
                                    short argByte = ByteBuffer.wrap(new byte[]{firstByte, secByte}).getShort();

                                    break;
                            }*/
                        }
                    }
                }
            }else{
                bytesToSkip--;
            }
        }
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

    public int getMethodFlags(){
        return flags;
    }

    public int getCodeSize(){
        return codeSize;
    }

    public long getCodeStartAddress(){
        return codeStartAddress;
    }

    public byte[] getCodeBytes(){
        return codeBytes;
    }

    public ArrayList<JavaOpcode> getMethodOpcodes(){
        return opcodes;
    }
}
