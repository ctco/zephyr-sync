package lv.ctco.zephyr.service;

import lv.ctco.zephyr.Config;
import lv.ctco.zephyr.beans.jira.Login;
import lv.ctco.zephyr.beans.jira.SessionResponse;
import lv.ctco.zephyr.util.HttpUtils;
import lv.ctco.zephyr.util.Utils;
import lv.ctco.zephyr.enums.ConfigProperty;
import lv.ctco.zephyr.util.ObjectTransformer;
import org.apache.http.HttpResponse;
import org.apache.http.impl.client.BasicCookieStore;

import java.io.IOException;

import static lv.ctco.zephyr.util.Utils.log;

public class AuthService {

    public static final BasicCookieStore COOKIE = new BasicCookieStore();
    private static String jSessionId;

    private Config config;

    public AuthService(Config config) {
        this.config = config;
    }

    public void authenticateInJira() throws IOException {
        if (jSessionId == null) {
            Login login = new Login(config.getValue(ConfigProperty.USERNAME), config.getValue(ConfigProperty.PASSWORD));

            HttpResponse response = HttpUtils.post(config, "auth/1/session", login);
            if (response.getStatusLine().getStatusCode() != 200) {
                log("ERROR: JIRA authentication failed: " + response.getStatusLine().getProtocolVersion() + " " + response.getStatusLine().getStatusCode() + " " + response.getStatusLine().getReasonPhrase());
            }
            SessionResponse loginResponse = ObjectTransformer.deserialize(Utils.readInputStream(response.getEntity().getContent()), SessionResponse.class);
            if (loginResponse != null) {
                jSessionId = loginResponse.getSession().get("value");
            }
        }
    }

}