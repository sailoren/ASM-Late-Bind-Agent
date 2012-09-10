ASM-Late-Bind-Agent
===================

A demo late binding Java agent utilising ASM for transforming classes.

Has a deliciously simple API to inject an agent into a target JVM. Has a deliciously simple API: 
Util.attachAgentToJVM(Class<?>[] {Agent.class, Util.class}, Util.getPIDFromRuntimeMBean()) 
Will inject a simple profiling method into every method call.

Code is pretty well documented, and can be used for educational purposes.

Dependencies:
-------------
ASM. Needed for reinstrumentation. http://asm.ow2.org/

AgentLoader JavaDocs
--------------------
https://dl.dropbox.com/u/67341745/AgentUtils/index.html
License:
--------
The agent, loader, test classes and everything else included in this repository are licensed under the WTFPL. Have fun.