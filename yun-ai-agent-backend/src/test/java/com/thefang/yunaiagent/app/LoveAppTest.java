package com.thefang.yunaiagent.app;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

/**
 * @Description
 * @Author Thefang
 * @Create 2025/6/3
 */
@SpringBootTest
class LoveAppTest {

    @Resource
    private LoveApp loveApp;

    @Test
    void testChat() {
        String chatId = UUID.randomUUID().toString();
        // 第一轮会话
        String message = "你好，我是珍珍大侦探";
        String answer = loveApp.doChat(message, chatId);
        // 第二轮会话
        message = "我想要另一半（小方同学）更加爱我";
        answer = loveApp.doChat(message, chatId);
        Assertions.assertNotNull(answer);
        // 第三轮会话
        message = "我的另一半叫什么来着？刚刚和你说过，帮我回忆一下";
        answer = loveApp.doChat(message, chatId);
        Assertions.assertNotNull(answer);

    }

    @Test
    void doChatWithReport() {
        String chatId = UUID.randomUUID().toString();
        // 第一轮会话
        String message = "你好，我是珍珍大侦探,我想要另一半（小方同学）更加爱我,但是我不知道改怎么做";
        LoveApp.LoveReport loveReport = loveApp.doChatWithReport(message, chatId);
        Assertions.assertNotNull(loveReport);
    }

    @Test
    void doChatWithRag() {
        String chatId = UUID.randomUUID().toString();
        // 第一轮会话
        String message = "我已经在谈恋爱了，但是经常会有争吵，你有什么建议吗？";
        String answer = loveApp.doChatWithRag(message, chatId);
        Assertions.assertNotNull(answer);
    }
}