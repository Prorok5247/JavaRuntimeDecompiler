package fr.noctu.jrd.javaobjects.utils;

public class JavaInstruction {
    private JavaOpcode opcode;
    private String arguments;
    private long address;

    public JavaInstruction(JavaOpcode opcode, String arguments, long address){
        this.opcode = opcode;
        this.arguments = arguments;
        this.address = address;
    }

    public JavaInstruction(JavaOpcode opcode, long address){
        this(opcode, "", address);
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

    public long getAddress(){
        return address;
    }

    public long getArgumentAddress(int argIndex){
        if(argIndex < 0 || argIndex > opcode.getArgsNumber())
            throw new RuntimeException("Invalid argument index !");
        return address + argIndex;
    }
}
