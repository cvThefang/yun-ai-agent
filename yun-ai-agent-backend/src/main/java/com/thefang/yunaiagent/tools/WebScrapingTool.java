package com.thefang.yunaiagent.tools;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

import java.io.IOException;

/**
 * @Description 网页抓取工具类
 * @Author Thefang
 * @Create 2025/6/16
 */
public class WebScrapingTool {

    @Tool(description = "Scrapes the web page at the given URL and returns the content as a string.")
    public String scrapeWebPage(@ToolParam(description = "URL of the web page to scrape") String url) {
        try {
            Document document = Jsoup.connect(url).get();
            return document.html();
        } catch (IOException e) {
            return "Error scraping web page: " + e.getMessage();
        }
    }
}
