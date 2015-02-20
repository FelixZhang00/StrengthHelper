package felix.example.strengthhelper.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import felix.example.strengthhelper.R;
import felix.example.strengthhelper.fragment.PracticeFragment;
import felix.example.strengthhelper.fragment.PracticeListFragment;
import felix.example.strengthhelper.model.Practice;

public class PracticeListActivity extends SingleFragmentActivity implements
		PracticeListFragment.Callbacks, PracticeFragment.Callbacks {

	@Override
	protected int getLayoutResId() {

		return R.layout.activity_masterdetail;
	}

	@Override
	protected Fragment createFragment() {
		return new PracticeListFragment();
	}


	@Override
	public  void onPracticeSelected(Practice practice, int model,int from) {
		if (findViewById(R.id.detailFragmentContainer) == null) {
			// Intent intent = new Intent(this, PracticePageActivity.class);
			Intent intent = new Intent(this, PracticeActivity.class);

			intent.putExtra(PracticeFragment.EXTRA_PRACTICE_ID,
					practice.getId());
			intent.putExtra(PracticeFragment.MODEL, model);
			intent.putExtra(PracticeFragment.FROM, from);
			startActivityForResult(intent, 0);

		} else {
			// 为平板准备的，暂不支持查看模式和编辑模式的切换
			Bundle bundle = new Bundle();
			bundle.putSerializable(PracticeFragment.EXTRA_PRACTICE_ID,
					practice.getId());
			PracticeFragment detailFragment = new PracticeFragment();
			detailFragment.setArguments(bundle);
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.detailFragmentContainer, detailFragment)
					.commit();

		}

	}



	@Override
	public void onPracticeUpdated() {
		FragmentManager fm = getSupportFragmentManager();
		PracticeListFragment fragment = (PracticeListFragment) fm
				.findFragmentById(R.id.fragmentContainer);
		fragment.updateListViewUI();

	}

}
