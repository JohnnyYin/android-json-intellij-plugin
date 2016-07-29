# android-json-intellij-plugin


```java
public class Account {
    public String uid;
    public String username;
    public String email;
    public String gender;
    public transient int localId;
}
```
使用后，自动生成toJson和fromJson方法

```java
import org.json.JSONException;
import org.json.JSONObject;

public class Account {
    public String uid;
    public String username;
    public String email;
    public String gender;
    public transient int localId;

    public JSONObject toJson() {
        try {
            JSONObject json = new JSONObject();
            json.put("uid", uid);
            json.put("username", username);
            json.put("email", email);
            json.put("gender", gender);
            return json;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void fromJson(String jsonStr) {
        try {
            JSONObject json = new JSONObject(jsonStr);
            uid = json.optString("uid");
            username = json.optString("username");
            email = json.optString("email");
            gender = json.optString("gender");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

```