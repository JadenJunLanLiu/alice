package edu.hncu.alice;

import org.junit.Test;

public class TestClass1 {

	@Test
	public void a1() throws ClassNotFoundException {
		Class clazz = Class.forName("java.lang.String");
		System.out.println(clazz.getClassLoader());
	}
}
