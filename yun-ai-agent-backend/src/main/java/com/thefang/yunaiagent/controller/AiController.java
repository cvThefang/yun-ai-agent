package com.thefang.yunaiagent.controller;

import com.thefang.yunaiagent.agent.YunManus;
import com.thefang.yunaiagent.app.LoveApp;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;

import java.io.IOException;

/**
 * @Description
 * @Author Thefang
 * @Create 2025/6/20
 */
@RestController
@RequestMapping("/ai")
public class AiController {

    @Resource
    private LoveApp loveApp;

    @Resource
    private ToolCallback[] allTools;

    @Resource
    private ChatModel dashscopeChatModel;

    /**
     * 同步调用 AI 恋爱大师应用
     *
     * @param message 消息内容
     * @param chatId  会话 ID
     * @return AI 回复
     */
    @GetMapping("/love_app/chat/sync")
    public String doChatWithLoveAppSync(String message, String chatId) {
        return loveApp.doChat(message, chatId);
    }

    /**
     * SSE 流式调用 AI 恋爱大师应用
     *
     * @param message 消息内容
     * @param chatId  会话 ID
     * @return AI 回复
     */
    @GetMapping(value = "/love_app/chat/sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> doChatWithLoveAppSSE(String message, String chatId) {
        return loveApp.doChatByStream(message, chatId);
    }

    /**
     * SSE 流式调用 AI 恋爱大师应用
     *
     * @param message 消息内容
     * @param chatId  会话 ID
     * @return AI 回复
     */
    @GetMapping(value = "/love_app/chat/server_sent_event")
    public Flux<ServerSentEvent<String>> doChatWithLoveAppServerSentEvent(String message, String chatId) {
        return loveApp.doChatByStream(message, chatId)
                .map(chunk -> ServerSentEvent.<String>builder(chunk)
                        .data(chunk)
                        .build());
    }

    /**
     * SSE 流式调用 AI 恋爱大师应用
     *
     * @param message 消息内容
     * @param chatId  会话 ID
     * @return AI 回复
     */
    @GetMapping(value = "/love_app/chat/sse_emitter")
    public SseEmitter doChatWithLoveAppSseEmitter(String message, String chatId) {
        // 创建一个超时时间较长的 SseEmitter 三分钟超时
        SseEmitter emitter = new SseEmitter(1800000L);
        // 获取 Flux 响应式数据流 并且直接通过订阅推送给 SseEmitter
        loveApp.doChatByStream(message, chatId)
                .subscribe(
                        // 处理每条消息
                        chunk -> {
                            try {
                                emitter.send(chunk);
                            } catch (IOException e) {
                                emitter.completeWithError(e);
                            }
                        },
                        // 处理错误
                        emitter::completeWithError,
                        // 处理完成
                        emitter::complete
                );
        // 返回emitter
        return emitter;
    }

    /**
     * 流式调用 Manus 超级智能体
     *
     * @param message 消息内容
     * @return AI 回复
     */
    @GetMapping("/manus/chat")
    public SseEmitter doChatWithManus(String message) {
        YunManus yuManus = new YunManus(allTools, dashscopeChatModel);
        return yuManus.runStream(message);
    }
}
