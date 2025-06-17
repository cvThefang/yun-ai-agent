package com.thefang.yunaiagent.tools;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @Description
 * @Author Thefang
 * @Create 2025/6/16
 */
class WebScrapingToolTest {

    @Test
    void scrapeWebPage() {
        WebScrapingTool tool = new WebScrapingTool();
        String url = "https://www.mi.com";
        String result = tool.scrapeWebPage(url);
        assertNotNull(result);
    }
}