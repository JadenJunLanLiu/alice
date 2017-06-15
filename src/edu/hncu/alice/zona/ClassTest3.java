package edu.hncu.alice.zona;

import java.util.Random;

public class ClassTest3 {
	public static void main(String[] args) {
		System.out.println(FinalTest2.x);
	}
}

class FinalTest2 {
	public static final int x = new Random().nextInt(100);

	static {
		System.out.println("FinalTest2 static block");
	}
}
