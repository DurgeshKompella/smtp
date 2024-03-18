package com.beehyv.skillbasedrecruitment.smtp.Service;

import com.beehyv.skillbasedrecruitment.smtp.DTO.CalendarRequest;
import jakarta.activation.DataHandler;
import jakarta.activation.MailcapCommandMap;
import jakarta.activation.MimetypesFileTypeMap;
import jakarta.mail.Message;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
@Component
public class CalendarService {
    @Autowired
    private JavaMailSender mailSender;
    public void sendCalendarInvite(String fromEmail, CalendarRequest calendarRequest) throws Exception {
        System.out.println("Calendar invite triggered");
        final MimetypesFileTypeMap mimetypes = (MimetypesFileTypeMap) MimetypesFileTypeMap.getDefaultFileTypeMap();
        mimetypes.addMimeTypes("text/calendar ics ICS");
        final MailcapCommandMap mc = (MailcapCommandMap) MailcapCommandMap.getDefaultCommandMap();
        mc.addMailcap("text/calendar;; x-java-content-handler=com.sun.mail.handlers.text_plain");

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        mimeMessage.addHeaderLine("method=REQUEST");
        mimeMessage.addHeaderLine("charset=UTF-8");
        mimeMessage.addHeaderLine("component=VEVENT");
        mimeMessage.setFrom(new InternetAddress(fromEmail));
        mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(calendarRequest.getToEmail()));
        mimeMessage.setSubject(calendarRequest.getSubject());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd HHmmss");
        StringBuilder builder = new StringBuilder();
        builder.append("BEGIN:VCALENDAR\n" +
                "METHOD:REQUEST\n" +
                "PRODID:Microsoft Exchange Server 2010\n" +
                "VERSION:2.0\n" +
                "BEGIN:VTIMEZONE\n" +
                "TZID:Asia/Kolkata\n" +
                "END:VTIMEZONE\n" +
                "BEGIN:VEVENT\n" +
                "ATTENDEE;ROLE=REQ-PARTICIPANT;RSVP=TRUE:MAILTO:" + calendarRequest.getToEmail() + "\n" +
                "ORGANIZER;CN=Foo:MAILTO:" + fromEmail + "\n" +
                "DESCRIPTION;LANGUAGE=en-US:" + calendarRequest.getBody() + "\n" +
                "UID:"+calendarRequest.getUid()+"\n" +
                "SUMMARY;LANGUAGE=en-US:Discussion\n" +
                "DTSTART:" + formatter.format(calendarRequest.getMeetingStartTime()).replace(" ", "T") + "\n" +
                "DTEND:" + formatter.format(calendarRequest.getMeetingEndTime()).replace(" ", "T") + "\n" +
                "CLASS:PUBLIC\n" +
                "PRIORITY:5\n" +
                "DTSTAMP:20200922T105302Z\n" +
                "TRANSP:OPAQUE\n" +
                "STATUS:CONFIRMED\n" +
                "SEQUENCE:$sequenceNumber\n" +
                "LOCATION;LANGUAGE=en-US:Microsoft Teams Meeting\n" +
                "BEGIN:VALARM\n" +
                "DESCRIPTION:REMINDER\n" +
                "TRIGGER;RELATED=START:-PT15M\n" +
                "ACTION:DISPLAY\n" +
                "END:VALARM\n" +
                "END:VEVENT\n" +
                "END:VCALENDAR");
        MimeBodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setHeader("Content-Class", "urn:content-classes:calendarmessage");
        messageBodyPart.setHeader("Content-ID", "calendar_message");
        DataHandler dataHandler =new DataHandler(builder.toString(),"text/calendar;method=REQUEST;name=\"invite.ics\"");
        messageBodyPart.setDataHandler(dataHandler);
        MimeMultipart multipart = new MimeMultipart();
        multipart.addBodyPart(messageBodyPart);
        mimeMessage.setContent(multipart);
        mailSender.send(mimeMessage);
        System.out.println("Calendar invite sent");

    }
}
