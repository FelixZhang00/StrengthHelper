package felix.example.strengthhelper.model;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

public class PracticeSub implements Serializable {
	private int mNum;
	private UUID mId;
	private Date mDate;

	private static final String JSON_ID = "id";
	private static final String JSON_NUM = "num";
	private static final String JSON_DATE = "date";

	public PracticeSub() {
		mId = UUID.randomUUID();

	}

	public PracticeSub(JSONObject jsonObject) throws JSONException {
		mId = UUID.fromString(jsonObject.getString(JSON_ID));
		if (jsonObject.has(JSON_DATE)) {
			mDate = new Date(jsonObject.getLong(JSON_DATE));
		}
		if (jsonObject.has(JSON_NUM)) {
			mNum = jsonObject.getInt(JSON_NUM);
		}

	}

	public JSONObject toJSON() throws JSONException {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put(JSON_ID, mId);
		jsonObject.put(JSON_DATE, mDate.getTime());
		jsonObject.put(JSON_NUM, mNum);

		return jsonObject;
	}

	public int getNum() {
		return mNum;
	}

	public void setNum(int num) {
		mNum = num;
	}

	public UUID getId() {
		return mId;
	}

	public void setId(UUID id) {
		mId = id;
	}

	public Date getDate() {
		return mDate;
	}

	public void setDate(Date date) {
		mDate = date;
	}

}
