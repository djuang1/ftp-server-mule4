package com.dejim;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.ftplet.Authority;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.ftplet.UserManager;
import org.apache.ftpserver.listener.ListenerFactory;
import org.apache.ftpserver.usermanager.PropertiesUserManagerFactory;
import org.apache.ftpserver.usermanager.impl.BaseUser;
import org.apache.ftpserver.usermanager.impl.ConcurrentLoginPermission;
import org.apache.ftpserver.usermanager.impl.WritePermission;

public class MuleFtpServer {
	
	private static Logger logger = LoggerFactory.getLogger(MuleFtpServer.class);
	
	private int port = 8082;
	private String passwd = "test";
	
	private FtpServer server;

	public MuleFtpServer() throws FtpException {
		init();
	}
	
	public MuleFtpServer(int _port, String _passwd) throws FtpException {
		this.setPort(_port);
		this.setPasswd(_passwd);
		init();
	}
	
	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
	
	public String getPasswd() {
		return passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}
	
	public void init() throws FtpException {
		
		logger.info("*********** init " + port + " ***********");
				
		FtpServerFactory serverFactory = new FtpServerFactory();
		ListenerFactory factory = new ListenerFactory();
		factory.setPort(port);
				
		PropertiesUserManagerFactory userManagerFactory = new PropertiesUserManagerFactory();
	    UserManager userManager = userManagerFactory.createUserManager();
	    
	    BaseUser user = new BaseUser();
	    user.setName("mule");
	    user.setPassword(this.passwd);
	    user.setHomeDirectory("/opt/storage");	
	    user.setAuthorities(Arrays.asList(new Authority[] {new ConcurrentLoginPermission(0, 0), new WritePermission()}));
	    
	    logger.info(user.getHomeDirectory());
	    
	    userManager.save(user);	    
	    
	    serverFactory.setUserManager(userManager);	    
		serverFactory.addListener("default", factory.createListener());
		
		this.server = serverFactory.createServer();
	}
	
	public String start() throws FtpException {
		logger.info("*********** start ***********");
		server.start();
		return "FTP server (Port:" + port + ") starting...";
	}

	public String stop() throws IOException {
		logger.info("*********** stop ***********");
		server.stop();
		return "FTP server stopping...";
	}

}
