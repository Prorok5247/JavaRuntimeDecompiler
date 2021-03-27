package test;

import fr.noctu.jrd.JavaRuntimeDecompiler;
import fr.noctu.jrd.javaobjects.JavaKlass;
import fr.noctu.jrd.javaobjects.JavaMethod;
import fr.noctu.jrd.javaobjects.utils.JavaOpcode;
import one.helfy.JVM;

import java.util.concurrent.TimeUnit;

public class BytecodeModificationTest {
    private static int add(int i){
        return i + 3;
    }

    public static void main(String[] args) {
        JavaRuntimeDecompiler javaRuntimeDecompiler = new JavaRuntimeDecompiler();
        JavaKlass javaKlass = javaRuntimeDecompiler.decompileClass(BytecodeModificationTest.class);
        JVM jvm = new JVM();

        int counter = 0;
        while (true){
            counter++;

            if(counter == 4){
                for (JavaMethod method : javaKlass.getMethods()) {
                    if(method.getMethodName().equals("add")){ // get method with name "add"
                        method.clearMethodInstructions(); // clear all instructions of the method
                        method.setInstruction(0, JavaOpcode.ICONST_2); // add iconst2 instruction
                        method.setInstruction(1, JavaOpcode.IRETURN); // add IRETURN instruction
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
