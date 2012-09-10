package com.github.Icyene.Test;

import com.github.Icyene.AgentLoader.AgentLoader;

public class Test {

    public static void main(String[] args) {

//	try {
//
//	    AgentLoader agent = new AgentLoader();
//
//	    agent.addToLibPath("C:\\Documents and Settings\\Tudor\\Desktop");
//	    System.loadLibrary("attach");
//
//	    agent.attachAgentToJVM(Agent.class,
//		    new Class<?>[] { AgentLoader.class }, agent.getCurrentPID());
//	} catch (Exception e) {
//	    e.printStackTrace();
//	}

	test();

    }

    public static void test() {


	String arch = System.getProperty("os.arch");	  
	  if(arch.contains("86")) {
	      arch = "32";
	  } else {
	      arch = "64";
	  }
	  
	 String os = System.getProperty("os.name").toLowerCase();
	  String ext = "";
	  if(os.contains("windows")) {
	      os = "windows";
	      ext = ".dll";
	  } else if(os.contains("linux")) {
	      os = "linux";
	      ext = ".so";
	  } else if(os.contains("solar")) {
	      os = "solaris";
	      ext = ".dll";
	  } else if(os.contains("solar")) {
	      os = "mac";
	      ext = ".so";
	  }
	  
	 
	String jV = System.getProperty("java.version");
	
	if(jV.startsWith("1.7")) {
	    jV = "java7";
	  } else if(os.startsWith("1.6")) {
	      jV = "java6";
	  } else if(os.startsWith("1.5")) {
	      jV = "java5";
	  }
	
	String file = jV + "_" + os + "_" + arch + ext;
	System.out.println(file);
	
    }

}
