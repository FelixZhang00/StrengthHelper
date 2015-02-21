package felix.example.strengthhelper.model;

import java.util.ArrayList;
import java.util.Date;

/**
 * @author tmac 用于向ExpandableListvew 展示数据
 */
public class PracticeGroup extends BaseModel {

	private Date mDay; // 只要精确到天的时间信息
	private int mTotalNum;
	private ArrayList<PracticeSub> mChilds = new ArrayList<PracticeSub>();

	public Date getDay() {
		return mDay;
	}

	public void setDay(Date day) {
		mDay = day;
	}

	public int getTotalNum() {
		return mTotalNum;
	}

	public void setTotalNum(int totalNum) {
		mTotalNum = totalNum;
	}

	public ArrayList<PracticeSub> getChilds() {
		return mChilds;
	}

	public void setChilds(ArrayList<PracticeSub> childs) {
		mChilds = childs;
	}

	public void addTotalNum(int num) {
		mTotalNum += num;

	}

}
