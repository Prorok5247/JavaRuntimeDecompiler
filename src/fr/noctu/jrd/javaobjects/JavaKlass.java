package fr.noctu.jrd.javaobjects;

public class JavaKlass {
    private Class<?> baseClass;

    public JavaKlass(Class<?> baseClass){
        this.baseClass = baseClass;
    }

    public Class<?> getBaseClass(){
        return baseClass;
    }
}
