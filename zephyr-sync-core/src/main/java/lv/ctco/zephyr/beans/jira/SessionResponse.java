package lv.ctco.zephyr.beans.jira;

import java.util.Map;

public class SessionResponse {

    Map<String, String> session;
    Map<String, String> loginInfo;

    public Map<String, String> getSession() {
        return session;
    }

    public void setSession(Map<String, String> session) {
        this.session = session;
    }

    public Map<String, String> getLoginInfo() {
        return loginInfo;
    }

    public void setLoginInfo(Map<String, String> loginInfo) {
        this.loginInfo = loginInfo;
    }
}
