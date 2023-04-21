package com.haiyue.messaging.service;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.haiyue.messaging.dao.MessageDAO;
import com.haiyue.messaging.dao.UserDAO;
import com.haiyue.messaging.model.Message;
import com.haiyue.messaging.model.User;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Log4j2
public class WebSocketMessageService {

    @Autowired
    private AmazonSNS snsClient;

    @Autowired
    private MessageDAO messageDAO;

    @Autowired
    private UserDAO userDAO;

    private Map<Integer, List<WebSocketSession>> userIdToSessions = new ConcurrentHashMap<>();

    private Map<Integer, Integer> userIdToMessageId = new ConcurrentHashMap<>();

    @Value("${sns.topicArn}")
    private String snsTopicArn;

    public void afterConnectionEstablished(User user, WebSocketSession session) throws IOException {

        if (!userIdToSessions.containsKey(user.getId())) {
            userIdToSessions.put(user.getId(), new ArrayList<>());
        }
        userIdToSessions.get(user.getId()).add(session);

        TextMessage message = new TextMessage("Welcome " + user.getUsername());
        session.sendMessage(message);

        log.info("Connection established with " + user.getUsername());
        sendNotification("New Connection", "WebSocket connection established with " + user.getUsername());
    }

    public void afterConnectionClosed(WebSocketSession session) {
        userIdToSessions.values().forEach(sessions -> sessions.remove(session));
    }
    
    public void sendPeriodicMessage() throws IOException {
        for (List<WebSocketSession> s : userIdToSessions.values()) {
            for (WebSocketSession session : s) {
                session.sendMessage(new TextMessage("Hey"));
            }
        }
    }

    public void sendNewMessageNotification() {
        for (Integer userId : userIdToSessions.keySet()) {
            if (userIdToSessions.get(userId).isEmpty()) {
                continue;
            }
            if (!userIdToMessageId.containsKey(userId)) {
                userIdToMessageId.put(userId, -1);
            }
            List<Message> messages = this.messageDAO.selectIdAndSenderUsersId(userId, userIdToMessageId.get(userId));
            for (Message message : messages) {
                String subject = "New Message from Messaging System";
                String mess = this.userDAO.selectNickNameById(message.getSenderUserId()) + " send you a new message";
                sendNotification(subject, mess);
            }
        }

    }

    public void sendNotification(String subject, String message) {
        PublishRequest publishRequest = new PublishRequest(this.snsTopicArn, message, subject);
        PublishResult publishResult = snsClient.publish(publishRequest);
        log.info("Notification sent, message ID: {}", publishResult.getMessageId());
    }
}
