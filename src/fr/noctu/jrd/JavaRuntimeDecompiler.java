package fr.noctu.jrd;

import fr.noctu.jrd.javaobjects.JavaField;
import fr.noctu.jrd.javaobjects.JavaKlass;
import fr.noctu.jrd.javaobjects.JavaMethod;
import fr.noctu.jrd.javaobjects.utils.JavaInstruction;

public class JavaRuntimeDecompiler {
    public int diplomatic = 2;

    public float test1(int rien){
        Object[] bruhArray = new Object[1];
        double oof = 4.2D * 2D;
        float issou = 21F * diplomatic;
        Object ya = new Object();
        if(ya instanceof Thread){
            System.out.println("oof");
        }
        test2();
        return 2F;
    }

    private static void test2(){
        String oui = "oof";
        System.err.println(oui);
    }

    public JavaKlass decompileClass(Class<?> clazz){
        return new JavaKlass(clazz);
    }

    // TEST //
    public static void main(String[] args) {
        JavaRuntimeDecompiler javaRuntimeDecompiler = new JavaRuntimeDecompiler();
        JavaKlass javaKlass = javaRuntimeDecompiler.decompileClass(JavaKlass.class);
        System.out.println(javaKlass.getMajorVersion() + "." + javaKlass.getMinorVersion());
        System.out.println(javaKlass.getClassName());
        for (JavaMethod method : javaKlass.getMethods()) {
            System.out.println(method.getMethodName() + "  " + method.getMethodSignature());
            for (byte codeByte : method.getCodeBytes()) {
                System.out.print((codeByte & 0xFF) + " ");
            }
            System.out.println();
            for (JavaInstruction instruction : method.getMethodInstructions()) {
                System.out.println("  * " + instruction.getOpcode() + " " + instruction.getArguments());
            }
        }
        for (JavaField field : javaKlass.getFields()) {
            System.out.println(field.getFieldName() + "  " + field.getFieldSignature());
        }
    }
}
