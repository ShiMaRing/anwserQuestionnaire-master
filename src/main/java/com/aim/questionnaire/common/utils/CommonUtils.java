package com.aim.questionnaire.common.utils;

import com.sun.mail.util.MailSSLSocketFactory;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class CommonUtils {

    /**
     * 生成随机数
     * @param num 位数
     * @return
     */
    public static String createRandomNum(int num){
        String randomNumStr = "";
        for(int i = 0; i < num;i ++){
            int randomNum = (int)(Math.random() * 10);
            randomNumStr += randomNum;
        }
        return randomNumStr;
    }

    public static void sendEmail(String myqq, String userqq , String title, String textContext) throws Exception{
            //授权码
        String password = "eybmpvedzwkjdaff";


        Properties properties = new Properties();
        //设置qq邮箱的服务器
        properties.setProperty("mail.host","smtp.qq.com");
        //邮件发送协议
        properties.setProperty("mail.transport.protocol","smtp");
        //验证用户名以及邮箱授权码
        properties.setProperty("mail.smtp.auth","true");

        //关于qq邮箱，还要设置SSL加密
        MailSSLSocketFactory sf = new MailSSLSocketFactory();
        sf.setTrustAllHosts(true);
        properties.put("mail.smtp.ssl.enable","true");
        properties.put("mail.smtp.ssl.socketFactory",sf);

        //开始发送邮件
        //创建定义整个应用程序所需的环境信息的session

        Session session = Session.getDefaultInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication( myqq + "@qq.com",password);
            }
        });
        //开启session，查看程序的运行状态
        session.setDebug(true);
        //通过session得到transport对象
        Transport ts = session.getTransport();

        //使用邮箱的用户名和授权码连上邮箱服务器
        ts.connect("smtp.qq.com",myqq + "@qq.com",password);
        //创建邮件
        MimeMessage mimeMessage = new MimeMessage(session);
        //指明邮件的发件人
        mimeMessage.setFrom(new InternetAddress(myqq + "@qq.com"));
        //指明邮件的收件人
        mimeMessage.setRecipient(Message.RecipientType.TO,new InternetAddress( userqq));
        //邮件的标题
        mimeMessage.setSubject(title);
        //邮件的文本内容
        mimeMessage.setContent(textContext,"text/html;charset=utf-8");
        //发送邮件
        ts.sendMessage(mimeMessage,mimeMessage.getAllRecipients());
        //关闭连接
        ts.close();
    }
}
