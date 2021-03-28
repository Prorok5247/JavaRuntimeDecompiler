package test;

import fr.noctu.jrd.JavaRuntimeDecompiler;
import fr.noctu.jrd.javaobjects.JavaKlass;
import fr.noctu.jrd.javaobjects.method.JavaMethod;
import fr.noctu.jrd.javaobjects.utils.JavaOpcode;
import sun.misc.Unsafe;

//This exemple remove the security of getUnsafe method
public class UnsafeGetUnlock {
    public static void main(String[] args) {
        JavaRuntimeDecompiler javaRuntimeDecompiler = new JavaRuntimeDecompiler();
        JavaKlass javaKlass = javaRuntimeDecompiler.decompileClass(Unsafe.class);
        JavaMethod getUnsafeMethod = javaKlass.getMethodByName("getUnsafe");
        System.out.println("Modifying method...");
        //178 19 0 176
        getUnsafeMethod.clearMethodInstructions(); //clear the method instructions
        getUnsafeMethod.setByteToNewInstruction(0, JavaOpcode.GETSTATIC); //set the first byte to getstatic instruction
        getUnsafeMethod.setByteToNewByte(1, (byte) 19); //set the first getstatic argument to 19 (theUnsafe constantpool index)
        getUnsafeMethod.setByteToNewByte(2, (byte) 0); //set the second getstatic argument to 0
        getUnsafeMethod.setByteToNewInstruction(3, JavaOpcode.ARETURN); //set the 3 byte to ARETURN instruction
        System.out.println("Modified method !");
        long yaAddress = Unsafe.getUnsafe().allocateMemory(100L);
        System.out.println("Allocated at: " + yaAddress);
    }
}
