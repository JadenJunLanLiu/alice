package edu.hncu.alice.victoria;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class TimeServer {
	public static void main(String[] args) {
		int port = 6060;
		ServerSocket server = null;
		Socket socket = null;
		try {
			server = new ServerSocket(port);
			while (true) {
				socket = server.accept();
				new Thread(new TimeHandler(socket)).start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (server != null) {
				try {
					server.close();
					server = null;
					System.out.println("关闭客户端ServerSocket");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}

class TimeHandler implements Runnable {
	Socket socket = null;

	public TimeHandler() {
	}

	public TimeHandler(Socket socket) {
		this();
		this.socket = socket;
	}

	@Override
	public void run() {
		System.out.println(Thread.currentThread().getName() + " start success...");
		BufferedReader br = null;
		PrintWriter pw = null;
		try {
			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			pw = new PrintWriter(socket.getOutputStream(), true);
			while (true) {
				String str = br.readLine();
				if ("end".equals(str)) {
					break;
				}
				String time = str + " : " + System.currentTimeMillis();
				pw.println(time);
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
