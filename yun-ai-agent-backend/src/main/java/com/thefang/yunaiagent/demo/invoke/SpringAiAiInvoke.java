package com.thefang.yunaiagent.demo.invoke;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @Description Spring AI 框架调用 AI 大模型
 * 当我们项目启动的时候，Spring 会自动扫描到加了注解 @component 的bean，发现他实现了 CommandLineRunner 接口，并调用其 run 方法。
 * @Author Thefang
 * @Create 2025/5/19
 */
@Component
public class SpringAiAiInvoke implements CommandLineRunner {

    @Resource
    private ChatModel dashscopeChatModel;

    @Override
    public void run(String... args) throws Exception {
        AssistantMessage assistantMessage = dashscopeChatModel.call(new Prompt("你好，我是珍珍大侦探"))
                .getResult()
                .getOutput();
        System.out.println(assistantMessage.getText());

    }
}
