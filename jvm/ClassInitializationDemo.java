package jvm;

/**
* Demonstrates,
  (i) class is NOT loaded on accessing a compile-time constant. Constant is fetched from .class file
  (ii) On accessing a non compile-time constant, class & its super-class are LOADED and INITIALIZED
  (iii) On instantiating the class, it is loaded from memory (loading was done previous step) and 
         (a) superclass constructor is run, i.e., CONSTRUCTOR CHAINING
		 (b) its instance variables are initialized and instance initializer block is run
		 (c) its own constructor is run
*/

interface Superinterface {
	int STATIC_FINAL3 = new ClassInitializationDemo().getInt();
	int STATIC_FINAL5 = new ClassInitializationDemo().getInt5();
	static void staticMethod() {
		System.out.println("Superinterface: staticMethod");
	}
}
class ObjectReference {
	ObjectReference() {
	  System.out.println("ObjectReference: constructor");
	}
}
class Superclass {
    static {
	    System.out.println("Superclass: static initializer");
	}
    {
	    System.out.println("Superclass: instance initializer");
	}
    Superclass () {
    	System.out.println("Superclass: constructor");
	}    
}
class Subclass extends Superclass implements Superinterface {
	static final int STATIC_FINAL = 47;	
	static final int STATIC_FINAL2 = (int) (Math.random() * 5);
	
	//static String stringLiteral = "hello";	
	//public static int STATIC_FINAL4 = new ClassInitializationDemo().getInt();
	
	ObjectReference objectReference = new ObjectReference();
	static {
		System.out.println("Subclass: static initializer");
		//staticFinal = 47;
	}		
	Subclass () {
		System.out.println("Subclass: constructor");	    
	}	
	// Instance initializer is copied to the beginning of constructor by compiler
	{
		System.out.println("Subclass: instance initializer");	
	}
}

public class ClassInitializationDemo {
	{
		System.out.println("\nClassInitializationDemo: instance initializer");	
	}
	static {
		System.out.println("\nClassInitializationDemo: static initializer (Initialization Stage)");
	}
	static int getInt() { 
		System.out.println("ClassInitializationDemo:getInt()");
		return 3; 
	}
	static int getInt5() { 
		System.out.println("ClassInitializationDemo:getInt5()");
		return 5; 
	}
	public static void main(String[] args) throws Exception { 		
		System.out.println("\nJVM invoked the main method ... ");
	    System.out.println("Subclass.STATIC_FINAL: " + Subclass.STATIC_FINAL);
	    //System.out.println("Subclass.stringLiteral: " + Subclass.stringLiteral);
	    System.out.println("Invoking Subclass.STATIC_FINAL2  ... ");
		System.out.println("Subclass.STATIC_FINAL2: " + Subclass.STATIC_FINAL2);		
		System.out.println("\nInstantiating Subclass ...");
		new Subclass();		
		System.out.println("Superinterface.STATIC_FINAL3: " + Superinterface.STATIC_FINAL3);
		//Superinterface.staticMethod();
	}
}
