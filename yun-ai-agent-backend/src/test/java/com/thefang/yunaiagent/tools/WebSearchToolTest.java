package com.thefang.yunaiagent.tools;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @Description
 * @Author Thefang
 * @Create 2025/6/16
 */
@SpringBootTest
class WebSearchToolTest {

    @Value("${search-api.api-key}")
    private String searchApiKey;

    @Test
    void searchWeb() {
        WebSearchTool webSearchTool = new WebSearchTool(searchApiKey);
        String query = "谁是珍珍大厨";
        String result = webSearchTool.searchWeb(query);
        Assertions.assertNotNull(result);
    }
}