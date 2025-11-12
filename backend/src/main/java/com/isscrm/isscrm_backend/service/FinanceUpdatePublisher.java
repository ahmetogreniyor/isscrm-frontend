package com.isscrm.isscrm_backend.service;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import java.util.Map;

@Service
public class FinanceUpdatePublisher {

    private final SimpMessagingTemplate messagingTemplate;

    public FinanceUpdatePublisher(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void sendFinanceUpdate(Map<String, Object> summary) {
        messagingTemplate.convertAndSend("/topic/finance-updates", summary);
    }
}
