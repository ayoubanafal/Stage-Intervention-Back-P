package com.interventionManager.services.chat;

import com.interventionManager.entities.Chat;
import com.interventionManager.repositories.ChatRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ChatServiceImpl{
    private ChatRepository chatRepository;
    public Chat saveMessage(Chat chatMessage) {
        chatMessage.setTimestamp(LocalDateTime.now());
        System.out.println(chatMessage+"//////// chat messages here/////");
        return chatRepository.save(chatMessage);
    }

    public List<Chat> getRecentMessages(String technicianId) {
        LocalDateTime yesterday = LocalDateTime.now().minusHours(24);
        return chatRepository.findByTechnicianIdAndAndTimestampAfter(technicianId, yesterday);
    }
    public void deleteMessagesOlderThan24Hours() {
        LocalDateTime cutoffTime = LocalDateTime.now().minusHours(24);
        List<Chat> oldMessages = chatRepository.findAllByTimestampBefore(cutoffTime);
        chatRepository.deleteAll(oldMessages);
    }
}
