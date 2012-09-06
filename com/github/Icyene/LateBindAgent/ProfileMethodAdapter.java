package com.github.Icyene.LateBindAgent;

import org.objectweb.asm.MethodAdapter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import static org.objectweb.asm.Opcodes.INVOKESTATIC;

public class ProfileMethodAdapter extends MethodAdapter {
    private String _className, _methodName;
    
    public ProfileMethodAdapter(MethodVisitor visitor,
	    String className,
	    String methodName) {
	super(visitor);
	_className = className;
	_methodName = methodName;
	System.out.println("Profiled " + methodName + " in class " + className
		+ ".");
    }

    public void visitCode() {
	this.visitLdcInsn(_className);
	this.visitLdcInsn(_methodName);
	this.visitMethodInsn(INVOKESTATIC,
		"com/github/Icyene/LateBindAgent/Profile",
		"start",
		"(Ljava/lang/String;Ljava/lang/String;)V");
	super.visitCode();
    }

    public void visitInsn(int inst) {
	switch (inst) {
	case Opcodes.ARETURN:
	case Opcodes.DRETURN:
	case Opcodes.FRETURN:
	case Opcodes.IRETURN:
	case Opcodes.LRETURN:
	case Opcodes.RETURN:
	case Opcodes.ATHROW:
	    this.visitLdcInsn(_className);
	    this.visitLdcInsn(_methodName);
	    this.visitMethodInsn(INVOKESTATIC,
		    "com/github/Icyene/LateBindAgent/Profile",
		    "end",
		    "(Ljava/lang/String;Ljava/lang/String;)V");

	    break;
	default:
	    break;
	}

	super.visitInsn(inst);
    }

}
