/*
  Copyright (C) 2018-2021 YouYu information technology (Shanghai) Co., Ltd.
  <p>
  All right reserved.
  <p>
  This software is the confidential and proprietary
  information of YouYu Company of China.
  ("Confidential Information"). You shall not disclose
  such Confidential Information and shall use it only
  in accordance with the terms of the contract agreement
  you entered into with YouYu inc.
 */
package com.ricoandilet.commons.utils.email;

import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.List;
import java.util.Properties;

/**
 * javax.mail
 * @author: rico
 * @date: 2022/12/10
 **/
public class BaseEmailUtil extends AbstractEmailUtil {


    public static boolean sendTextEmail(String subject,
                                        String from,
                                        String recipient,
                                        String cc,
                                        String body){
        try{
            return sendEmail(subject, from, List.of(recipient), List.of(cc),false, body);
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }


    private static boolean sendEmail(String subject,
                      String from,
                      List<String> recipients,
                      List<String> cc,
                      boolean html,
                      String body) throws Exception{


        Properties properties = new Properties();
        properties.setProperty("mail.smtp.host", "smtp.126.com");
        properties.setProperty("mail.smtp.port", "25");
        properties.setProperty("mail.smtp.auth","true");
        properties.setProperty("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("ricomusk@126.com", "CLXOLRFHXXEPLSUD");
            }
        });

        // message
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(from));
        if (recipients != null && recipients.size() > 0) {
          for (String email:recipients){
             message.setRecipient(Message.RecipientType.TO, new InternetAddress(email));
          }
        }
        if (cc !=null && cc.size()>0){
            for (String email:cc){
                message.setRecipient(Message.RecipientType.CC, new InternetAddress(email));
            }
        }
        message.setSubject(subject,"UTF-8");

        // body
        Multipart multipart = new MimeMultipart();
        BodyPart bodyPart = new MimeBodyPart();
        if(html){
            bodyPart.setContent(body,"text/html;utf-8");
        }else {
            bodyPart.setText(body);
        }
        multipart.addBodyPart(bodyPart);
        message.setContent(multipart);

        Transport.send(message);
        return true;
    }


}
