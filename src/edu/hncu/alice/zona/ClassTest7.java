package edu.hncu.alice.zona;

class CL {
	static {
		System.out.println("Class CL");
	}
}

public class ClassTest7 {
	public static void main(String[] args) throws ClassNotFoundException {
		// 获得系统类加载器
		ClassLoader loader = ClassLoader.getSystemClassLoader();
		Class<?> clazz = loader.loadClass("edu.hncu.alice.zona.CL");
		System.out.println(clazz);
		System.out.println(clazz.toString() + "===" + clazz.getName());
		System.out.println(clazz.getResource("/").getPath());
		System.out.println(clazz.getClassLoader());
		System.out.println("===============");
		clazz = Class.forName("edu.hncu.alice.zona.CL");
	}
}
