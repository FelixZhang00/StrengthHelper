package felix.example.strengthhelper.fragment.dialog;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import felix.example.strengthhelper.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.TimePicker;

public class TimePickerFragment extends DialogFragment implements
		TimePicker.OnTimeChangedListener {

	public static final String EXTRA_DIALOG_TITLE = "felix.example.strengthhelper.fragment.PickerFragment.TITLE";
	public static final String EXTRA_PRACTICE_TIME = "felix.example.strengthhelper.fragment.PickerFragment.TIME";
	private static final String TAG = "TimePickerFragment";
	private Date mDate;
	private int mTitleId;
	private int mYear;
	private int mMonth;
	private int mDay;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		mTitleId = (Integer) getArguments().getSerializable(EXTRA_DIALOG_TITLE);
		mDate = (Date) getArguments().getSerializable(EXTRA_PRACTICE_TIME);

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(mDate);
		mYear = calendar.get(Calendar.YEAR);
		mMonth = calendar.get(Calendar.MONTH);
		mDay = calendar.get(Calendar.DAY_OF_MONTH);

		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int min = calendar.get(Calendar.MINUTE);
		View view = getActivity().getLayoutInflater().inflate(
				R.layout.dialog_time, null);

		TimePicker timePicker = (TimePicker) view.findViewById(R.id.timePicker);
		timePicker.setIs24HourView(true);
		timePicker.setCurrentHour(hour);
		timePicker.setCurrentMinute(min);
		timePicker.setOnTimeChangedListener(this);
		return new AlertDialog.Builder(getActivity())
				.setTitle(mTitleId)
				.setView(view)
				.setPositiveButton(R.string.confirm,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								sendResult(Activity.RESULT_OK);
							}

						}).create();
	}

	public static TimePickerFragment newInstance(int titleId, Date date) {
		Bundle args = new Bundle();
		args.putSerializable(EXTRA_DIALOG_TITLE, titleId);
		args.putSerializable(EXTRA_PRACTICE_TIME, date);
		TimePickerFragment fragment = new TimePickerFragment();
		fragment.setArguments(args);
		return fragment;
	}

	private void sendResult(int resultOk) {
		if (getTargetFragment() == null) {
			return;
		}

		Intent i = new Intent();
		i.putExtra(EXTRA_DIALOG_TITLE, mTitleId);
		i.putExtra(EXTRA_PRACTICE_TIME, mDate);

		getTargetFragment().onActivityResult(getTargetRequestCode(), resultOk,
				i);
	}

	@Override
	public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
		// Logger.i(TAG, "hourOfDay:"+hourOfDay+"  minute:"+minute);
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(mDate);

		calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
		calendar.set(Calendar.MINUTE, minute);
		mDate = calendar.getTime();

	}

}
