package com.thefang.yunaiagent.chatmemory;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.objenesis.strategy.StdInstantiatorStrategy;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.Message;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description
 * @Author Thefang
 * @Create 2025/6/7
 */
public class FileBasedChatMemory implements ChatMemory {

    private final String BASE_DIR;

    // 初始化一个 kryo 对象，用于序列化和反序列化消息对象
    private static final Kryo kryo = new Kryo();

    // 指定一个 kryo的生成策略，用于动态的去注册需要序列化和反序列化消息对象
    static {
        // 设置成不需要手动注册
        kryo.setRegistrationRequired(false);
        // 设置实例化策略
        kryo.setInstantiatorStrategy(new StdInstantiatorStrategy());
    }

    // 构造对象时，指定文件保存目录
    public FileBasedChatMemory(String dir) {
        this.BASE_DIR = dir;
        // 判断文件是否存在，如果不存在就创建
        File baseDir = new File(dir);
        if (!baseDir.exists()) {
            baseDir.mkdirs();
        }
    }

    @Override
    public void add(String conversationId, List<Message> messages) {
        // 先根据会话 ID 获取会话消息列表，再添加消息
        List<Message> messageList = getOrCreateConversation(conversationId);
        messageList.addAll(messages);
        saveConversation(conversationId, messageList);

    }

    @Override
    public List<Message> get(String conversationId, int lastN) {
        List<Message> messageList = getOrCreateConversation(conversationId);
        return messageList.stream()
                .skip(Math.max(0, messageList.size() - lastN))
                .toList();
    }

    @Override
    public void clear(String conversationId) {
        File file = getConversationFile(conversationId);
        if (file.exists()) {
            file.delete();
        }
    }

    /**
     * 获取或创建会话消息的列表
     *
     * @param conversationId 会话 ID
     * @return 会话消息列表
     */
    private List<Message> getOrCreateConversation(String conversationId) {
        File file = getConversationFile(conversationId);
        List<Message> messages = new ArrayList<>();
        if (file.exists()) {
            try (Input input = new Input(new FileInputStream(file))) {
                messages = kryo.readObject(input, ArrayList.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return messages;
    }

    /**
     * 保存会话消息列表
     *
     * @param conversationId 会话 ID
     * @param messages       会话消息列表
     */
    private void saveConversation(String conversationId, List<Message> messages) {
        File file = getConversationFile(conversationId);
        try (Output output = new Output(new FileOutputStream(file))) {
            kryo.writeObject(output, messages);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 取每个会话的文件单独保存
     *
     * @param conversationId 会话 ID
     * @return 会话文件
     */
    private File getConversationFile(String conversationId) {
        return new File(BASE_DIR, conversationId + ".kryo");
    }
}
