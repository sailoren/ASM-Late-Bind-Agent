package com.github.Icyene.Test;

import com.github.Icyene.LateBindAgent.Agent;
import com.github.Icyene.LateBindAgent.Util;

public class Test {

    public static void main(String[] args) {

	try {
	    
	 Util.addToLibPath("C:\\Documents and Settings\\Tudor\\Desktop");
	 System.loadLibrary("attach");
	    
	 Util.attachAgentToJVM(Agent.class, new Class<?>[] { Util.class }, Util.getPID());
	} catch (Exception e) {
	    e.printStackTrace();
	}
	
	test();
	
    }
    
    public static void test() {
	
    }

}
