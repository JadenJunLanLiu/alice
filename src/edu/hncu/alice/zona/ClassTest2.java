package edu.hncu.alice.zona;

public class ClassTest2 {
	public static void main(String[] args) {
		System.out.println(FinalTest.x);
	}
}

class FinalTest {
	public static final int x = 6 / 3;

	static {
		System.out.println("FinalTest static block");
	}
}
