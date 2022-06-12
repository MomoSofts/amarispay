package com.amarispay.notificationservices.web;

import com.amarispay.notificationservices.beans.Mail;
import com.amarispay.notificationservices.mailservices.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class NotificationServicesController {
    private final MailService mailService;
    private final String userCodeFrom, userCodeSubject, userCodeBeginMessage, userCodeEndMessage;
    @Autowired
    public NotificationServicesController(MailService mailService, String userCodeFrom,
                                          String userCodeSubject, String userCodeBeginMessage,
                                          String userCodeEndMessage){
        this.mailService = mailService;
        this.userCodeFrom = userCodeFrom;
        this.userCodeSubject = userCodeSubject;
        this.userCodeBeginMessage = userCodeBeginMessage;
        this.userCodeEndMessage = userCodeEndMessage;
    }
    @GetMapping("/user-code/{code}/email/{mailto}")
    @ResponseBody
    public int sendUserCodeEmail(@PathVariable("code") String code, @PathVariable("mailto") String mailTo){
        try {
            Mail mail = feedMail(
                    userCodeFrom, mailTo, userCodeSubject,
                    String.format("%s%s%s", userCodeBeginMessage.replace("'", ""), code, userCodeEndMessage.replace("'", ""))
            );
            mailService.sendEmail(mail);
            return 1;
        }catch(Exception e){
            return 0;
        }
    }

    private Mail feedMail(String from, String to,
                          String subject, String content){
            Mail mail = new Mail();
            mail.setContentType("text/html");
            mail.setMailFrom(from);mail.setMailTo(to);
            mail.setMailSubject(subject);mail.setMailContent(content);
            return mail;
    }
}
