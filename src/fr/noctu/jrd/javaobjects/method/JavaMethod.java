package fr.noctu.jrd.javaobjects.method;

import fr.noctu.jrd.javaobjects.JavaConstantPool;
import fr.noctu.jrd.javaobjects.JavaKlass;
import fr.noctu.jrd.javaobjects.utils.JavaInstruction;
import fr.noctu.jrd.javaobjects.utils.JavaOpcode;
import fr.noctu.jrd.javaobjects.utils.accessflags.AccessFlagsUtils;
import fr.noctu.jrd.javaobjects.utils.accessflags.MethodAccessFlags;
import fr.noctu.jrd.utils.JVMUtils;
import one.helfy.JVM;

import java.util.ArrayList;

public class JavaMethod {
    private JVM jvm;
    private JavaKlass owner;
    private long address;

    private JavaMethodCounters javaMethodCounters;

    private JavaConstantPool javaConstantPool;
    private int methodNameIndex, methodSignatureIndex, maxStack, maxLocals;
    private long methodFlagsAddress;
    private byte[] originalCode;
    private ArrayList<MethodAccessFlags> methodFlags;

    public JavaMethod(JavaKlass owner, long address){
        this.jvm = new JVM();
        this.owner = owner;
        this.address = address;
        setupMethod();
    }

    private void setupMethod(){
        long constMethodOffset = jvm.type("Method").offset("_constMethod");
        javaMethodCounters = new JavaMethodCounters(this, address + jvm.type("Method").offset("_method_counters"));
        address = jvm.getAddress(address + constMethodOffset);

        long constantPoolOffset = jvm.type("ConstMethod").offset("_constants");
        javaConstantPool = new JavaConstantPool(jvm, owner, jvm.getAddress(address + constantPoolOffset));

        long methodNameOffset = jvm.type("ConstMethod").offset("_name_index");
        methodNameIndex = jvm.getShort(address + methodNameOffset);

        long methodSignatureIndexOffset = jvm.type("ConstMethod").offset("_signature_index");
        methodSignatureIndex = jvm.getShort(address + methodSignatureIndexOffset);

        long flagsOffset = jvm.type("ConstMethod").offset("_flags");
        methodFlagsAddress = address + flagsOffset;
        short methodFlagsShort = jvm.getShort(methodFlagsAddress);
        methodFlags = AccessFlagsUtils.getMethodAccessFlags(methodFlagsShort);

        long maxStackOffset = jvm.type("ConstMethod").offset("_max_stack");
        maxStack = jvm.getShort(address + maxStackOffset);

        long maxLocalsOffset = jvm.type("ConstMethod").offset("_max_locals");
        maxLocals = jvm.getShort(address + maxLocalsOffset);

        parseMethodCode();
    }

    private int codeSize;
    private long codeStartAddress;
    private byte[] codeBytes;
    private ArrayList<JavaInstruction> javaInstructions;

