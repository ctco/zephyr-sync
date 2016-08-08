package lv.ctco.zephyr.transformer;

import lv.ctco.zephyr.ZephyrSyncException;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

public class ReportTransformerFactory {

    private static final ReportTransformerFactory instance = new ReportTransformerFactory();
    public static ReportTransformerFactory getInstance() {
        return instance;
    }

    private final ServiceLoader<ReportTransformer> transformers;

    private ReportTransformerFactory() {
        transformers = ServiceLoader.load(ReportTransformer.class);
    }

    public List<String> getSupportedReportTransformers() {
        List<String> result = new ArrayList<String>();
        for (ReportTransformer transformer : transformers) {
            result.add(transformer.getType());
        }
        return result;
    }

    public ReportTransformer getTransformer(String reportType) {
        for (ReportTransformer transformer : transformers) {
            if (transformer.getType().equalsIgnoreCase(reportType)) {
                return transformer;
            }
        }
        throw new ZephyrSyncException("Report type " + reportType + " is not recognized!");
    }

}
