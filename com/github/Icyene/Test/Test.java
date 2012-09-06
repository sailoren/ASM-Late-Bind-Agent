package com.github.Icyene.Test;

import com.github.Icyene.LateBindAgent.Agent;
import com.github.Icyene.LateBindAgent.Profile;
import com.github.Icyene.LateBindAgent.ProfileClassAdapter;
import com.github.Icyene.LateBindAgent.ProfileMethodAdapter;
import com.github.Icyene.LateBindAgent.Util;

public class Test {

    public static void main(String[] args) {

	Util.attachAgentToJVM(new Class<?>[] { Agent.class, Util.class,
		Profile.class, ProfileClassAdapter.class,
		ProfileMethodAdapter.class }, Util.getPidFromRuntimeMBean()); 
		//Because we are creating the agent jar with all necessary files,
		//we need not worry about visibility when attaching to a class
		//that isn't using the system class loader

	sayHello(5);
	sayWorld();
	
    }

    public static void sayHello(int s) {
	System.out.println("Hello");
    }

    public static void sayWorld() {
	System.out.println("World!");
    }

}
