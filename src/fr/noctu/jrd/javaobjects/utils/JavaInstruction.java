package fr.noctu.jrd.javaobjects.utils;

public class JavaInstruction {
    private JavaOpcode opcode;
    private String arguments;

    public JavaInstruction(JavaOpcode opcode, String arguments){
        this.opcode = opcode;
        this.arguments = arguments;
    }

    public JavaInstruction(JavaOpcode opcode){
        this(opcode, "");
    }

    public JavaOpcode getOpcode() {
        return opcode;
    }

    public String getArguments() {
        return arguments;
    }

    public void appendArg(String text){
        arguments += text;
    }
}
