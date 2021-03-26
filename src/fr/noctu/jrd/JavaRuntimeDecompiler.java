package fr.noctu.jrd;

import fr.noctu.jrd.javaobjects.JavaKlass;
import fr.noctu.jrd.javaobjects.JavaMethod;

public class JavaRuntimeDecompiler {
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
        }
    }
}
