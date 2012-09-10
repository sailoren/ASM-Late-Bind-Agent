package com.github.Icyene.Test;

import com.github.Icyene.LateBindAgent.Agent;
import com.github.Icyene.LateBindAgent.Util;

public class Test {

    public static void main(String[] args) {

	try {
	    
	 Util.addToLibPath("C:\\Documents and Settings\\Tudor\\Desktop");
	 System.loadLibrary("attach");
	    
	 Util.attachAgentToJVM(new Class<?>[] { Agent.class, Util.class }, new
	 Class<?>[] { Test.class }, Util.getPidFromRuntimeMBean());
	} catch (Exception e) {
	    e.printStackTrace();
	}
	
	test();
	
    }
    
    public static void test() {
	
    }

}
