package com.lelandyan.ct.analysis;

import com.lelandyan.ct.analysis.tool.AnalysisBeanTool;
import com.lelandyan.ct.analysis.tool.AnalysisTextTool;
import org.apache.hadoop.util.ToolRunner;

public class AnalysisData {
    public static void main(String[] args) throws Exception {
        System.out.println("begin ******");
        //int result = ToolRunner.run(new AnalysisTextTool(), args);
        int result = ToolRunner.run(new AnalysisBeanTool(), args);
        System.out.println("end ******");
    }
}
