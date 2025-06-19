package com.thefang.yunaiagent.tools;

import org.springframework.ai.tool.annotation.Tool;

/**
 * @Description 终止工具类 （作用是让自主规划的智能体能够合理的中断）
 * @Author Thefang
 * @Create 2025/6/19
 */
public class TerminateTool {

    @Tool(description = """  
            Terminate the interaction when the request is met OR if the assistant cannot proceed further with the task.  
            "When you have finished all the tasks, call this tool to end the work.  
            """)
    public String doTerminate() {
        return "任务结束";
    }
}
