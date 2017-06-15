package edu.hncu.alice.victoria;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;

public class NIOSocketClient {
	private static final int CLIENT_PORT = 10200;

	public static void main(String[] args) {
		SocketChannel sc = null;
		Selector sel = null;
		try {
			sc = SocketChannel.open(new InetSocketAddress("192.168.1.158", 6060));
			sel = Selector.open();
			sc.configureBlocking(false);
			sc.socket().bind(new InetSocketAddress(CLIENT_PORT));
			sc.register(sel, SelectionKey.OP_READ | SelectionKey.OP_WRITE | SelectionKey.OP_CONNECT);
			int i = 0;
			boolean written = false;
			boolean done = false;
			String encoding = System.getProperty("file.encoding");
			Charset cs = Charset.forName(encoding);
			ByteBuffer buf = ByteBuffer.allocate(16);
			while (!done) {
				sel.select();
				Iterator<SelectionKey> it = sel.selectedKeys().iterator();
				while (it.hasNext()) {
					SelectionKey key = it.next();
					System.out.println("key = " + key.toString());
					it.remove();
					//获取创建甬道选择器事件键的套接字甬道
					sc = (SocketChannel) key.channel();
					/*
					 * 当前甬道选择器产生连接已经准备就绪事件，
					 * 并且客户端套接字甬道尚未连接到服务端套接字甬道
					 */
					if (key.isConnectable() && !sc.isConnected()) {
						InetAddress addr = InetAddress.getByName("192.168.1.158");
						boolean success = sc.connect(new InetSocketAddress(addr, NIOSocketServer.PORT));
						/*
						 * 如果客户端么有立即连接到服务端，
						 * 则客户端完成非立即连接操作
						 */
						if (!success) {
							sc.finishConnect();
						}
					}
					/*
					 * 如果甬道选择器产生读取操作已准备好事件，
					 * 且已经向甬道写入数据
					 */
					if (key.isReadable() && written) {
						if (sc.read((ByteBuffer) buf.clear()) > 0) {
							written = false;
							//从套接字甬道中读取数据
							String response = cs.decode((ByteBuffer) buf.flip()).toString();
							System.out.println(response);
							if (response.indexOf("END") != -1) {
								done = true;
							}
						}
					}
					/*
					 * 如果甬道选择器产生写入操作已准备好事件，
					 * 并且尚未向甬道写入数据
					 */
					if (key.isWritable() && !written) {
						//向套接字甬道写入数据
						if (i < 10) {
							sc.write(ByteBuffer.wrap(new String("how dy " + i + "\n").getBytes()));
						} else if (i == 10) {
							sc.write(ByteBuffer.wrap(new String("END").getBytes()));
							written = true;
							i++;
						}
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (sc != null) {
					sc.close();
				}
				if (sel != null) {
					sel.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
