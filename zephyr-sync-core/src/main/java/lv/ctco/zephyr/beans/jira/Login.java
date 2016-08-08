package lv.ctco.zephyr.beans.jira;

public class Login {

    final String username;
    final String password;

    public Login(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
