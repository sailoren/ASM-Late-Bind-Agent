package com.github.Icyene.LateBindAgent;

import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

public class ProfileClassAdapter extends ClassAdapter {

    private String className;

    public ProfileClassAdapter(ClassVisitor visitor, String theClass) {
	super(visitor);
	this.className = theClass;
    }

    public MethodVisitor visitMethod(int access, String name, String desc,
	    String signature, String[] exceptions) {
		
	MethodVisitor mv = super.visitMethod(access, 
		name, 
		desc, 
		signature, 
		exceptions);
	
	
	return new ProfileMethodAdapter(mv, className, name);
    }

}
