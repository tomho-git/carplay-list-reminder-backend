package com.carplayPackage.controller;

import com.carplayPackage.service.CarPlayNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carplay")
@CrossOrigin(origins = "*")
public class CarPlayController {
    
    @Autowired
    private CarPlayNotificationService carPlayNotificationService;
    
    @GetMapping("/notification")
    public ResponseEntity<CarPlayNotificationService.NotificationResponse> getCarPlayNotification() {
        CarPlayNotificationService.NotificationResponse response = carPlayNotificationService.getCarPlayNotification();
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/list/{listId}/summary")
    public ResponseEntity<CarPlayNotificationService.NotificationResponse> getShoppingListSummary(@PathVariable Long listId) {
        CarPlayNotificationService.NotificationResponse response = carPlayNotificationService.getShoppingListSummary(listId);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/connect")
    public ResponseEntity<CarPlayNotificationService.NotificationResponse> onCarPlayConnect() {
        CarPlayNotificationService.NotificationResponse response = carPlayNotificationService.getCarPlayNotification();
        return ResponseEntity.ok(response);
    }
}