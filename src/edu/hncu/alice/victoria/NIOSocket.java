package edu.hncu.alice.victoria;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class NIOSocket {
	private static final int CLIENT_PORT = 6010;
	private static final int SERVER_PORT = 6060;

	//面向流的连接套接字的可选择甬道
	private static SocketChannel ch;
	// 甬道选择器
	private static Selector sel;

	public static void main(String[] args) throws IOException {
		//打开套接字甬道
		ch = SocketChannel.open();
		//打开一个选择器
		sel = Selector.open();
		/*
		 * 获取与甬道关联的套接字，并将该套接字绑定到本机指定端口
		 */
		ch.socket().bind(new InetSocketAddress(CLIENT_PORT));
		//调整此甬道为非阻塞模式
		ch.configureBlocking(false);
		//为甬道选择器注册甬道，并指定操作的选择键集
		ch.register(sel, SelectionKey.OP_READ | SelectionKey.OP_WRITE | SelectionKey.OP_CONNECT);
		//选择甬道上注册的事件，其相应甬道已为I/O操作准备就绪
		sel.select();
		Iterator<SelectionKey> it = sel.selectedKeys().iterator();
		while (it.hasNext()) {
			//获取甬道选择器的键
			SelectionKey sk = it.next();
			it.remove();
			//如果该甬道已经准备好套接字连接
			if (sk.isConnectable()) {
				InetAddress addr = InetAddress.getLocalHost();
				//				InetAddress addr = InetAddress.getByName("192.168.1.158");
				System.out.println("Connect will not block");
				/*
				 * 调用此方法发起一个非阻塞的连接操作，
				 * 如果立即建立连接，则此方法返回true
				 * 反之返回false
				 * 且必须在以后使用finishConnect()完成连接操作
				 * 此处建立和服务端的Socket连接
				 */
				if (!ch.connect(new InetSocketAddress(addr, SERVER_PORT))) {
					//完成非立即连接操作
					ch.finishConnect();
				}
			}
			//此甬道已准备好进行读取
			if (sk.isReadable()) {
				System.out.println("Read will not block");
			}
			//此甬道已准备好进行写入
			if (sk.isWritable()) {
				System.out.println("Write will not block");
			}
		}
		ch.close();
		sel.close();
	}
}
