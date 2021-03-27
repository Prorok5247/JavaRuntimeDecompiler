package fr.noctu.jrd.javaobjects.method;

import one.helfy.JVM;

public class JavaInvocationCounter {
    private JVM jvm;
    private long address;

    private long counterAddress;

    public JavaInvocationCounter(long address){
        this.jvm = new JVM();
        this.address = address;
        setupInvocationCounter();
    }

    private void setupInvocationCounter(){
        this.counterAddress = address + jvm.type("InvocationCounter").offset("_counter");
    }

    public int getCounter(){
        return jvm.getInt(counterAddress);
    }
}
