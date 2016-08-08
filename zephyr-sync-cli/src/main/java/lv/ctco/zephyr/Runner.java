package lv.ctco.zephyr;

import lv.ctco.zephyr.enums.ConfigProperty;
import lv.ctco.zephyr.transformer.ReportTransformerFactory;
import lv.ctco.zephyr.util.Utils;

public class Runner {

    public static void main(final String[] args) throws Exception {
        Utils.log("Supported report types: " + ReportTransformerFactory.getInstance().getSupportedReportTransformers());
        Config config = new Config(new CliConfigLoader(args));

        ZephyrSyncService service = new ZephyrSyncService(config);
        service.execute();
    }

    public static class CliConfigLoader implements Config.Loader {

        private String[] args;

        public CliConfigLoader(String[] args) {
            this.args = args;
        }

        public void execute(Config config) {
            for (String arg : args) {
                if (!arg.startsWith("--")) {
                    throw new ZephyrSyncException("Arguments should start with '--', e.g. --projectType=cucumber. Found: " + arg);
                }
                String[] keyValue = arg.split("=");
                String key = keyValue[0].substring(2);
                String value = keyValue[1];
                config.setProperty(ConfigProperty.findByName(key), value);
            }
        }
    }
}
