package lv.ctco.zephyr;

import lv.ctco.zephyr.enums.ConfigProperty;
import lv.ctco.zephyr.transformer.ReportTransformerFactory;

import java.util.List;

public class AutodetectReportType implements Config.Loader {
    public void execute(Config config) {
        List<String> supportedReportTransformers = ReportTransformerFactory.getInstance().getSupportedReportTransformers();
        if (supportedReportTransformers.size() == 1) {
            config.applyDefault(ConfigProperty.REPORT_TYPE, supportedReportTransformers.get(0));
        }
    }
}