    private void parseMethodCode(){
        codeSize = jvm.getShort(address + jvm.type("ConstMethod").offset("_code_size"));
        codeBytes = new byte[codeSize];
        codeStartAddress = address + jvm.type("ConstMethod").size;

        //Reading bytes
        for(int i = 0; i<codeSize; i++){
            codeBytes[i] = jvm.getByte(codeStartAddress + i);
        }
        this.originalCode = codeBytes;

        //Interpreting
        javaInstructions = new ArrayList<>();
        int bytesToSkip = 0;
        for(int i = 0; i<codeSize; i++){
            byte codeByte = codeBytes[i];
            if(bytesToSkip == 0){
                for (JavaOpcode value : JavaOpcode.values()) {
                    if((codeByte & 0xFF) == value.getOpcodeValue()){
                        JavaInstruction javaInstruction = new JavaInstruction(value, codeStartAddress + i);
                        if(value.getArgsNumber() != 0) {
                            bytesToSkip += value.getArgsNumber();
                            switch (value){
                                case ASTORE:
                                case FSTORE:
                                case ISTORE:
                                case LSTORE:
                                case DSTORE:
                                case ALOAD:
                                case FLOAD:
                                case ILOAD:
                                case LLOAD:
                                case DLOAD:
                                case BIPUSH:
                                    javaInstruction.appendArg(Byte.toString(codeBytes[i + 1]));
                                    break;
                                case IINC:
                                    javaInstruction.appendArg(codeBytes[i + 1] + " " + codeBytes[i + 2]);
                                    break;
                                case SIPUSH:
                                    short intermediateShort = JVMUtils.getShortFromTwoBytes(codeBytes[i + 1], codeBytes[i + 2]);
                                    javaInstruction.appendArg(String.valueOf(JVMUtils.signExtendIntFromShort(intermediateShort)));
                                    break;
                                case IF_ICMPEQ:
                                case IF_ICMPNE:
                                case IF_ICMPIT:
                                case IF_ICMPGE:
                                case IF_ICMPGT:
                                case IF_ICMPIE:
                                case IF_ACMPEQ:
                                case IF_ACMPNE:
                                case IFEQ:
                                case IFGE:
                                case IFGT:
                                case IFIE:
                                case IFIT:
                                case IFNE:
                                case IFNONNULL:
                                case IFNULL:
                                case GOTO:
                                    short intermediateShort1 = JVMUtils.getShortFromTwoBytes(codeBytes[i + 1], codeBytes[i + 2]); //this is the label destination
                                    javaInstruction.appendArg("L" + JVMUtils.signExtendIntFromShort(intermediateShort1));
                                    break;
                                case GOTO_W:
                                    int destInt = JVMUtils.getIntFromFourBytes(codeBytes[i + 1], codeBytes[i + 2], codeBytes[i + 3], codeBytes[i + 4]); //this is the label destination
                                    javaInstruction.appendArg("L" + destInt);
                                    break;
                                case LDC:
                                    byte indexByte = codeBytes[i + 1];
                                    int tag = javaConstantPool.tagAt(indexByte);
                                    switch (tag){
                                        case 7:
                                            javaInstruction.appendArg(javaConstantPool.getConstantPoolObject().getClassAt(indexByte).getName().replaceAll("\\.", "/"));
                                            break;
                                        case 3:
                                            javaInstruction.appendArg(String.valueOf(javaConstantPool.getConstantPoolObject().getIntAt(indexByte)));
                                            break;
                                        case 4:
                                            javaInstruction.appendArg(String.valueOf(javaConstantPool.getConstantPoolObject().getFloatAt(indexByte)));
                                            break;
                                        case 8:
                                            javaInstruction.appendArg(javaConstantPool.getConstantPoolObject().getStringAt(indexByte));
                                            break;
                                    }
                                    break;
                                case LDC_W:
                                    short cpShortIndex = JVMUtils.getShortFromTwoBytes(codeBytes[i + 1], codeBytes[i + 2]);
                                    int cpTag = javaConstantPool.tagAt(cpShortIndex);
                                    switch (cpTag){
                                        case 7:
                                            javaInstruction.appendArg(javaConstantPool.getConstantPoolObject().getClassAt(cpShortIndex).getName().replaceAll("\\.", "/"));
                                            break;
                                        case 3:
                                            javaInstruction.appendArg(String.valueOf(javaConstantPool.getConstantPoolObject().getIntAt(cpShortIndex)));
                                            break;
                                        case 4:
                                            javaInstruction.appendArg(String.valueOf(javaConstantPool.getConstantPoolObject().getFloatAt(cpShortIndex)));
                                            break;
                                        case 8:
                                            javaInstruction.appendArg(javaConstantPool.getConstantPoolObject().getStringAt(cpShortIndex));
                                            break;
                                    }
                                    break;
                                case LDC2_W:
                                    short cpShortIndex1 = JVMUtils.getShortFromTwoBytes(codeBytes[i + 1], codeBytes[i + 2]);
                                    int cpTag1 = javaConstantPool.tagAt(cpShortIndex1);
                                    switch (cpTag1){
                                        case 5:
                                            javaInstruction.appendArg(String.valueOf(javaConstantPool.getConstantPoolObject().getLongAt(cpShortIndex1)));
                                            break;
                                        case 6:
                                            javaInstruction.appendArg(String.valueOf(javaConstantPool.getConstantPoolObject().getDoubleAt(cpShortIndex1)));
                                            break;
                                    }
                                    break;
                                case INSTANCEOF:
                                case NEW:
                                case ANEWARRAY:
                                case CHECKCAST:
                                    short index = JVMUtils.getShortFromTwoBytes(codeBytes[i + 1], codeBytes[i + 2]);
                                    javaInstruction.appendArg(javaConstantPool.getConstantPoolObject().getClassAt(index).getName().replaceAll("\\.", "/"));
                                    break;
                                case NEWARRAY:
                                    int arrayType = codeBytes[i + 1];
                                    switch (arrayType){
                                        case 4:
                                            javaInstruction.appendArg("T_BOOLEAN");
                                            break;
                                        case 5:
                                            javaInstruction.appendArg("T_CHAR");
                                            break;
                                        case 6:
                                            javaInstruction.appendArg("T_FLOAT");
                                            break;
                                        case 7:
                                            javaInstruction.appendArg("T_DOUBLE");
                                            break;
                                        case 8:
                                            javaInstruction.appendArg("T_BYTE");
                                            break;
                                        case 9:
                                            javaInstruction.appendArg("T_SHORT");
                                            break;
                                        case 10:
                                            javaInstruction.appendArg("T_INT");
                                            break;
                                        case 11:
                                            javaInstruction.appendArg("T_LONG");
                                            break;
                                    }
                                    break;
                                case RET:
                                    javaInstruction.appendArg(String.valueOf(JVMUtils.byteToUnsignedByte(codeBytes[i + 1])));
                                    break;
                            }
                        }
                        javaInstructions.add(javaInstruction);
                    }
                }
            }else{
                bytesToSkip--;
            }
        }
    }

