package com.haiyue.messaging.controller;

import com.haiyue.messaging.model.User;
import com.haiyue.messaging.service.UserService;
import com.haiyue.messaging.service.WebSocketMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.List;

// client <-> server
// 1. client: subscribe/connect
// 2. server: send message
// 3. server/client: close connection
@Controller
public class WebSocketMessageController extends TextWebSocketHandler {

    @Autowired
    private UserService userService;

    @Autowired
    private WebSocketMessageService webSocketMessageService;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // ws://localhost:8080
        List<String> loginTokens = session.getHandshakeHeaders().get("Login-Token");
        User user = this.userService.authenticate(loginTokens.get(0));
        this.webSocketMessageService.afterConnectionEstablished(user, session);
    }

//    @Scheduled(fixedRate = 1000)
//    void sendPeriodicMessage() throws IOException {
//        this.webSocketMessageService.sendPeriodicMessage();
//    }

    @Scheduled(fixedRate = 1000)
    void sendNewMessageNotification() throws IOException {
        this.webSocketMessageService.sendNewMessageNotification();
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) {
        this.webSocketMessageService.afterConnectionClosed(session);
    }

}
