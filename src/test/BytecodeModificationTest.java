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
            if(method.getMethodName().equals("add")){
                for (byte codeByte : method.getCodeBytes()) {
                    System.out.print((codeByte & 0xFF) + " ");
                }
                System.out.println();
                System.out.println(JVM.getUnsafe().getByte(method.getMethodInstructions().get(0).getAddress()));
                method.setFirstOccureOfInstruction(JavaOpcode.IADD, JavaOpcode.IMUL);
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
