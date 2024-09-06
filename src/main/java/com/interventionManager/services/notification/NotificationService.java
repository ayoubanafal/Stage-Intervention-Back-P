package com.interventionManager.services.notification;


import com.interventionManager.config.NotificationWebSocketHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationWebSocketHandler notificationWebSocketHandler;


    public void sendNotificationToTechnician(Long technicianId, String message) {
        try {
            notificationWebSocketHandler.sendNotificationToTechnician(technicianId, message);
        } catch (Exception e) {
            // Handle exception
            e.printStackTrace();
        }
    }
}