package wang.dreamland.www.mail;

import org.apache.log4j.Logger;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * Created by wly on 2018/3/7.
 */
public class SendEmail {
    private final static Logger log = Logger.getLogger( SendEmail.class);
    public static void sendEmailMessage(String ipAndPort,String email,String validateCode) {
       try {
           String host = "smtp.qq.com";   //发件人使用发邮件的电子信箱服务器
           String from = "792649900@qq.com";    //发邮件的出发地（发件人的信箱）
           String to = email;   //发邮件的目的地（收件人信箱）
           // Get system properties
           Properties props = System.getProperties();

           //使用465端口和25端口就只有配置文件不一样
           props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
           props.put("mail.smtp.port", "465");
           props.put("mail.smtp.socketFactory.port", "465");
           props.put("mail.smtp.host", host);
           props.put("mail.smtp.auth", "true");
           props.put("mail.user", from);
           props.put("mail.password", "rnbvboiualvrbfib");



           MyAuthenticator myauth = new MyAuthenticator(from, "rnbvboiualvrbfib");
           Session session = Session.getDefaultInstance(props, myauth);

//    session.setDebug(true);

           // Define message
           MimeMessage message = new MimeMessage(session);


           // Set the from address
           message.setFrom(new InternetAddress(from));

           // Set the to address
           message.addRecipient( Message.RecipientType.TO,
                   new InternetAddress(to));

           // Set the subject
           message.setSubject("梦境网激活邮件通知");
           log.info("now ipAndPort is:"+ipAndPort);

           // Set the content
           message.setContent( "<a href=\"http://"+ipAndPort+"/activecode?email="+email+"&validateCode="+validateCode+"\" target=\"_blank\">请于24小时内点击激活</a>","text/html;charset=gb2312");
           message.saveChanges();

           Transport.send(message);

           log.info( "send validateCode to " + email );
       }catch (Exception e){

           log.info( "Send Email Exception:"+e.getMessage() );
       }

    }
}
