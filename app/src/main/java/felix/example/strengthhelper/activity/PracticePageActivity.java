package felix.example.strengthhelper.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;

import java.util.ArrayList;
import java.util.UUID;

import felix.example.strengthhelper.R;
import felix.example.strengthhelper.fragment.PracticeFragment;
import felix.example.strengthhelper.model.Practice;
import felix.example.strengthhelper.model.PracticeLab;

/**
 * @author tmac
 *@deprecated
 */
public class PracticePageActivity extends FragmentActivity implements
		PracticeFragment.Callbacks {
	private ViewPager mViewPager;
	private ArrayList<Practice> mPractices;
	private Practice mPractice;
	private int mPos;
	
	



	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		mViewPager = new ViewPager(this);
		mViewPager.setId(R.id.viewPager);
		setContentView(mViewPager);
		mPractices = PracticeLab.getInstance(this).getPractices();
		final int model = (Integer) getIntent().getSerializableExtra(
				PracticeFragment.MODEL);
		UUID practiceId = (UUID) getIntent().getSerializableExtra(
				PracticeFragment.EXTRA_PRACTICE_ID);
		for (int i = 0; i < mPractices.size(); i++) {
			mPractice = mPractices.get(i);
			if (mPractice.getId().equals(practiceId)) {
				mPos = i;
				break;
			}
		}
		FragmentManager fm = getSupportFragmentManager();
		mViewPager.setAdapter(new FragmentPagerAdapter(fm) {

			@Override
			public int getCount() {
				return mPractices.size();
			}

			@Override
			public Fragment getItem(int arg0) {
				// 给PracticeFragment指定具体的展示风格
				if (arg0 == mPos) {
					return PracticeFragment.newInstance(mPractices.get(arg0)
							.getId(), model,PracticeFragment.FROM_DONOT_CARE);
				} else {
					return PracticeFragment.newInstance(mPractices.get(arg0)
							.getId(), PracticeFragment.MODEL_SEE,PracticeFragment.FROM_DONOT_CARE);

				}
			}
		});
		mViewPager.setCurrentItem(mPos);
		// setTitle(mPractice.getTitle());

		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				String title = mPractices.get(arg0).getTitle();
				if (title != null) {
					// setTitle(title);

				} else {
					// setTitle("");
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});

	}

	@Override
	public void onPracticeUpdated() {
		// 因为PracticeListFragment界面被遮盖，不需要更新
	}

}
