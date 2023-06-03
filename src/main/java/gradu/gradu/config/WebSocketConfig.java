package gradu.gradu.config;

import gradu.gradu.controller.BoardController;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new BoardController.MyWebSocketHandler(), "/bbs/ws")
                .setAllowedOrigins("*");
        registry.addHandler(new BoardController.MyWebSocketHandler(), "/view/ws/{id}")
                .setAllowedOrigins("*");
    }
}