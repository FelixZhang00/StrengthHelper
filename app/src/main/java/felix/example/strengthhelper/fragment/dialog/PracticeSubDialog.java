package felix.example.strengthhelper.fragment.dialog;

import java.util.Date;
import java.util.UUID;

import felix.example.strengthhelper.R;
import felix.example.strengthhelper.model.Practice;
import felix.example.strengthhelper.model.PracticeLab;
import felix.example.strengthhelper.model.PracticeSub;
import felix.example.strengthhelper.utils.Logger;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.EditText;

public class PracticeSubDialog extends DialogFragment {
	public static final String EXTRA_DIALOG_PRACTICE_ID = "felix.example.strengthhelper.fragment.dialog.PracticeSubDialog.PRACTICE";
	protected static final String TAG = "PracticeSubDialog";
	private EditText mEtNum;
	private Practice mPractice;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		UUID id = (UUID) getArguments().getSerializable(
				EXTRA_DIALOG_PRACTICE_ID);

		mPractice = PracticeLab.getInstance(getActivity()).getPractice(id);

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		View view = getActivity().getLayoutInflater().inflate(
				R.layout.dialog_practicesub, null);
		mEtNum = (EditText) view.findViewById(R.id.et_dialog_practicesub);

		builder.setTitle(R.string.practicesub_dialog_title);
		builder.setView(view);
		builder.setNegativeButton(R.string.cancel,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {

					}
				});
		builder.setPositiveButton(R.string.confirm,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						Logger.i(TAG, "onClick");
						String num = mEtNum.getText().toString();
						if (num != null && !"".equals(num)) {
							Date date = new Date();
							PracticeSub practiceSub = new PracticeSub();
							practiceSub.setDate(date);
							practiceSub.setNum(Integer.parseInt(num));
							mPractice.addPracticeSub(practiceSub);
							Logger.i(TAG, "target:" + mPractice.getTargetNum()
									+ "ramainder:" + mPractice.getRemainder());
							sendResult(Activity.RESULT_OK);
						}
					}

				
				});

		return builder.create();
	}

	public static PracticeSubDialog newInstance(UUID id) {
		Bundle args = new Bundle();
		args.putSerializable(EXTRA_DIALOG_PRACTICE_ID, id);
		PracticeSubDialog fragment = new PracticeSubDialog();
		fragment.setArguments(args);
		return fragment;
	}
	
	private void sendResult(int resultOk) {
		if(getTargetFragment()==null){
			return;
		}
		Intent i=new Intent();
		getTargetFragment().onActivityResult(getTargetRequestCode(), resultOk, i);
	}

}
