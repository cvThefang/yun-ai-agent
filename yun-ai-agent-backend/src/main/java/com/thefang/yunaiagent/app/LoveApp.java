package com.thefang.yunaiagent.app;

import com.thefang.yunaiagent.advisor.MyLoggerAdvisor;
import com.thefang.yunaiagent.chatmemory.FileBasedChatMemory;
import com.thefang.yunaiagent.rag.LoveAppRagCustomAdvisorFactory;
import com.thefang.yunaiagent.rag.QueryRewriter;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.List;

import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY;
import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_RETRIEVE_SIZE_KEY;

/**
 * @Description 恋爱助手 App
 * @Author Thefang
 * @Create 2025/6/3
 */
@Component
@Slf4j
public class LoveApp {

    private final ChatClient chatClient;

    private static final String SYSTEM_PROMPT = "扮演深耕恋爱心理领域的专家。开场向用户表明身份，告知用户可倾诉恋爱难题。" +
            "围绕单身、恋爱、已婚三种状态提问：单身状态询问社交圈拓展及追求心仪对象的困扰；" +
            "恋爱状态询问沟通、习惯差异引发的矛盾；已婚状态询问家庭责任与亲属关系处理的问题。" +
            "引导用户详述事情经过、对方反应及自身想法，以便给出专属解决方案。";

    /**
     * 初始化 ChatClient 客户端
     *
     * @param dashscopeChatModel 阿里云灵积对话模型
     */
    public LoveApp(ChatModel dashscopeChatModel) {
        // 初始化基于文件的对话记忆
        String fileDir = System.getProperty("user.dir") + "/tmp/chat-memory";
        ChatMemory chatMemory = new FileBasedChatMemory(fileDir);

        // 初始化基于内存的对话记忆
//        ChatMemory chatMemory = new InMemoryChatMemory();
        // 初始化聊天客户端 使用构造器模式创建一个可复用的、多个请求共享的 ChatClient 客户端
        chatClient = ChatClient.builder(dashscopeChatModel)
                .defaultSystem(SYSTEM_PROMPT)
                .defaultAdvisors(
                        new MessageChatMemoryAdvisor(chatMemory),
                        // 添加自定义的日志记录 Advisor 可按需开启
                        new MyLoggerAdvisor()
                        // 添加自定义的推理增强 Re2 Advisor 可按需开启 （原理是同一个问题让AI回答两次，优点：增强推理效果 缺点：额外消耗token）
//                        new ReReadingAdvisor()
                )
                .build();
    }

    /**
     * AI 基础对话接口（支持多轮对话记忆）
     *
     * @param message 用户输入的消息
     * @param chatId  对话的唯一标识：相当于一个房间一个对话id
     * @return 对话的回复
     */
    public String doChat(String message, String chatId) {
        ChatResponse chatResponse = chatClient
                .prompt()
                .user(message)
                .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10))
                .call()
                .chatResponse();
        String content = chatResponse.getResult().getOutput().getText();
        log.info("content: {}", content);
        return content;
    }

    /**
     * AI 基础对话接口（支持多轮对话记忆，SSE 流式输出）
     *
     * @param message 用户输入的消息
     * @param chatId  对话的唯一标识：相当于一个房间一个对话id
     * @return 对话的回复
     */
    public Flux<String> doChatByStream (String message, String chatId) {
        Flux<String> content = chatClient
                .prompt()
                .user(message)
                .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10))
                .stream()
                .content();
        return content;
    }

    /**
     * 恋爱报告类-用于结构化输出  jdk21 新特性 record 一个记录的语法用来定义一个类
     *
     * @param title       报告标题
     * @param suggestions 建议列表
     */
    record LoveReport(String title, List<String> suggestions) {

    }

    /**
     * AI 恋爱报告接口（支持结构化输出）
     *
     * @param message 用户输入的消息
     * @param chatId  对话的唯一标识：相当于一个房间一个对话id
     * @return 对话的回复
     */
    public LoveReport doChatWithReport(String message, String chatId) {
        LoveReport loveReport = chatClient
                .prompt()
                .system(SYSTEM_PROMPT + "每次对话后都要生成恋爱结果，标题为{用户名}的恋爱报告，内容为建议列表")
                .user(message)
                .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10))
                .call()
                .entity(LoveReport.class);
        log.info("loveReport: {}", loveReport);
        return loveReport;
    }

    // AI 恋爱知识库问答功能
    @Resource
    private VectorStore loveAppVectorStore;

    @Resource
    private Advisor loveAppRagCloudAdvisor;

    @Resource
    private VectorStore pgVectorVectorStore;

    @Resource
    private QueryRewriter queryRewriter;

    /**
     * 和 RAG 知识库进行对话
     *
     * @param message 用户输入的消息
     * @param chatId  对话的唯一标识：相当于一个房间一个对话id
     * @return 对话的回复
     */
    public String doChatWithRag(String message, String chatId) {
        // 先执行查询重写器
        String rewrittenMessage = queryRewriter.doQueryRewrite(message);
        ChatResponse chatResponse = chatClient
                .prompt()
                // 使用改写后的查询
                .user(rewrittenMessage)
                .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10))
                // 开启日志记录 Advisor
