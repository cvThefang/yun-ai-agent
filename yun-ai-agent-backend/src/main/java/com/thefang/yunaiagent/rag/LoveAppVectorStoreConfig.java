package com.thefang.yunaiagent.rag;

import jakarta.annotation.Resource;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @Description 恋爱大师向量数据库配置（初始化基于内存的向量数据库 Bean）
 * @Author Thefang
 * @Create 2025/6/9
 */
@Configuration
public class LoveAppVectorStoreConfig {

    @Resource
    private LoveAppDocumentLoader loveAppDocumentLoader;

    @Resource
    private MyTokenTextSplitter  myTokenTextSplitter;

    @Resource
    private MyKeywordEnricher  myKeywordEnricher;

    @Bean
    VectorStore loveAppVectorStore(EmbeddingModel embeddingModel) {
        SimpleVectorStore simpleVectorStore = SimpleVectorStore.builder(embeddingModel).build();
        // 加载markdown文档
        List<Document> documentList = loveAppDocumentLoader.loadMarkdowns();
        // 自定义分词器分词 自主切分文档 这边不建议使用自定义的分词器 推荐使用云平台的分词器更为准确、灵活
//        List<Document> splitDocuments = myTokenTextSplitter.splitCustomized(documentList);
        // 自动补充关键词元信息
        List<Document> enrichDocuments = myKeywordEnricher.enrichDocuments(documentList);
        simpleVectorStore.doAdd(enrichDocuments);
        return simpleVectorStore;
    }
}
