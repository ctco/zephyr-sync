package lv.ctco.zephyr;

public class ZephyrSyncException extends RuntimeException {

    public ZephyrSyncException(String message) {
        super(message);
    }

    public ZephyrSyncException(String message, Throwable cause) {
        super(message, cause);
    }
}
