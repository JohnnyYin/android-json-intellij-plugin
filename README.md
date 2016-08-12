# android-json-intellij-plugin
**主要用于使用Android原生的JsonObject来序列或反序列化Java实体对象。**

如：实体类`Account`: 

```java
public class Account {
    public String uid;
    public String username;
    public String email;
    public String gender;
    public transient int localId;
}
```

使用后，自动生成toJson和fromJson方法：

```java
import android.text.TextUtils;

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

    public static Account fromJson(String jsonStr) {
        if (TextUtils.isEmpty(jsonStr)) {
            return null;
        }
        Account m = new Account();
        try {
            JSONObject json = new JSONObject(jsonStr);
            m.uid = json.optString("uid");
            m.username = json.optString("username");
            m.email = json.optString("email");
            m.gender = json.optString("gender");
        } catch (JSONException e) {
            e.printStackTrace();
            m = null;
        }
        return m;
    }
}

```

# 安装
1. 下载[release](https://github.com/JohnnyYin/android-json-intellij-plugin/releases)包;
2. 安装：`Android Studio->Preferences->Plugins->Install plugin from disk...`