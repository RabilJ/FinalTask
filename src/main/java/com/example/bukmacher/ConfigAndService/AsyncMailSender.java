package com.example.bukmacher.ConfigAndService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.mail.*;
import javax.mail.internet.MimeMessage;
import java.io.File;

@Component
public class AsyncMailSender {
    private final Logger logger = LoggerFactory.getLogger(AsyncMailSender.class);
    private JavaMailSenderImpl javaMailSender;
    private MailProperties mailProperties;

    public AsyncMailSender(JavaMailSenderImpl javaMailSender, MailProperties mailProperties) {
        this.javaMailSender = javaMailSender;
        this.mailProperties = mailProperties;
    }
    @Async
    public void sendMail(String username, String to, String subject, String content){
        sendEmailWithAttachment(username,to, subject, content, null, null);
    }
    @Async
    public void sendEmailWithAttachment(String username,String to, String subject, String content, String attachmentName, File attachment){
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try{
            String fromEmail = "ultrabot@interia.pl";
            String fromName = "Ultra Bot";
            String replyTo = "ultrabot@interia.pl";
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage,true,"UTF-8");
            message.setTo(to);
            message.setFrom(fromEmail,fromName);
            message.setSubject(subject);
            message.setReplyTo(replyTo);
            message.setText(content,true);
            if(attachment!=null) {
            message.addAttachment(attachmentName, attachment);
            }
            javaMailSender.send(mimeMessage);
            saveEmailToSendDir(mimeMessage);
        } catch (Exception e) {
            logger.warn("E-mail could not be sent to User '{}'",username,e.getMessage());
        }
    }
    private void saveEmailToSendDir(MimeMessage mimeMessage) throws MessagingException{
        Session session = javaMailSender.getSession();
        Store store = session.getStore("imap");
        String host = mailProperties.getHost();
        String username = mailProperties.getUsername();
        String password = mailProperties.getPassword();

        store.connect(host,username,password);
        Folder folder = store.getFolder("INBOX.Sent");
        folder.open(Folder.READ_WRITE);
        mimeMessage.setFlag(Flags.Flag.SEEN,true);
        folder.appendMessages(new Message[]{mimeMessage});
        store.close();
    }
}
