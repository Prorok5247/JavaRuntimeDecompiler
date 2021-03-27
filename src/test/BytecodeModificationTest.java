package test;

import fr.noctu.jrd.JavaRuntimeDecompiler;
import fr.noctu.jrd.javaobjects.JavaKlass;
import fr.noctu.jrd.javaobjects.JavaMethod;
import fr.noctu.jrd.javaobjects.utils.JavaOpcode;
import one.helfy.JVM;

import java.util.concurrent.TimeUnit;

public class BytecodeModificationTest {
    private static int add(int i, int ii){
        return i + 3;
    }

    public static void main(String[] args) {
        JavaRuntimeDecompiler javaRuntimeDecompiler = new JavaRuntimeDecompiler();
        JavaKlass javaKlass = javaRuntimeDecompiler.decompileClass(BytecodeModificationTest.class);
        JVM jvm = new JVM();

        for (JavaMethod method : javaKlass.getMethods()) {
            if(method.getMethodName().equals("add")){ // get method with name "add"
                for (byte codeByte : method.getCodeBytes()) {
                    System.out.print((codeByte & 0xFF) + " "); // print bytes
                }
                System.out.println();
                System.out.println(JVM.getUnsafe().getByte(method.getMethodInstructions().get(0).getAddress())); // print the first instruction of the method
                method.setFirstOccureOfInstruction(JavaOpcode.IADD, JavaOpcode.IMUL); // replacing the first occurence of IADD with IMUL (replace addition per multiplication)
            }
        }

        while (true){
            System.out.println(add(2, 4));
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
