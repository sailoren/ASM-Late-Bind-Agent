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
