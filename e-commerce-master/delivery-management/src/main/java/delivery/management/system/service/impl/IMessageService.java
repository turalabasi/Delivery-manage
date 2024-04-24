package delivery.management.system.service.impl;

import common.notification.dto.request.EmailRequest;
import common.notification.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class IMessageService {

    @Autowired
    private EmailService emailService;

    @KafkaListener(topics = "emailRequests", groupId = "email-group")
    public void listenForEmailRequests(EmailRequest emailRequest) {
        emailService.sendEmail(emailRequest);
    }

}
