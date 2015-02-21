package felix.example.strengthhelper.model;

import com.google.gson.Gson;

/**
 * Created by felix on 15/2/21.
 * @deprecated no use
 */
public abstract class BaseModel {
    public String toJson() {
        return new Gson().toJson(this);
    }
}
