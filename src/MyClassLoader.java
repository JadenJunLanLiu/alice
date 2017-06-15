import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;

public class MyClassLoader extends ClassLoader {
	/*
	 * name 类加载器名字 方便查看是哪个加载器加载的类
	 * path 默认加载类的路径 
	 * fileType class文件的扩展名
	 */
	private String name;
	private String path = "d:/ex/serverlib/";
	private final String fileType = ".class";

	public MyClassLoader(String name) {
		/*
		 * 让系统类加载器成为该类加载器的父加载器
		 */
		super();
		this.name = name;
	}

	public MyClassLoader(ClassLoader parent, String name) {
		/*
		 * 显示指定该类加载器的父加载器
		 */
		super(parent);
		this.name = name;
	}

	@Override
	public String toString() {
		return this.name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getFileType() {
		return fileType;
	}

	@Override
	public Class<?> findClass(String name) throws ClassNotFoundException {
		byte[] data = this.loadClassData(name);
		return this.defineClass(name, data, 0, data.length);
	}

	private byte[] loadClassData(String name) {
		InputStream is = null;
		byte[] data = null;
		ByteArrayOutputStream baos = null;
		try {
			name = name.replace(".", "/");
			//System.out.println(path + name + fileType);
			is = new FileInputStream(new File(path + name + fileType));
			baos = new ByteArrayOutputStream();
			int ind = 0;
			while (-1 != (ind = is.read())) {
				baos.write(ind);
			}
			data = baos.toByteArray();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				is.close();
				baos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return data;
	}

	public static void main(String[] args) {
		MyClassLoader loader1 = new MyClassLoader("loader1");
		loader1.setPath("d:\\ex\\serverlib\\");
		MyClassLoader loader2 = new MyClassLoader(loader1, "loader2");
		loader2.setPath("d:\\ex\\clientlib\\");
		//		MyClassLoader loader3 = new MyClassLoader(null, "loader3");
		//		loader3.setPath("d:/ex/otherlib/");

		//		try {
		//			testLoader(loader2);
		//			testLoader(loader3);
		//		} catch (Exception e) {
		//			e.printStackTrace();
		//		}

		try {
			Class<?> clazz = loader1.loadClass("Simple");
			System.out.println(clazz.hashCode());
			Object object = clazz.newInstance();
			loader1 = null;
			clazz = null;
			object = null;

			loader1 = new MyClassLoader("loader1");
			loader1.setPath("d:/ex/serverlib/");
			clazz = loader1.loadClass("Simple");
			System.out.println(clazz.hashCode());
			object = clazz.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void testLoader(ClassLoader loader)
			throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		Class<?> clazz = loader.loadClass("Simple");
		Object obj = clazz.newInstance();
		//System.out.println("输出数据 = " + obj);
	}

}
