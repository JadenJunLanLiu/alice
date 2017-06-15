package edu.hncu.alice.zona;

import org.junit.Test;

public class ClassTest1 {

	@Test
	public void a2() throws ClassNotFoundException {
		Class clazz = Class.forName("java.lang.String");
		System.out.println(clazz.getClassLoader());

		Class clazz2 = Class.forName("edu.hncu.alice.zona.C");
		System.out.println(clazz2);
		System.out.println(clazz2.getClassLoader());
	}

	@Test
	public void a1() throws ClassNotFoundException {
		Class clazz = Class.forName("java.lang.String");
		System.out.println(clazz.getClassLoader());

		Class clazz2 = Class.forName("edu.hncu.alice.zona.C");
		System.out.println(clazz2);
		System.out.println(clazz2.getClassLoader());
		System.out.println(clazz2.getClassLoader().getParent());
		System.out.println(clazz2.getResource("/").getPath());
	}

}

class C {
}
