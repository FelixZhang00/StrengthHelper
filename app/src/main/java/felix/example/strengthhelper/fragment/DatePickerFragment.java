package felix.example.strengthhelper.fragment;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import felix.example.strengthhelper.R;
import felix.example.strengthhelper.utils.Logger;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;

public class DatePickerFragment extends DialogFragment {

	public static final String EXTRA_DIALOG_TITLE = "felix.example.strengthhelper.fragment.PickerFragment.TITLE";
	public static final String EXTRA_PRACTICE_DATE = "felix.example.strengthhelper.fragment.PickerFragment.DATE";
	private static final String TAG = "DatePickerFragment";
	private Date mDate;
	private int mTitleId;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		mTitleId = (Integer) getArguments().getSerializable(EXTRA_DIALOG_TITLE);
		mDate = (Date) getArguments().getSerializable(EXTRA_PRACTICE_DATE);

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(mDate);
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);

		View view = getActivity().getLayoutInflater().inflate(
				R.layout.dialog_date, null);

		DatePicker datePicker = (DatePicker) view.findViewById(R.id.datePicker);
		datePicker.init(year, month, day,
				new DatePicker.OnDateChangedListener() {

					@Override
					public void onDateChanged(DatePicker view, int year,
							int monthOfYear, int dayOfMonth) {
						mDate = new GregorianCalendar(year, monthOfYear,
								dayOfMonth).getTime();
						getArguments().putSerializable(EXTRA_PRACTICE_DATE,
								mDate);
					}
				});

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

	public static DatePickerFragment newInstance(int titleId, Date date) {
		Bundle args = new Bundle();
		args.putSerializable(EXTRA_DIALOG_TITLE, titleId);
		args.putSerializable(EXTRA_PRACTICE_DATE, date);
		DatePickerFragment fragment = new DatePickerFragment();
		fragment.setArguments(args);
		return fragment;
	}

	private void sendResult(int resultOk) {
		if (getTargetFragment() == null) {
			return;
		}

		Intent i = new Intent();
		i.putExtra(EXTRA_DIALOG_TITLE, mTitleId);
		i.putExtra(EXTRA_PRACTICE_DATE, mDate);

		getTargetFragment().onActivityResult(getTargetRequestCode(), resultOk,
				i);
	}

}
