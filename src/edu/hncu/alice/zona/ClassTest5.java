package edu.hncu.alice.zona;

class Parent2 {
	public static int a = 3;
	static {
		System.out.println("Paretn2 static block");
	}
}

class Child2 {
	public static int b = 4;
	static {
		System.out.println("Child2 static block");
	}
}

public class ClassTest5 {
	static {
		System.out.println("Test5 static block");
	}

	public static void main(String[] args) {
		Parent2 parent;
		System.out.println("-----------");
		parent = new Parent2();
		System.out.println(Parent2.a);
		System.out.println(Child.b);
	}
}
