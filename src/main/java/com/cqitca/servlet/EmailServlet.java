package com.cqitca.servlet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message.RecipientType;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class EmailServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static String mailServer;
	private static String mailAddress;
	private static String mailPassword;
	private static String mailNickname;
	private static String backupPath;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json;charset=UTF-8");
		String name = escape(request.getParameter("name"));
		String email = escape(request.getParameter("email"));
		String message = escape(request.getParameter("message"));
		String content = "<p>称呼： <span>" + name + "</span></p><p>邮箱: <span>" + email + "</span></p><hr /><p>内容: </p><p>" + message + "</p>";
		send("642203604@qq.com", "NiX Team Lite留言", content);
		PrintWriter out = response.getWriter();
		out.print("{\"msg\":\"感谢您的支持！\"}");
		out.flush();
		out.close();
	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		mailServer = config.getInitParameter("mailServer");
		mailAddress = config.getInitParameter("mailAddress");
		mailPassword = config.getInitParameter("mailPassword");
		mailNickname = config.getInitParameter("mailNickname");
		backupPath = config.getInitParameter("backupPath");
	}

	private String escape(String content) {
		return content == null ? null : content.trim().replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\"", "&quot;");
	}

	private void send(final String address, final String subject, final String content) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				System.out.println("开始发送邮件...");
				// 初始化props
				Properties props = new Properties();
				props.put("mail.smtp.auth", "true");
				props.put("mail.smtp.host", mailServer);
				// 创建session
				Session session = Session.getInstance(props, new Authenticator() {
					@Override
					protected PasswordAuthentication getPasswordAuthentication() {
						// 验证
						return new PasswordAuthentication(mailAddress, mailPassword);
					}
				});
				final MimeMessage message = new MimeMessage(session);
				try {
					message.setFrom(new InternetAddress(MimeUtility.encodeText(mailNickname) + " <" + mailAddress + ">"));
					message.setRecipient(RecipientType.TO, new InternetAddress(address));
					message.setSubject(subject);
					message.setContent(content, "text/html; charset=UTF-8");
					Transport.send(message);
					System.out.println("发送成功！");
				} catch (Exception e) {
					e.printStackTrace();
					File file = new File(backupPath, "EMAIL-" + System.currentTimeMillis() + ".txt");
					try {
						OutputStream outputStream = new FileOutputStream(file);
						outputStream.write(String.valueOf(address + "\n" + subject + "\n" + content).getBytes());
						outputStream.flush();
						outputStream.close();
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					System.out.println("发送失败！");
				}
			}
		}).start();
	}
}
