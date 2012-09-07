package com.github.Icyene.Test;

import com.github.Icyene.LateBindAgent.Agent;
import com.github.Icyene.LateBindAgent.Util;

public class Test {

    public static void main(String[] args) {

	Util.attachAgentToJVM(new Class<?>[] {Agent.class, Util.class}, new Class<?>[] {Test.class}, Util.getPidFromRuntimeMBean()); 
		//Because we are creating the agent jar with all necessary files,
		//we need not worry about visibility when attaching to a class
		//that isn't using the system class loader

	sayWorld();
	for(int i = 0; i != 50; ++i) {
	sayWorld();
	}
	
    }

    public static void sayHello(int s) {
	System.out.println("Hello");
    }

    public static void sayWorld() {
	System.out.println("World!");
    }

}
