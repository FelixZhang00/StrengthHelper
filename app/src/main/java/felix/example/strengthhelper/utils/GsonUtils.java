package felix.example.strengthhelper.utils;

import com.google.gson.Gson;

public class GsonUtils {

	public static <T> T jsonToBean(String jsonResult, Class<T> clz) {
		Gson gson = new Gson();
		T t = gson.fromJson(jsonResult, clz);
		return t;
	}

	public static String createGsonString(Object object) {
		Gson gson = new Gson();
		String gsonString = gson.toJson(object);
		return gsonString;
	}

}
