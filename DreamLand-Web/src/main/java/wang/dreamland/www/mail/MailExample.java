package wang.dreamland.www.mail;

/**
 * Created by wly on 2018/3/7.
 */

public class MailExample {

    public static void main (String args[]) throws Exception {
        String email = "";
        String validateCode = "";
        String ipAndPort="";
        SendEmail.sendEmailMessage(ipAndPort,email,validateCode);

    }
}
