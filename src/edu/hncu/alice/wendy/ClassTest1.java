package edu.hncu.alice.wendy;

import org.junit.Test;

public class ClassTest1 {

	@Test
	public void a3() {
		System.out.println(System.getProperty("java.class.path"));
	}

	@Test
	public void a2() {
		System.out.println(System.getProperty("java.ext.dirs"));
	}

	@Test
	public void a1() {
		System.out.println(System.getProperty("sun.boot.class.path"));
	}
}
