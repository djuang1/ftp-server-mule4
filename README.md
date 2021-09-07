# Run an embedded FTP Server on Mule 4.x

> :warning: **Not for CloudHub Production**: This is for demo / example purposes only. Data stored to the file system on a CloudHub worker is not persistent in the event of a restart or deployment. 

 This project is an example that allows you to run a FTP server on Mule 4.x. It leverages [Apache FtpServer](http://mina.apache.org/ftpserver-project/), which is based on [Apache MINA](https://mina.apache.org/), a light weight network application framework, to expose an SFTP server for testing purposes.

## Versions
* Apache FtpServer 1.1.1

## Setup

1. Configure the `ftp.password` properties file with a password

    #### mule-properties.yaml
    ```
    ftp:
        port: "8082"
        password: "password"
    ```
1. Deploy to CloudHub
1. Navigate to `http://<app-name>.<region>.cloudhub.io/ftp?action=start` to start the FTP server

## Use

In order to connect to the FTP server, you need to access it from the external IP address of the Mule worker. The address will be in the following format:

```
mule-worker-<app-name>.<region>.cloudhub.io:8082
```

## Code

Below is a snippet from the code that shows how to setup the FTP server subsystem.

```		
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

userManager.save(user);	    

serverFactory.setUserManager(userManager);	    
serverFactory.addListener("default", factory.createListener());

this.server = serverFactory.createServer();

```
 
