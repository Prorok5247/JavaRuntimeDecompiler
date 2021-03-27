package test;

import fr.noctu.jrd.JavaRuntimeDecompiler;
import fr.noctu.jrd.javaobjects.JavaField;
import fr.noctu.jrd.javaobjects.JavaKlass;
import fr.noctu.jrd.javaobjects.JavaMethod;
import fr.noctu.jrd.javaobjects.utils.JavaInstruction;

public class JrdTest {
    public static void main(String[] args) {
        JavaRuntimeDecompiler javaRuntimeDecompiler = new JavaRuntimeDecompiler(); //Instancing a new javaruntime decomp
        JavaKlass javaKlass = javaRuntimeDecompiler.decompileClass(JavaKlass.class); //decompile the class JavaKlass
        System.out.println(javaKlass.getMajorVersion() + "." + javaKlass.getMinorVersion()); // printing the version of the class
        System.out.println(javaKlass.getClassName()); // printing the class name

        for (JavaMethod method : javaKlass.getMethods()) { // for on the methods
            System.out.println(method.getMethodName() + "  " + method.getMethodSignature()); // printing the name and signature of the method
            for (byte codeByte : method.getCodeBytes()) { // for on the method code bytes
                System.out.print((codeByte & 0xFF) + " "); // print each byte of the method code
            }
            System.out.println();
            for (JavaInstruction instruction : method.getMethodInstructions()) { // for on the method instructions
                System.out.println("  * " + instruction.getOpcode() + " " + instruction.getArguments()); // print the instruction name and arguments
            }
        }
        for (JavaField field : javaKlass.getFields()) { // for on the fields
            System.out.println(field.getFieldName() + "  " + field.getFieldSignature()); // print the field name and signature
        }
    }
}
