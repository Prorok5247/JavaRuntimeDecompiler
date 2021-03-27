package fr.noctu.jrd.javaobjects.method;

import one.helfy.JVM;

public class JavaMethodCounters {
    private JVM jvm;
    private JavaMethod owner;
    private long address;

    private JavaInvocationCounter invocationCounter;

    public JavaMethodCounters(JavaMethod owner, long address){
        this.jvm = new JVM();
        this.owner = owner;
        this.address = address;
        setupMethod();
    }

    private void setupMethod() {
        long invocationCounterAddress = address + jvm.type("MethodCounters").offset("_invocation_counter");
        invocationCounter = new JavaInvocationCounter(invocationCounterAddress);
    }

    public JavaInvocationCounter getInvocationCounter(){
        return invocationCounter;
    }
}
