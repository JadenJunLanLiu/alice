package edu.hncu.alice.victoria;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;

public class NIOSocketServer {
	public static final int PORT = 6060;

	public static void main(String[] args) {
		/*
		 * NIO的甬道channel中内容读取到字符缓冲区ByteBuffer时是字节方式存储的，
		 * 对于已字符方式读取和处理的数据必须要进行字符集编码和解码
		 */
		String encoding = System.getProperty("file.encoding");
		//加载字节编码集
		Charset cs = Charset.forName(encoding);
		//分配两个字大小的字节缓存冲区
		ByteBuffer bb = ByteBuffer.allocate(16);
		SocketChannel ch = null;
		ServerSocketChannel ssc = null;
		Selector sel = null;
		try {
			//打开服务端的套接字甬道
			ssc = ServerSocketChannel.open();
			//打开甬道选择器
			sel = Selector.open();
			//将服务端套接字甬道连接方式调整为非阻塞模式
			ssc.configureBlocking(false);
			//将服务端套接字甬道绑定到本机服务端端口
			ssc.socket().bind(new InetSocketAddress(PORT));
			//将服务端套接字甬道OP_ACCEPT事件注册到甬道选择器上
			ssc.register(sel, SelectionKey.OP_ACCEPT);
			System.out.println("Server on port:" + PORT);
			while (true) {
				//甬道选择器开始轮询甬道事件
				sel.select();
				Iterator<SelectionKey> it = sel.selectedKeys().iterator();
				while (it.hasNext()) {
					//获取甬道选择器事件键
					SelectionKey skey = it.next();
					it.remove();
					//服务端套接字甬道发送客户端连接事件，客户端套接字甬道尚未连接
					if (skey.isAcceptable()) {
						//获取服务端套接字甬道上连接的客户端套接字甬道
						ch = ssc.accept();
						System.out.println("Accepted connection from:" + ch.socket());
						//将客户端套接字通过连接模式调整为非阻塞模式
						ch.configureBlocking(false);
						//将客户端套接字甬道OP_READ事件注册到甬道选择器上
						ch.register(sel, SelectionKey.OP_READ);
					} else {
						/*
						 * 客户端套接字甬道已经连接
						 * 获取创建此甬道选择器事件键的套接字甬道
						 */
						ch = (SocketChannel) skey.channel();
						//将客户套接字甬道数据读取到字节缓存区中
						ch.read(bb);
						//使用字符集解码字节缓存区数据
						CharBuffer cb = cs.decode((ByteBuffer) bb.flip());
						String response = cb.toString();
						System.out.println("Encoding:" + response);
						//重绕字节缓冲区，继续读取客户端套接字甬道数据
						ch.write((ByteBuffer) bb.rewind());
						if (response.indexOf("END") != -1) {
							ch.close();
						}
						bb.clear();
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (ch != null) {
				try {
					ch.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (ssc != null) {
				try {
					ssc.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (sel != null) {
				try {
					sel.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
