package com.github.Icyene.LateBindAgent;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.management.ManagementFactory;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;

import com.sun.tools.attach.VirtualMachine;

public class Util {

    /**
     * Gets the current JVM PID
     * @return
     * Returns the PID
     * @throws Exception
     */
    
    public static String getPidFromRuntimeMBean() {
	String jvm = ManagementFactory.getRuntimeMXBean().getName();
	String pid = jvm.substring(0, jvm.indexOf('@'));
	return pid;
    }

    /**
     * Attaches given agent classes to JVM
     * 
     * @param agentClasses
     * A Class<?>[] of classes to be included in agent
     * @param JVMPid
     * The PID of the JVM to attach to
     */
    
    public static void attachAgentToJVM(Class<?>[] agentClasses, String JVMPid) {

	try {
	

	final File jarFile = File.createTempFile("agent", ".jar");
	jarFile.deleteOnExit();

	final Manifest manifest = new Manifest();
	final Attributes mainAttributes = manifest.getMainAttributes();
	mainAttributes.put(Attributes.Name.MANIFEST_VERSION, "1.0");
	mainAttributes.put(new Attributes.Name("Agent-Class"),
		Agent.class.getName());
	mainAttributes.put(new Attributes.Name("Can-Retransform-Classes"),
		"true");
	mainAttributes.put(new Attributes.Name("Can-Redefine-Classes"), "true");

	final JarOutputStream jos = new JarOutputStream(new FileOutputStream(
		jarFile), manifest);
	
	
	for(Class<?> clazz: agentClasses) {  	    
		final JarEntry agent = new JarEntry(clazz.getName().replace('.',
			'/')
			+ ".class");
		jos.putNextEntry(agent);
	    
	jos.write(getBytesFromIS(clazz.getClassLoader()
		.getResourceAsStream(
			clazz.getName().replace('.', '/') + ".class")));
	jos.closeEntry();
	}
	
	jos.close();
	VirtualMachine vm = VirtualMachine.attach(JVMPid);
	vm.loadAgent(jarFile.getAbsolutePath());
	vm.detach();
	} catch (Exception e) {
	    e.printStackTrace();
	}

    }
    
    /**
     * Gets bytes from InputStream
     * 
     * @param stream
     * The InputStream
     * @return 
     * Returns a byte[] representation of given stream
     */

    public static byte[] getBytesFromIS(InputStream stream) {

	ByteArrayOutputStream buffer = new ByteArrayOutputStream();
	try {
	    int nRead;
	    byte[] data = new byte[16384];

	    while ((nRead = stream.read(data, 0, data.length)) != -1) {
		buffer.write(data, 0, nRead);
	    }

	    buffer.flush();
	} catch (Exception e) {
	    System.err.println("Failed to convert IS to byte[]!");
	    e.printStackTrace();
	}

	return buffer.toByteArray();

    }
    
    /**
     * Gets bytes from class
     * 
     * @param clazz    
     * The class
     * @return
     * Returns a byte[] representation of given class
     */
    
    public static byte[] getBytesFromClass(Class<?> clazz) {			
	return getBytesFromIS(clazz.getClassLoader().getResourceAsStream( clazz.getName().replace('.', '/') + ".class"));	
    }
    
    /**
     * Gets bytes from resource
     * 
     * @param resource
     * The resource string
     * @return
     * Returns a byte[] representation of given class
     * 
     */
    
    public static byte[] getBytesFromResource(ClassLoader clazzLoader, String resource) {
	return getBytesFromIS(clazzLoader.getResourceAsStream(resource);
    }

}