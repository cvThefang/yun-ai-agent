package com.thefang.yunaiagent.rag;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.KeywordMetadataEnricher;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Description 关键词增强器 基于 AI 的文档元信息增强器（为文档补充元信息）
 * @Author Thefang
 * @Create 2025/6/13
 */
@Component
public class MyKeywordEnricher {

    @Resource
    private ChatModel dashcopeChatModel;

    public List<Document> enrichDocuments(List<Document> documents){
        KeywordMetadataEnricher keywordMetadataEnricher = new KeywordMetadataEnricher(dashcopeChatModel, 5);
        return keywordMetadataEnricher.apply(documents);

    }

}
