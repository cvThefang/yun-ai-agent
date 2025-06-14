package com.thefang.yunaiagent.rag;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;

/**
 * @Description
 * @Author Thefang
 * @Create 2025/6/11
 */
@SpringBootTest
class PgVectorVectorStoreConfigTest {

    @Resource
    VectorStore pgVectorVectorStore;

    @Test
    void pgVectorVectorStore() {
        List<Document> documents = List.of(
                new Document("你好，我是珍珍大侦探，专门帮助人们解决问题。", Map.of("meta1", "meta1")),
                new Document("你好，我是歌手阿珍，欢迎来到我的专属音乐空间。"),
                new Document("nice to meet you, my name is Jane, I love to sing and dance.", Map.of("meta2", "meta2")));
        // 添加文档
        pgVectorVectorStore.add(documents);
        // 相似度查询
        List<Document> results = pgVectorVectorStore.similaritySearch(SearchRequest.builder().query("谁是歌手").topK(3).build());
        Assertions.assertNotNull(results);
    }
}