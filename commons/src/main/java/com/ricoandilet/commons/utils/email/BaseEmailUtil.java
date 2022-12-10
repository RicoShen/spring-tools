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
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Properties;

/**
 * javax.mail
 * @author: rico
 * @date: 2022/12/10
 **/
public class BaseEmailUtil extends AbstractEmailUtil {


    @Override
    public boolean sendEmail(String subject,
                      String recipient,
                      String cc,
                      String body) throws Exception {


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
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress("ricomusk@126.com"));
        message.setRecipient(Message.RecipientType.TO, new InternetAddress("ricomusk@outlook.com"));
        message.setRecipient(Message.RecipientType.CC, new InternetAddress("ricoandilet@outlook.com"));
        message.setSubject("Hello,rico","utf-8");
        // body
        Multipart multipart = new MimeMultipart();
        BodyPart bodyPart = new MimeBodyPart();

        //bodyPart.setContent();
        //multipart.addBodyPart(bodyPart);
        message.setText("test");
        Transport.send(message);
        return false;
    }
}
