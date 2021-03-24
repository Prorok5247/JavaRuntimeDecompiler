package fr.noctu.jrd;

import fr.noctu.jrd.javaobjects.JavaKlass;

public class JavaRuntimeDecompiler {
    public JavaKlass decompileClass(Class<?> clazz){
        return new JavaKlass(clazz);
    }
}
