package felix.example.strengthhelper.fragment;

import java.io.Serializable;
import java.util.Date;

import felix.example.strengthhelper.R;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Chronometer;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * 对话框的界面为listview，实现对话框多项选择的功能
 * 
 * @author tmac
 *
 */
public class ChoiseDialogFragment extends DialogFragment {

	public static final String EXTRA_DIALOG_TITLE = "felix.example.strengthhelper.fragment.PickerFragment.TITLE";
	public static final String EXTRA_DIALOG_DATE = "felix.example.strengthhelper.fragment.PickerFragment.DATE";
	protected static final String TAG = "ChoiseDialogFragment";
	protected static final String DIALOG_TAG_DATE = "date";
	protected static final String DIALOG_TAG_TIME = "time";

	private ListView mLv;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		final int titleId = (Integer) getArguments().getSerializable(
				EXTRA_DIALOG_TITLE);
		final Date date = (Date) getArguments().getSerializable(
				EXTRA_DIALOG_DATE);
		View view = getActivity().getLayoutInflater().inflate(
				R.layout.list_choise, null);
		mLv = (ListView) view.findViewById(R.id.lv_choise);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_list_item_1, new String[] {
						getActivity().getString(R.string.select_date),
						getActivity().getString(R.string.select_time) });

		mLv.setAdapter(adapter);
		mLv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (position == 0) {
					FragmentManager fm = getActivity()
							.getSupportFragmentManager();
					DatePickerFragment dialog = DatePickerFragment.newInstance(
							titleId, date);
					dialog.setTargetFragment(getTargetFragment(),
							PracticeFragment.REQUEST_DATE);
					dialog.show(fm, DIALOG_TAG_DATE);
				} else if (position == 1) {
					FragmentManager fm = getActivity()
							.getSupportFragmentManager();
					TimePickerFragment dialog = TimePickerFragment.newInstance(
							titleId, date);
					dialog.setTargetFragment(getTargetFragment(),
							PracticeFragment.REQUEST_TIME);
					dialog.show(fm, DIALOG_TAG_DATE);
				}
			}
		});

		return new AlertDialog.Builder(getActivity())
				.setTitle(R.string.dialog_select).setView(view).create();
	}

	public static ChoiseDialogFragment newInstance(int titleId, Date date) {
		Bundle args = new Bundle();
		args.putSerializable(EXTRA_DIALOG_TITLE, titleId);
		args.putSerializable(EXTRA_DIALOG_DATE, date);
		ChoiseDialogFragment fragment = new ChoiseDialogFragment();
		fragment.setArguments(args);
		return fragment;
	}

}
