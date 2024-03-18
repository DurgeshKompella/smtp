package com.beehyv.skillbasedrecruitment.smtp.Service;

import com.beehyv.skillbasedrecruitment.smtp.DTO.CalendarRequest;
import com.beehyv.skillbasedrecruitment.smtp.DTO.EmailDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class EmailService {

    @Autowired
    private JavaMailSender emailSender;
    @Autowired
    private CalendarService calendarService;

    public String sendSimpleMail(
            EmailDetails emailDetails) {
        try{
            CalendarRequest calendarRequest = new CalendarRequest();
            calendarRequest.setSubject(emailDetails.getSubject());
            calendarRequest.setBody(emailDetails.getMsgBody());
            calendarRequest.setToEmail(emailDetails.getRecipient());
            calendarRequest.setMeetingStartTime(LocalDateTime.now());
            calendarRequest.setMeetingEndTime(LocalDateTime.now().plusHours(1));
            calendarService.sendCalendarInvite(
                    "noreply@beehired.com",
                    calendarRequest
            );
            return "Mail sent";
        }
        catch (MailException exception){
            System.out.println(exception.getMessage());
            return "Error occurred while sending mail";
        }
        catch(Exception exception){
            return "Something went wrong";
        }

    }
}