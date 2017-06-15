

public class Simple {
	public int v1 = 1;

	public Simple() {
		System.out.println("Simple is loaded by : " + this.getClass().getClassLoader());
		new Dog();
	}
}
