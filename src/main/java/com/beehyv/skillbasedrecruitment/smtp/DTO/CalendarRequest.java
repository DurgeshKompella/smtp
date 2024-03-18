package com.beehyv.skillbasedrecruitment.smtp.DTO;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class CalendarRequest {
    private String uid = UUID.randomUUID().toString();
    private String toEmail;
    private String subject;
    private String body;
    private LocalDateTime meetingStartTime;
    private LocalDateTime meetingEndTime;
}
