package fr.noctu.jrd;

import fr.noctu.jrd.javaobjects.JavaField;
import fr.noctu.jrd.javaobjects.JavaKlass;
import fr.noctu.jrd.javaobjects.JavaMethod;
import fr.noctu.jrd.javaobjects.utils.JavaOpcode;

public class JavaRuntimeDecompiler {
    public int diplomatic = 2;

    public void test1(int rien){
        System.out.println("oui");
        double oof = 4.2D * 2D;
        double issou = oof * diplomatic;
    }

    private void test2(){
        System.out.println("bruh");
    }

    public JavaKlass decompileClass(Class<?> clazz){
        return new JavaKlass(clazz);
    }

    // TEST //
    public static void main(String[] args) {
        JavaRuntimeDecompiler javaRuntimeDecompiler = new JavaRuntimeDecompiler();
        JavaKlass javaKlass = javaRuntimeDecompiler.decompileClass(JavaRuntimeDecompiler.class);
        System.out.println(javaKlass.getMajorVersion() + "." + javaKlass.getMinorVersion());
        System.out.println(javaKlass.getSourceFileName());
        for (JavaMethod method : javaKlass.getMethods()) {
            System.out.println(method.getMethodName() + "  " + method.getMethodSignature());
            for (byte codeByte : method.getCodeBytes()) {
                System.out.print((codeByte & 0xFF) + " ");
            }
            System.out.println();
            for (JavaOpcode methodOpcode : method.getMethodOpcodes()) {
                System.out.println("  * " + methodOpcode);
            }
        }
        for (JavaField field : javaKlass.getFields()) {
            System.out.println(field.getFieldName() + "  " + field.getFieldSignature());
        }
    }
}
