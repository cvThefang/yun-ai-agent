package com.thefang.yunaiagent.tools;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @Description
 * @Author Thefang
 * @Create 2025/6/16
 */
class TerminalOperationToolTest {

    @Test
    void executeTerminalCommand() {
        TerminalOperationTool tool = new TerminalOperationTool();
        String command = "dir";
        String result = tool.executeTerminalCommand(command);
        assertNotNull(result);
    }
}