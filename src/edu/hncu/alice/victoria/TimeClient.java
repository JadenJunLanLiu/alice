package edu.hncu.alice.victoria;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class TimeClient {
	public static void main(String[] args) {
		int port = 6060;
		Socket socket = null;
		BufferedReader br = null;
		PrintWriter pw = null;
		try {
			socket = new Socket("192.168.1.158", port);
			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			pw = new PrintWriter(socket.getOutputStream(), true);
			pw.println("hello");
			pw.println("world");
			pw.println("niubi");
			pw.println("end");

			System.out.println("send message success...");
			while (true) {
				String res = br.readLine();
				if (null == res || "".equals(res)) {
					break;
				}
				System.out.println("the res is " + res);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (pw != null) {
				pw.close();
			}
		}
	}
}
