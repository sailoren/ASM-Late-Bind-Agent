package com.github.Icyene.LateBindAgent;

import java.lang.instrument.ClassDefinition;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;

import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

import com.github.Icyene.Test.Test;

/**
 * Primary class used by tests to allow for method profiling information. Client
 * code will interact with this method only. * This class is NOT Thread safe.
 * But can be made so if required. This code is licensed under the WTFPL. Please
 * credit if used; it took me 4 hours to develop.
 * 
 * @author Icyene
 */
public class Agent implements ClassFileTransformer {

    private static Instrumentation instrumentation = null;
    private static Agent transformer;

    public static void agentmain(String s, Instrumentation i) {

	System.out.println("Agent loaded!");

	// initialization code:
	transformer = new Agent();
	instrumentation = i;
	instrumentation.addTransformer(transformer);
	// to instrument, first revert all added bytecode:
	// call retransformClasses() on all modifiable classes...
	try {
	    instrumentation.redefineClasses(new ClassDefinition(Test.class,
		    Util.getBytesFromClass(Test.class)));
	} catch (Exception e) {
	    e.printStackTrace();
	    System.out.println("Failed to redefine class!");
	}

    };

    /**
     * Kills this agent
     */

    public static void killAgent() {
	instrumentation.removeTransformer(transformer);
    }

    @Override
    public byte[] transform(ClassLoader loader, String className,
	    Class<?> classBeingRedefined, ProtectionDomain protectionDomain,
	    byte[] classfileBuffer) throws IllegalClassFormatException {

	System.out.println("Instrumenting class: " + className);

	// We can only profile classes that we can see. If a class uses a custom
	// classloader we will nto be able to see it and crash if we try to
	// profile it.
	if (loader != ClassLoader.getSystemClassLoader()) {
	    System.err
		    .println(className
			    + " is not using the system loader, and so cannot be loaded!");
	    return classfileBuffer;
	}

	// Don't profile yourself, otherwise you'll stackoverflow.
	if (className.startsWith("com/github/Icyene/LateBindAgent")) {
	    System.err
		    .println(className
			    + " is part of profiling classes. No StackOverflow for you!");
	    return classfileBuffer;
	}

	byte[] result = classfileBuffer;
	try {
	    //Create class reader from buffer
	    ClassReader reader = new ClassReader(classfileBuffer);
	    //Make writer
	    ClassWriter writer = new ClassWriter(true);	    
	    ClassAdapter profiler = new ProfileClassAdapter(writer, className);	    
	    //Add the class adapter as a modifier
	    reader.accept(profiler, true);
	    result = writer.toByteArray();
	    System.out.println("Returning reinstrumented class: " + className);
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return result;
    }
}