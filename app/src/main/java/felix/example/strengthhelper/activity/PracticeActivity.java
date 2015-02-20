package felix.example.strengthhelper.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import java.util.ArrayList;
import java.util.UUID;

import felix.example.strengthhelper.fragment.PracticeFragment;
import felix.example.strengthhelper.model.Practice;
import felix.example.strengthhelper.model.PracticeLab;
import felix.example.strengthhelper.utils.Logger;

/**
 * @author tmac TODO 考虑取代PracticePageActivity
 */
public class PracticeActivity extends SingleFragmentActivity {

	private static final String TAG = "PracticeActivity";

	private ArrayList<Practice> mPractices;
	private Practice mPractice;
	private int mPos;
	private PracticeFragment mFragment;

	private CallBacks mCallBack;

	public interface CallBacks {
		/**
		 * @return true : 调用系统的返回
		 */
		boolean onKeyBackPressed();
		void updateUI();
	}

	@Override
	protected void onCreate(Bundle arg0) {

		mPractices = PracticeLab.getInstance(this).getPractices();
		final int model = (Integer) getIntent().getSerializableExtra(
				PracticeFragment.MODEL);
		int from = (Integer) getIntent().getSerializableExtra(
				PracticeFragment.FROM);

		UUID practiceId = (UUID) getIntent().getSerializableExtra(
				PracticeFragment.EXTRA_PRACTICE_ID);

		mFragment = PracticeFragment.newInstance(practiceId, model, from);

		super.onCreate(arg0);

	}

	@Override
	protected Fragment createFragment() {
		mCallBack = (CallBacks) mFragment;
		return mFragment;
	}

	// 按返回键后调用的方法
	@Override
	public void onBackPressed() {
		boolean b = mCallBack.onKeyBackPressed();
		if (b) {
			super.onBackPressed(); // 抢在调用父类方法之前
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode!=Activity.RESULT_OK){
			return;
		}
		Logger.i(TAG, "onActivityResult");
		mCallBack.updateUI();
		super.onActivityResult(requestCode, resultCode, data);
	}

}
