package felix.example.strengthhelper.fragment;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import felix.example.strengthhelper.R;
import felix.example.strengthhelper.activity.PracticeActivity;
import felix.example.strengthhelper.activity.PracticeSubActivity;
import felix.example.strengthhelper.fragment.dialog.ChoiseDialogFragment;
import felix.example.strengthhelper.fragment.dialog.DatePickerFragment;
import felix.example.strengthhelper.fragment.dialog.PracticeSubDialog;
import felix.example.strengthhelper.fragment.dialog.TimePickerFragment;
import felix.example.strengthhelper.model.Practice;
import felix.example.strengthhelper.model.PracticeLab;
import felix.example.strengthhelper.utils.Logger;

public class PracticeFragment extends Fragment implements
        PracticeActivity.CallBacks {
    private static final String TAG = "PracticeFragment";
    public static final String EXTRA_PRACTICE_ID = "felix.example.strengthhelper.practice_id";
    public static final String MODEL = "felix.example.strengthhelper.practicefragment_model";
    public static final String FROM = "felix.example.strengthhelper.practicefragment_from"; // 以什么方式打开的此fragment
    public static final String DATE_FORMAT = "yyyy-MM-dd EEE HH:mm"; //显示的时间格式

    public static final int MODEL_SEE = 0;
    public static final int MODEL_EDIT = 1;
    public static final int FROM_ADD = 2;
    public static final int FROM_EDIT = 3;
    public static final int FROM_DONOT_CARE = -1;

    public static final int REQUEST_DATE = 4;
    public static final int REQUEST_TIME = 5;
    public static final int REQUEST_HISTORY = 8;
    protected static final int REQUEST_CHOICE = 6;
    protected static final int REQUEST_PRACTICESUB = 7;

    private Practice mPractice;
    private Practice mOldPractice; // 保存一开始mPractice对象的临时变量
    private Date mStartDate;
    private Date mEndDate;

    private int mModel = 0; // 当前界面显示的状态
    private int mFrom = 2;
    private boolean mViewEnable = true;


    private EditText mEtTitle;
    private EditText mEtTarget;
    private TextView mTvRemainder;
    private RelativeLayout mRLStart;
    private RelativeLayout mRLEnd;
    private TextView mTvStartTime;
    private TextView mTvEndTime;
    private Button mBtnGo;
    private Button mBtnSee;
    private TextView mTvRemainderLabel;
    private ProgressBar mPb;


    private Callbacks mCallbacks;

    public interface Callbacks {
        /**
         * 数据层改变后，就应该调用此方法更新UI等
         */
        void onPracticeUpdated();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        parseArguments();
    }

    /**
     * 解析接收到的参数
     */
    private void parseArguments() {
        if (getArguments() != null) {
            UUID practice_id = (UUID) getArguments().getSerializable(
                    EXTRA_PRACTICE_ID);
            mPractice = PracticeLab.getInstance(getActivity()).getPractice(
                    practice_id);

            mOldPractice = new Practice();
            PracticeLab.getInstance(getActivity())
                    .Copy(mPractice, mOldPractice);

            mModel = (Integer) getArguments().getSerializable(MODEL);
            mFrom = (Integer) getArguments().getSerializable(FROM);
            if (mModel == MODEL_SEE) {
                mViewEnable = false;
            }
        }
        Logger.i(TAG, "Fragment onCreate Model:" + mModel + "mPractice"
                + mPractice.getTitle());
    }

    public static PracticeFragment newInstance(UUID practiceID, int model,
                                               int from) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_PRACTICE_ID, practiceID);
        bundle.putSerializable(MODEL, model);
        bundle.putSerializable(FROM, from);

        PracticeFragment fragment = new PracticeFragment();
        fragment.setArguments(bundle);
        return fragment;

    }

    @SuppressLint("NewApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // 设置actionbar 左边的返回键
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            setActionbar();
        }


        View view = inflater.inflate(R.layout.fragment_practice, container,
                false);

        initView(view);

        return view;
    }

    private void initView(View view) {
        mStartDate = mPractice.getStartDate();
        mEndDate = mPractice.getEndDate();
        if (mStartDate == null) {
            mStartDate = new Date();
            mPractice.setStartDate(mStartDate);
        }
        if (mEndDate == null) {
            mEndDate = new Date();
            mPractice.setEndDate(mEndDate);
        }

        mEtTitle = (EditText) view.findViewById(R.id.et_practice_title);
        mEtTarget = (EditText) view.findViewById(R.id.et_practice_target);
        mTvRemainder = (TextView) view.findViewById(R.id.tv_remainder);
        mRLStart = (RelativeLayout) view.findViewById(R.id.rl_start);
        mRLEnd = (RelativeLayout) view.findViewById(R.id.rl_end);
        mTvStartTime = (TextView) view.findViewById(R.id.tv_start_time);
        mTvEndTime = (TextView) view.findViewById(R.id.tv_end_time);
        mBtnGo = (Button) view.findViewById(R.id.btn_go);
        mBtnSee = (Button) view.findViewById(R.id.btn_see);
        mTvRemainderLabel = (TextView) view
                .findViewById(R.id.tv_remainder_label);
        mPb = (ProgressBar) view.findViewById(R.id.pb_fragment_practice);


        // 根据模式决定是否可编辑
        mEtTitle.setEnabled(mViewEnable);
        mEtTarget.setEnabled(mViewEnable);
        mRLStart.setEnabled(mViewEnable);
        mRLEnd.setEnabled(mViewEnable);
        if (mViewEnable) {
            mBtnGo.setVisibility(View.GONE);
        }

        mEtTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                mPractice.setTitle(s.toString());
                // mCallbacks.onPracticeUpdated();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        mEtTitle.setText(mPractice.getTitle());

        mEtTarget.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (mViewEnable) {

                    if (s == null || "".equals(s.toString())) {
                        mPractice.setTargetNum(0);
                    } else {
                        mPractice.setTargetNum(Integer.parseInt(s.toString()));
                    }
                }
                // mCallbacks.onPracticeUpdated();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO 更科学地 计算 剩余的数量
                updateRemainder();

            }
        });
        if (0 != mPractice.getTargetNum()) {
            mEtTarget.setText(mPractice.getTargetNum() + "");
        }

        mRLStart.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                ChoiseDialogFragment fragment = ChoiseDialogFragment
                        .newInstance(R.string.startdate_label, mStartDate);
                fragment.setTargetFragment(PracticeFragment.this,
                        REQUEST_CHOICE);
                fragment.show(getFragmentManager(), "choice");

            }
        });
        mRLEnd.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                ChoiseDialogFragment fragment = ChoiseDialogFragment
                        .newInstance(R.string.enddate_label, mEndDate);
                fragment.setTargetFragment(PracticeFragment.this,
                        REQUEST_CHOICE);
                fragment.show(getFragmentManager(), "choice");
            }
        });

        mBtnGo.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                PracticeSubDialog dialog = PracticeSubDialog
                        .newInstance(mPractice.getId());
                dialog.setTargetFragment(PracticeFragment.this,
                        REQUEST_PRACTICESUB);
                dialog.show(getFragmentManager(), "practicesubdialog");
            }
        });
        mBtnSee.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PracticeSubActivity.class);
                intent.putExtra(PracticeSubActivity.EXTRA_PRACTICE_ID, mPractice.getId());
                getActivity().startActivityForResult(intent, REQUEST_HISTORY);
            }
        });

        updateProgressBar();
        updateDate();
    }

    private void setActionbar() {
        if (mModel == MODEL_EDIT) {
            Logger.i(TAG, "MODEL_EDIT");
            getActivity().getActionBar().setDisplayHomeAsUpEnabled(false);
            getActivity().getActionBar().setDisplayShowCustomEnabled(true);

            LayoutParams layout = new LayoutParams(
                    LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
            getActivity().getActionBar().setCustomView(
                    onCreateActionView(null), layout);

            // 隐藏actionbar左边的图标
            getActivity().getActionBar().setIcon(
                    new ColorDrawable(getResources().getColor(
                            android.R.color.transparent)));

        } else if (mModel == MODEL_SEE) {
            Logger.i(TAG, "MODEL_SEE");
            if (NavUtils.getParentActivityName(getActivity()) != null) { // 只有当此activity设置了home才让左上角的返回键有效
                getActivity().getActionBar()
                        .setDisplayHomeAsUpEnabled(true);
            }
            // 设置当前的fragment拥有菜单
            setHasOptionsMenu(true);
        }
    }

    /**
     * 设置自定义的actionbar，并设置好监听事件
     *
     * @param forItem
     * @return
     */
    public View onCreateActionView(MenuItem forItem) {
        // Inflate the action view to be shown on the action bar.
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View view = layoutInflater.inflate(R.layout.actionbar_practice_edit,
                null);
        Button btnCancel = (Button) view.findViewById(R.id.btn_bar_cancel);
        btnCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showCancelDialog();
            }

        });
        Button btnConfirm = (Button) view.findViewById(R.id.btn_bar_confirm);
        btnConfirm.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mFrom == FROM_ADD) {
                    if (mPractice.getTitle() == null
                            || "".equals((mPractice.getTitle()))) {
                        PracticeLab.getInstance(getActivity()).deletePractice(
                                mPractice);
                    }
                } else if (mFrom == FROM_EDIT) {
                    if (mPractice.getTitle() == null
                            || "".equals((mPractice.getTitle()))) {
                        mPractice.setTitleEmpty(true);
                    }
                }
                getActivity().finish();

            }
        });

        return view;
    }

    /**
     * 更新时间
     */
    private void updateDate() {
        // 2015-01-30 周五 19:22
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        mTvStartTime.setText(dateFormat.format(mStartDate));
        mTvEndTime.setText(dateFormat.format(mEndDate));

    }

    /**
     * 更新剩余
     */
    private void updateRemainder() {

        int num = mPractice.getRemainder();
        if (num >= 0) {
            mTvRemainderLabel.setText(R.string.practice_remainder_label);
            mTvRemainder.setText(num + "");
            int color = getActivity().getResources().getColor(R.color.practice_num_normal);
            mTvRemainder.setTextColor(color);
        } else {
            mTvRemainderLabel.setText(R.string.practice_remainder_over_label);
            mTvRemainder.setText(-num + "");
            int color = getActivity().getResources().getColor(R.color.practice_num_over);
            mTvRemainder.setTextColor(color);
        }

    }

    private void updateProgressBar() {
        mPb.setMax(mPractice.getTargetNum());
        mPb.setProgress(mPractice.getTargetNum() - mPractice.getRemainder());
    }

    private void showCancelDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.dialog_cancel);
        builder.setPositiveButton(R.string.confirm,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 根据打开此activity的来源
                        if (mFrom == FROM_ADD) {
                            PracticeLab.getInstance(getActivity())
                                    .deletePractice(mPractice);
                        } else if (mFrom == FROM_EDIT) {
                            Logger.i(TAG, "取消编辑");
                            PracticeLab.getInstance(getActivity()).Copy(
                                    mOldPractice, mPractice);
                        }
                        getActivity().finish();

                    }
                });

        builder.setNegativeButton(R.string.cancel,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                });
        builder.create().show();
    }

    private void showDeleteDailog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.dialog_delete);
        builder.setPositiveButton(R.string.confirm,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 删除此practie并返回到上一个activity
                        PracticeLab.getInstance(getActivity()).deletePractice(
                                mPractice);
                        getActivity().finish();
                    }
                });

        builder.setNegativeButton(R.string.cancel,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                });
        builder.create().show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: // 系统提供的左上角的返回菜单
                // Toast.makeText(getActivity(), "android.R.id.home", 0).show();
                if (NavUtils.getParentActivityName(getActivity()) != null) {
                    NavUtils.navigateUpFromSameTask(getActivity());
                }

                return true;

            case R.id.menu_practice_delete:
                //  弹出对话框，提示是否删除
                showDeleteDailog();
                return true;
            case R.id.menu_practice_edit:
                Intent intent = new Intent(getActivity(), PracticeActivity.class);
                intent.putExtra(PracticeFragment.EXTRA_PRACTICE_ID,
                        mPractice.getId());
                intent.putExtra(PracticeFragment.MODEL, MODEL_EDIT);
                intent.putExtra(PracticeFragment.FROM, FROM_EDIT);
                startActivity(intent);
                getActivity().finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (mModel == 0) {
            inflater.inflate(R.menu.fragment_practice_model_see, menu);
        } else if (mModel == 1) {
            // inflater.inflate(R.menu.fragment_practice_model_edit, menu);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPause() {
        PracticeLab.getInstance(getActivity()).savePractice();
        super.onPause();
    }

    @Override
    public void onAttach(Activity activity) {
        // mCallbacks = (Callbacks) activity;

        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        // mCallbacks = null;

        super.onDetach();
    }

    /**
     * 返回键按下
     */
    @Override
    public boolean onKeyBackPressed() {
        if (mModel == MODEL_EDIT) {
            showCancelDialog();
            return false;
        }
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Logger.i(TAG, "onActivityResult");
        if (resultCode != Activity.RESULT_OK)
            return;
        if (requestCode == REQUEST_DATE) {
            int titleId = (Integer) data
                    .getSerializableExtra(DatePickerFragment.EXTRA_DIALOG_TITLE);
            Logger.i(TAG, "titleId=" + titleId);
            if (titleId == R.string.startdate_label) {
                Date startDate = (Date) data
                        .getSerializableExtra(DatePickerFragment.EXTRA_PRACTICE_DATE);
                mPractice.setStartDate(startDate);
                mStartDate = startDate;
                updateDate();
            } else if (titleId == R.string.enddate_label) {
                Date endDate = (Date) data
                        .getSerializableExtra(DatePickerFragment.EXTRA_PRACTICE_DATE);
                mPractice.setEndDate(endDate);
                mEndDate = endDate;
                updateDate();
            }
        }
        if (requestCode == REQUEST_TIME) {
            int titleId = (Integer) data
                    .getSerializableExtra(DatePickerFragment.EXTRA_DIALOG_TITLE);
            if (titleId == R.string.startdate_label) {
                Date startDate = (Date) data
                        .getSerializableExtra(TimePickerFragment.EXTRA_PRACTICE_TIME);
                mPractice.setStartDate(startDate);
                mStartDate = startDate;
                updateDate();
            } else if (titleId == R.string.enddate_label) {
                Date endDate = (Date) data
                        .getSerializableExtra(TimePickerFragment.EXTRA_PRACTICE_TIME);
                mPractice.setEndDate(endDate);
                mEndDate = endDate;
                updateDate();
            }

        }

        if (requestCode == REQUEST_PRACTICESUB) {
            updateRemainder();
            updateProgressBar();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void updateUI() {
        updateRemainder();
        updateProgressBar();

    }

}