//                .advisors(new MyLoggerAdvisor())
                // 开启 RAG 知识库问答 Advisor
                .advisors(new QuestionAnswerAdvisor(loveAppVectorStore))
                // 应用 RAG 检索增强服务 Advisor（基于云知识库服务）
//                .advisors(loveAppRagCloudAdvisor)
                // 应用 RAG 检索增强服务（基于 PGVector向量存储）
//                .advisors(new QuestionAnswerAdvisor(pgVectorVectorStore))
                // 应用自定义的 RAG 检索增强服务 （文档查询器 + 上下文增强器）
                .advisors(
                        LoveAppRagCustomAdvisorFactory.createLoveAppRagCustomAdvisor(
                                loveAppVectorStore, "单身"
                        )
                )
                .call()
                .chatResponse();
        String content = chatResponse.getResult().getOutput().getText();
        log.info("content: {}", content);
        return content;
    }

    // AI 调用工具能力
    @Resource
    private ToolCallback[] allTools;

    /**
     * AI 调用工具能力接口
     *
     * @param message 用户输入的消息
     * @param chatId  对话的唯一标识：相当于一个房间一个对话id
     * @return 对话的回复
     */
    public String doChatWithTools(String message, String chatId) {
        ChatResponse response = chatClient
                .prompt()
                .user(message)
                .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10))
                // 开启日志，便于观察效果
//                .advisors(new MyLoggerAdvisor())
                .tools(allTools)
                .call()
                .chatResponse();
        String content = response.getResult().getOutput().getText();
        log.info("content: {}", content);
        return content;
    }

    /**
     * Spring AI MCP 服务在启动的时候会自动读取 mcp-servers.json配置文件，从中找到所有的工具，
     * 自动注册到 ToolCallbackProvider工具提供者类中，就可以直接使用 MCP 服务
     * <p>
     * mcp其实就是工具调用，Spring AI 并没有单独开发一套 MCP服务的调用机制，
     * 而是将 MCP 中的工具提取出来，通过 Spring AI 的 ChatClient 客户端进行调用（tools 方法）。
     */
    @Resource
    private ToolCallbackProvider toolCallbackProvider;

    /**
     * AI 调用 MCP 工具能力接口（使用 ToolCallbackProvider 接口）
     *
     * @param message 用户输入的消息
     * @param chatId  对话的唯一标识：相当于一个房间一个对话id
     * @return 对话的回复
     */
    public String doChatWithMcp(String message, String chatId) {
        ChatResponse response = chatClient
                .prompt()
                .user(message)
                .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10))
                // 开启日志，便于观察效果
//                .advisors(new MyLoggerAdvisor())
                .tools(toolCallbackProvider)
                .call()
                .chatResponse();
        String content = response.getResult().getOutput().getText();
        log.info("content: {}", content);
        return content;
    }

}
