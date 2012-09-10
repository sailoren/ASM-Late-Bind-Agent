package com.github.Icyene.LateBindAgent;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.management.ManagementFactory;
import java.lang.reflect.Field;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;

import com.sun.tools.attach.AgentInitializationException;
import com.sun.tools.attach.AgentLoadException;
import com.sun.tools.attach.AttachNotSupportedException;
import com.sun.tools.attach.VirtualMachine;

public class Util {

    /**
     * Gets the current JVM PID
     * 
     * @return Returns the PID
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
     *            A Class<?>[] of classes to be included in agent
     * @param JVMPid
     *            The PID of the JVM to attach to
     * @throws IOException
     * @throws AttachNotSupportedException
     * @throws AgentInitializationException
     * @throws AgentLoadException
     */

    public static void attachAgentToJVM(Class<?>[] agentClasses,
	    Class<?>[] toAttachTo, String JVMPid) throws IOException,
	    AttachNotSupportedException, AgentLoadException,
	    AgentInitializationException {

	final File jarFile = File.createTempFile("agent", ".jar");
	jarFile.deleteOnExit();

	final Manifest manifest = new Manifest();
	final Attributes mainAttributes = manifest.getMainAttributes();
	mainAttributes.put(Attributes.Name.MANIFEST_VERSION, "1.0");
	mainAttributes.put(new Attributes.Name("Agent-Class"),
		Agent.class.getName());
	mainAttributes.put(new Attributes.Name("Can-Retransform-Classes"),
		"true");
	mainAttributes.put(new Attributes.Name("Can-Redefine-Classes"),
		"true");

	final JarOutputStream jos = new JarOutputStream(
		new FileOutputStream(
			jarFile), manifest);

	for (Class<?> clazz : agentClasses) {
	    final JarEntry agent = new JarEntry(clazz.getName().replace(
		    '.',
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

    }

    /**
     * Gets bytes from InputStream
     * 
     * @param stream
     *            The InputStream
     * @return Returns a byte[] representation of given stream
     * @throws IOException
     */

    public static byte[] getBytesFromIS(InputStream stream) throws IOException {

	ByteArrayOutputStream buffer = new ByteArrayOutputStream();

	int nRead;
	byte[] data = new byte[65536];

	while ((nRead = stream.read(data, 0, data.length)) != -1) {
	    buffer.write(data, 0, nRead);
	}

	buffer.flush();

	return buffer.toByteArray();

    }

    /**
     * Gets bytes from class
     * 
     * @param clazz
     *            The class
     * @return Returns a byte[] representation of given class
     * @throws IOException
     */

    public static byte[] getBytesFromClass(Class<?> clazz) throws IOException {
	return getBytesFromIS(clazz.getClassLoader().getResourceAsStream(
		clazz.getName().replace('.', '/') + ".class"));
    }

    /**
     * Gets bytes from resource
     * 
     * @param resource
     *            The resource string
     * @return Returns a byte[] representation of given class
     * @throws IOException
     * 
     */

    public static byte[] getBytesFromResource(ClassLoader clazzLoader,
	    String resource) throws IOException {
	return getBytesFromIS(clazzLoader.getResourceAsStream(resource));
    }

    /**
     * Adds a path to the current path
     * 
     * @param path
     *            The path.
     * @throws SecurityException
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     */

    public static void addToLibPath(String path) throws NoSuchFieldException,
	    SecurityException, IllegalArgumentException, IllegalAccessException {
	if (System.getProperty("java.library.path") != null) {
	    // If java.library.path is not empty, we will prepend our path
	    // Note that path.separator is ; on Windows and : on Unix-like,
	    // so we can't hard code it.
	    System.setProperty("java.library.path",
		    path + System.getProperty("path.separator")
			    + System.getProperty("java.library.path"));
	} else {
	    System.setProperty("java.library.path", path);
	}

	// Important: java.library.path is cached
	// We will be using reflection to clear the cache

	Field fieldSysPath = ClassLoader.class
		.getDeclaredField("sys_paths");
	fieldSysPath.setAccessible(true);
	fieldSysPath.set(null, null);

    }

}
