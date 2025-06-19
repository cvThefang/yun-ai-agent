package com.thefang.yunimagesearchmcpserver.tools;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @Description
 * @Author Thefang
 * @Create 2025/6/18
 */
@SpringBootTest
class ImageSearchToolTest {

    @Resource
    private ImageSearchTool imageSearchTool;

    @Test
    void searchMediumImages() {
        List<String> result = imageSearchTool.searchMediumImages("computer");
        Assertions.assertNotNull(result);
    }
}