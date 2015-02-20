package felix.example.strengthhelper.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import java.util.UUID;

import felix.example.strengthhelper.fragment.PracticeSubFragment;

public class PracticeSubActivity extends SingleFragmentActivity {

	public static final String EXTRA_PRACTICE_ID = "felix.example.strengthhelper.activity.PracticeSubActivity.EXTRA_PRACTICE_ID";

	protected static final String TAG = "PracticeSubActivity";
	private Fragment mFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// 这样做，创建了对象，并非获得模型中的引用，所以在下面的删除操作不管用.应修改为获取id,在从模型中找出id所对应的Practice对象
		// mPractice = (Practice)
		// getIntent().getSerializableExtra(EXTRA_PRACTICE);
		UUID id = (UUID) getIntent().getSerializableExtra(EXTRA_PRACTICE_ID);
		mFragment = PracticeSubFragment.newInstance(id);
		super.onCreate(savedInstanceState);
	}

	@Override
	protected Fragment createFragment() {
		return mFragment;
	}

	

}
