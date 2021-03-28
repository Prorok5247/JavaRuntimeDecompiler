package test;

import fr.noctu.jrd.JavaRuntimeDecompiler;
import fr.noctu.jrd.javaobjects.JavaKlass;
import fr.noctu.jrd.javaobjects.method.JavaMethod;
import fr.noctu.jrd.javaobjects.utils.JavaOpcode;

import java.util.concurrent.TimeUnit;

public class BytecodeModificationTest {
    private static int add(int i){
        System.out.println("called");
        return i + 3;
    }

    public static void main(String[] args) {
        JavaRuntimeDecompiler javaRuntimeDecompiler = new JavaRuntimeDecompiler();
        JavaKlass javaKlass = javaRuntimeDecompiler.decompileClass(BytecodeModificationTest.class);

        int counter = 0;
        while (true){
            counter++;

            for (JavaMethod method : javaKlass.getMethods()) {
                if(method.getMethodName().equals("add")){ // get method with name "add"
                    if(counter == 4){
                        method.clearMethodInstructions(); // clear all instructions of the method
                        method.setByteToNewInstruction(0, JavaOpcode.ICONST_M1); // add iconst2 instruction
                        method.setByteToNewInstruction(1, JavaOpcode.IRETURN); // add IRETURN instruction
                    } else if(counter == 10){
                        method.restoreOriginalCode();
                    }
                }
            }

            System.out.println(add(2));
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
