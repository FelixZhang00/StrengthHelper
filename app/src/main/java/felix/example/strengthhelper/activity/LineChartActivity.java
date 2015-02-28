package felix.example.strengthhelper.activity;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;

import java.util.UUID;

import felix.example.strengthhelper.R;
import felix.example.strengthhelper.fragment.LineChartFragment;
import felix.example.strengthhelper.model.Practice;
import felix.example.strengthhelper.model.PracticeLab;


/**
 * Created by felix on 15/2/28.
 * 查看历史折线图图表
 */
public class LineChartActivity extends FragmentActivity {

    private Practice mPractice;
    private Fragment mFirstFragment;
    private UUID mId;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_fragment);
        parseArgs();
        setActionbar();
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.history, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: // 系统提供的左上角的返回菜单
                // Toast.makeText(getActivity(), "android.R.id.home", 0).show();
                finish();
                return true;
            case R.id.menu_item_history_detail:
                Intent intent = new Intent(this, PracticeSubActivity.class);
                intent.putExtra(PracticeSubActivity.EXTRA_PRACTICE_ID, mPractice.getId());
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    /**
     * 解析参数
     */
    private void parseArgs() {
        mId = (UUID) getIntent().getSerializableExtra(PracticeSubActivity.EXTRA_PRACTICE_ID);
        mPractice = PracticeLab.getInstance(this).getPractice(mId);
    }

    private void setActionbar() {
        // 设置actionbar 左边的返回键
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {

            getActionBar().setDisplayHomeAsUpEnabled(true);
        }
        getActionBar().setTitle(getResources().getString(R.string.actionbar_chart));
        getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        ActionBar.OnNavigationListener mNavigationCallback = new ActionBar.OnNavigationListener() {
            @Override
            public boolean onNavigationItemSelected(int itemPosition, long itemId) {
                // Create new fragment from our own Fragment class
                LineChartFragment newFragment = LineChartFragment.newInstance(mId, (int) itemId);
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

                ft.replace(R.id.fragmentContainer, newFragment);

                // Apply changes
                ft.commit();
                return true;
            }
        };
        //下拉导航的适配器
        SpinnerAdapter mSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.action_list, R.layout.dropdown_item);
        getActionBar().setListNavigationCallbacks(mSpinnerAdapter, mNavigationCallback);
    }


}
