package com.thefang.yunaiagent.tools;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @Description
 * @Author Thefang
 * @Create 2025/6/16
 */
class ResourceDownloadToolTest {

    @Test
    void downloadResource() {
        ResourceDownloadTool tool = new ResourceDownloadTool();
        String url = "https://b0.bdstatic.com/ugc/BQD_Uj3WYy_kIGnxx_pCPw7959af89abfa594ce21eac4b361d5409.jpg";
        String fileName = "kpbl.jpg";
        String result = tool.downloadResource(url, fileName);
        assertNotNull(result);
    }
}