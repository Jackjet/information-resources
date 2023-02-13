package d1.project.dcrun.center.webapi.system.model.user;

/**
 * @author chengh
 */
public class SignInResult {
    String id;
    String token;
    String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
