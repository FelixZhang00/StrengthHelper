package felix.example.strengthhelper.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class Practice extends BaseModel implements Serializable ,Cloneable{

	private String mTitle;
	private UUID mId;
	private int mTargetNum; // 目标个数
	private Date mStartDate;
	private Date mEndDate;
	private boolean mTitleEmpty;
	private ArrayList<PracticeSub> mPracticeSubs;

	private int mRemainder;



	public Practice() {
		mId = UUID.randomUUID();
		mPracticeSubs = new ArrayList<PracticeSub>();
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
    protected Object clone() throws CloneNotSupportedException {

        return super.clone();
    }

    @Override
	public String toString() {
		return mTitle;
	}

}
