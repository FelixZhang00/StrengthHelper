package felix.example.strengthhelper.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class Practice implements Serializable {

	private String mTitle;
	private UUID mId;
	private int mTargetNum; // 目标个数
	private Date mStartDate;
	private Date mEndDate;
	private boolean mTitleEmpty;
	private ArrayList<PracticeSub> mPracticeSubs;

	private int mRemainder;

	private static final String JSON_ID = "id";
	private static final String JSON_TITLE = "title";
	private static final String JSON_TARGETNUM = "targetnum";
	private static final String JSON_STARTDATE = "startdate";
	private static final String JSON_ENDDATE = "enddate";
	private static final String JSON_PRACTICESUBS = "practicesub";
	private static final String JSON_REMAINDER = "remainder";

	public Practice() {
		mId = UUID.randomUUID();
		mPracticeSubs = new ArrayList<PracticeSub>();
	}

	public Practice(JSONObject jsonObject) throws JSONException {
		mPracticeSubs = new ArrayList<PracticeSub>();
		mId = UUID.fromString(jsonObject.getString(JSON_ID));
		if (jsonObject.has(JSON_STARTDATE)) {
			mStartDate = new Date(jsonObject.getLong(JSON_STARTDATE));
		}
		if (jsonObject.has(JSON_ENDDATE)) {
			mEndDate = new Date(jsonObject.getLong(JSON_ENDDATE));
		}
		if (jsonObject.has(JSON_TITLE)) {
			mTitle = jsonObject.getString(JSON_TITLE);
		}
		if (jsonObject.has(JSON_TARGETNUM)) {
			mTargetNum = Integer.parseInt(jsonObject.getString(JSON_TARGETNUM));
		}
		if (jsonObject.has(JSON_STARTDATE)) {
			mStartDate = new Date(jsonObject.getLong(JSON_STARTDATE));
		}
		if (jsonObject.has(JSON_ENDDATE)) {
			mEndDate = new Date(jsonObject.getLong(JSON_ENDDATE));
		}
		if (jsonObject.has(JSON_PRACTICESUBS)) {
			JSONArray array = (JSONArray) new JSONTokener(
					jsonObject.getString(JSON_PRACTICESUBS)).nextValue();
			// Build the array of crimes from JSONObjects
			for (int i = 0; i < array.length(); i++) {
				mPracticeSubs.add(new PracticeSub(array.getJSONObject(i)));
			}

		}
		if (jsonObject.has(JSON_REMAINDER)) {
			mRemainder = jsonObject.getInt(JSON_REMAINDER);
		}

	}

	public JSONObject toJSON() throws JSONException {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put(JSON_ID, mId);
		jsonObject.put(JSON_TITLE, mTitle);
		jsonObject.put(JSON_TARGETNUM, mTargetNum);
		jsonObject.put(JSON_STARTDATE, mStartDate.getTime());
		jsonObject.put(JSON_ENDDATE, mEndDate.getTime());
		if (mPracticeSubs != null && mPracticeSubs.size() > 0) {
			JSONArray array = new JSONArray();
			for (PracticeSub p : mPracticeSubs) {
				array.put(p.toJSON());
			}
			jsonObject.put(JSON_PRACTICESUBS, array);
		}
		jsonObject.put(JSON_REMAINDER, mRemainder);

		return jsonObject;
	}

	public String getTitle() {
		return mTitle;
	}

	public void setTitle(String title) {
		mTitle = title;
	}

	public UUID getId() {
		return mId;
	}

	public void setId(UUID id) {
		mId = id;
	}

	public int getTargetNum() {
		return mTargetNum;
	}

	public void setTargetNum(int targetNum) {
		mTargetNum = targetNum;
		mRemainder = mTargetNum;
	}

	public Date getStartDate() {
		return mStartDate;
	}

	public void setStartDate(Date startDate) {
		mStartDate = startDate;
	}

	public Date getEndDate() {
		return mEndDate;
	}

	public void setEndDate(Date endDate) {
		mEndDate = endDate;
	}

	public boolean isTitleEmpty() {
		return mTitleEmpty;
	}

	public void setTitleEmpty(boolean titleEmpty) {
		mTitleEmpty = titleEmpty;
	}

	public ArrayList<PracticeSub> getPracticeSubs() {
		return mPracticeSubs;
	}

	public void setPracticeSubs(ArrayList<PracticeSub> practiceSubs) {
		mPracticeSubs = practiceSubs;
	}

	public void addPracticeSub(PracticeSub practiceSub) {
		mPracticeSubs.add(practiceSub);
		mRemainder -= practiceSub.getNum();

	}

	public int getRemainder() {
		return mRemainder;
	}

	public void setRemainder(int remainder) {
		mRemainder = remainder;
	}

	public void addRemainder(int remainder) {
		mRemainder += remainder;
	}

	@Override
	public String toString() {
		return mTitle;
	}

}
