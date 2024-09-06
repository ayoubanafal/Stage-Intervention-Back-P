package com.interventionManager.services.chat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class ChatCleanupService {

    @Autowired
    private ChatServiceImpl chatService;

    @Scheduled(cron = "0 0 * * * ?")  // Runs every hour
    public void deleteOldMessages() {
        chatService.deleteMessagesOlderThan24Hours();
    }
}
