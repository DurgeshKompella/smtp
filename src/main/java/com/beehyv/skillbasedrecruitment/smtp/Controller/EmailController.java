package com.beehyv.skillbasedrecruitment.smtp.Controller;

import com.beehyv.skillbasedrecruitment.smtp.DTO.EmailDetails;
import com.beehyv.skillbasedrecruitment.smtp.Service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mail/v1")
public class EmailController {
    @Autowired
    private EmailService emailService;

    // Sending a simple Email
    @PostMapping("/sendMail")
    public String sendMail(@RequestBody EmailDetails details){
        return emailService.sendSimpleMail(details);
    }
}
