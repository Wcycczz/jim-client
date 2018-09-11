package org.jim.client;

import org.jim.common.Const;
import org.jim.common.packets.ChatBody;
import org.jim.common.packets.Command;
import org.jim.common.packets.LoginReqBody;
import org.jim.common.packets.MessageReqBody;
import org.jim.common.tcp.TcpPacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tio.client.AioClient;
import org.tio.client.ClientChannelContext;
import org.tio.client.ClientGroupContext;
import org.tio.client.ReconnConf;
import org.tio.client.intf.ClientAioHandler;
import org.tio.client.intf.ClientAioListener;
import org.tio.core.Aio;
import org.tio.core.Node;

/**
 * 
 * 版本: [1.0] 功能说明: 作者: WChao 创建时间: 2017年8月30日 下午1:05:17
 */
public class HelloClientStarter {

	private static Logger log = LoggerFactory.getLogger(HelloClientStarter.class);

	// 服务器节点
	public static Node serverNode = new Node("im.rl-telecom.com", Const.SERVER_PORT);

	// handler, 包括编码、解码、消息处理
	public static ClientAioHandler aioClientHandler = new HelloClientAioHandler();

	// 事件监听器，可以为null，但建议自己实现该接口，可以参考showcase了解些接口
	public static ClientAioListener aioListener = null;

	// 断链后自动连接的，不想自动连接请设为null
	private static ReconnConf reconnConf = new ReconnConf(5000L);

	// 一组连接共用的上下文对象
	public static ClientGroupContext clientGroupContext = new ClientGroupContext(aioClientHandler, aioListener,
			reconnConf);

	public static AioClient aioClient = null;
	public static ClientChannelContext clientChannelContext = null;

	/**
	 * 启动程序入口
	 */
	public static void main(String[] args) throws Exception {
		// clientGroupContext.setHeartbeatTimeout(org.tio.examples.helloworld.common.Const.TIMEOUT);
		clientGroupContext.setHeartbeatTimeout(1000 * 60 * 4 * 1);
		aioClient = new AioClient(clientGroupContext);
		clientChannelContext = aioClient.connect(serverNode);
		// 连上后，发条消息玩玩
		send();
	}

	private static void send() throws Exception {
		byte[] loginBody = new LoginReqBody("", "", "5b366b03f0ab049b1a29f14a107ce166").toByte();
		TcpPacket loginPacket = new TcpPacket(Command.COMMAND_LOGIN_REQ, loginBody);
		Aio.send(clientChannelContext, loginPacket);// 先登录;

		try {
			Thread.sleep(3000);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		// 获取离线消息 
		MessageReqBody reqMessageBody = new MessageReqBody();
		reqMessageBody.setUserId("v45cjokmum");
		reqMessageBody.setType(0);
		byte[] reqMessageByte = reqMessageBody.toByte();
		TcpPacket reqMessagePacket = new TcpPacket(Command.COMMAND_GET_MESSAGE_REQ, reqMessageByte);
		Aio.send(clientChannelContext, reqMessagePacket);// 取离线消息

//		System.out.println("发送消息： ");
//
//		ChatBody chatBody = new ChatBody().setFrom("27c7q1mw1e").setTo("v45cjokmum").setMsgType(0).setChatType(2)
//				.setGroup_id("").setContent("我在找你呢444");
//		TcpPacket chatPacket = new TcpPacket(Command.COMMAND_CHAT_REQ, chatBody.toByte());
//		Aio.send(clientChannelContext, chatPacket);

		if (false) {

			// 取离线消息
//			MessageReqBody reqMessageBody = new MessageReqBody();
//			reqMessageBody.setUserId("rladmin");
//			reqMessageBody.setType(0);
//			byte[] reqMessageByte = reqMessageBody.toByte();
//			TcpPacket reqMessagePacket = new TcpPacket(Command.COMMAND_GET_MESSAGE_REQ, reqMessageByte);
//			Aio.send(clientChannelContext, reqMessagePacket);// 取离线消息
//
//			System.out.println("发送消息： ");

//			ChatBody chatBody = new ChatBody().setFrom("rladmin").setTo("rladmin").setMsgType(0).setChatType(2)
//					.setGroup_id("").setContent("大家好！再次试试");
//			TcpPacket chatPacket = new TcpPacket(Command.COMMAND_CHAT_REQ, chatBody.toByte());
//			Aio.send(clientChannelContext, chatPacket);
		}
	}
}
