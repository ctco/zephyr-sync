package lv.ctco.zephyr.transformer;

import lv.ctco.zephyr.beans.TestCase;

import java.util.List;

public interface ReportTransformer {

    String getType();

    List<TestCase> transformToTestCases(String reportPath);

}
