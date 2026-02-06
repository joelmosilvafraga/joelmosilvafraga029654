package com.joelmofraga.artists_albums_api.websocket.notifier;

import com.joelmofraga.artists_albums_api.websocket.dto.AlbumCreatedEvent;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class AlbumWsNotifier {

    private final SimpMessagingTemplate messaging;

    public AlbumWsNotifier(SimpMessagingTemplate messaging) {
        this.messaging = messaging;
    }

    public void notifyAlbumCreated(AlbumCreatedEvent event) {
        messaging.convertAndSend("/topic/albums/created", event);
    }
}