    //This replace a byte of instructions list
    public void setByteToNewInstruction(int byteIndex, JavaOpcode opcode){
        JVM.getUnsafe().putByte(codeStartAddress + byteIndex, (byte) opcode.getOpcodeValue());
    }

    //This replace an instruction
    public void replaceInstruction(int instructionIndex, JavaOpcode opcode){
        JVM.getUnsafe().putByte(javaInstructions.get(instructionIndex).getAddress(), (byte) opcode.getOpcodeValue());
    }

    //This replace the first occurence of an instruction
    public void setFirstOccureOfInstruction(JavaOpcode baseOpcode, JavaOpcode newOpcode){
        for (JavaInstruction javaInstruction : javaInstructions) {
            if(javaInstruction.getOpcode() == baseOpcode) {
                replaceInstruction(javaInstructions.indexOf(javaInstruction), newOpcode);
                return;
            }
        }
    }

    //This replace all occurences of an instruction
    public void setAllInstructionsOfAType(JavaOpcode baseOpcode, JavaOpcode newOpcode){
        for (JavaInstruction javaInstruction : javaInstructions) {
            if(javaInstruction.getOpcode() == baseOpcode)
                replaceInstruction(javaInstructions.indexOf(javaInstruction), newOpcode);
        }
    }

    //This replace the argument of an instruction
    public void setInstructionArgument(JavaInstruction javaInstruction, int argumentIndex, byte value){
        JVM.getUnsafe().putByte(javaInstruction.getArgumentAddress(argumentIndex), value);
    }

    //This clear all instructions of a method
    public void clearMethodInstructions(){
        for(int i = 0; i<codeSize; i++){
            JVM.getUnsafe().putByte(codeStartAddress + i, (byte) JavaOpcode.NOP.getOpcodeValue());
        }
    }

    //This replace the current method by a new
    public void copyMethod(JavaMethod javaMethod){
        if(codeSize >= javaMethod.getCodeSize()){
            clearMethodInstructions();
            for (int i = 0; i<javaMethod.codeSize; i++){
                JVM.getUnsafe().putByte(codeStartAddress + i, javaMethod.codeBytes[i]);
            }
        }else
            throw new RuntimeException("CodeSize is superior: original method codesize=" + codeSize + "  toCopyMethod codesize=" + javaMethod.getCodeSize());
    }

    //This restore the original method code
    public void restoreOriginalCode(){
        clearMethodInstructions();
        for(int i = 0; i<codeSize; i++){
            JVM.getUnsafe().putByte(codeStartAddress + i, originalCode[i]);
        }
    }

    public void reloadMethod(){
        setupMethod();
    }

    public JavaMethodCounters getJavaMethodCounters(){
        return javaMethodCounters;
    }

    public JavaConstantPool getConstantPool(){
        return javaConstantPool;
    }

    public String getMethodName(){
        return javaConstantPool.getConstantPoolObject().getUTF8At(methodNameIndex);
    }

    public String getMethodSignature(){
        return javaConstantPool.getConstantPoolObject().getUTF8At(methodSignatureIndex);
    }

    public ArrayList<MethodAccessFlags> getMethodFlags(){
        return methodFlags;
    }

    public void setMethodFlags(MethodAccessFlags... accessFlags){
        jvm.putShort(methodFlagsAddress, AccessFlagsUtils.buildMethoddAccessFlags(accessFlags));
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

    public int getMaxStack() {
        return maxStack;
    }

    public int getMaxLocals() {
        return maxLocals;
    }

    public ArrayList<JavaInstruction> getMethodInstructions(){
        return javaInstructions;
    }
}
