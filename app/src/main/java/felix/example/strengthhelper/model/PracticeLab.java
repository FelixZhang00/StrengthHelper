package felix.example.strengthhelper.model;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import felix.example.strengthhelper.db.PracticeDatabaseHelper;
import felix.example.strengthhelper.utils.GsonUtils;
import felix.example.strengthhelper.utils.Logger;

public class PracticeLab extends BaseModel {
    private static final String TAG = "PracticeLab";
    private static final String FILENAME = "practice.json";
    private static PracticeLab sPracticeLab;
    private Context mAppContext;
    private ArrayList<Practice> mPractices;
    private PracticeJSONSerializer mSerializer;

    private PracticeDatabaseHelper mDBHelper;

    public static PracticeLab getInstance(Context context) {
        if (sPracticeLab == null) {
            sPracticeLab = new PracticeLab(context.getApplicationContext());
        }
        return sPracticeLab;
    }

    private PracticeLab(Context context) {
        mAppContext = context;
        mSerializer = new PracticeJSONSerializer(mAppContext, FILENAME);
        try {
            mPractices = mSerializer.loadPractices();
            Logger.i(TAG, "load practices succeed!");
        } catch (Exception e) {
            mPractices = new ArrayList<Practice>();
            Logger.i(TAG, "load practices failed!");
            e.printStackTrace();
        }
//        Logger.i(TAG,"准备生成数据库");
//        mDBHelper = new PracticeDatabaseHelper(mAppContext);
//        mDBHelper.queryPractices();

    }

    public boolean savePractice() {
        try {
            mSerializer.savePractices(mPractices);
            Logger.i(TAG, "save practices json succeed!");
            return true;
        } catch (Exception e) {
            Logger.i(TAG, "save practices failed!");
        }
        return false;

    }

    public void addPractice(Practice practice) {
        mPractices.add(practice);
    }

    public void deletePractice(Practice practice) {
        mPractices.remove(practice);
    }

    public Practice getPractice(UUID id) {
        for (Practice p : mPractices) {
            if (p.getId().equals(id)) {
                return p;
            }
        }
        return null;
    }

    /**
     * @param sour
     * @param dest TODO 检查还有没有其他属性
     */
    public void Copy(Practice sour, Practice dest) {
        dest.setId(sour.getId());
        dest.setTargetNum(sour.getTargetNum());
        dest.setStartDate(sour.getStartDate());
        dest.setEndDate(sour.getEndDate());
        dest.setTitle(sour.getTitle());
        dest.setTitleEmpty(sour.isTitleEmpty());
        dest.setPracticeSubs(sour.getPracticeSubs());
        dest.setRemainder(sour.getRemainder());
    }

    public ArrayList<Practice> getPractices() {
        return mPractices;
    }

    /**
     * FIXME 如果每次点击 查看历史记录 都要执行这段代码，势必影响效率，最好能同Practice对象一同保存到json数据中
     *
     * @param practice
     * @return
     */
    public ArrayList<PracticeGroup> switch2Group(Practice practice) {
        ArrayList<PracticeGroup> groups = new ArrayList<PracticeGroup>();
        ArrayList<PracticeSub> practiceSubs = practice.getPracticeSubs();
        if (practiceSubs == null || practiceSubs.size() == 0) {
            return groups;
        }

        PracticeSub tempSub = null;

        if (practiceSubs.size() == 1) {
            PracticeGroup group = new PracticeGroup();
            tempSub = practiceSubs.get(0);
            group.setDay(tempSub.getDate());
            group.addTotalNum(tempSub.getNum());
            group.getChilds().add(practiceSubs.get(0));
            groups.add(group);
            return groups;
        }

        PracticeGroup group = null;
        for (int i = 0; i < practiceSubs.size(); i++) {
            if (i == 0) {
                group = new PracticeGroup();
                tempSub = practiceSubs.get(0);
                group.setDay(tempSub.getDate());
                group.addTotalNum(tempSub.getNum());
                group.getChilds().add(practiceSubs.get(i));
            } else {

                if (isDayEqual(tempSub.getDate(), practiceSubs.get(i).getDate())) {
                    group.setDay(practiceSubs.get(i).getDate());
                    group.addTotalNum(practiceSubs.get(i).getNum());
                    group.getChilds().add(practiceSubs.get(i));
                } else {
                    groups.add(group);
                    group = new PracticeGroup();
                    tempSub = practiceSubs.get(i);
                    group.setDay(practiceSubs.get(i).getDate());
                    group.addTotalNum(practiceSubs.get(i).getNum());
                    group.getChilds().add(practiceSubs.get(i));
                }
            }

        }
        groups.add(group);

        return groups;
    }

    public int findPracticeSubIndex(int groupIndex, int childIndex,
                                    ArrayList<PracticeGroup> groups) {
        int index = 0;
        for (int i = 0; i < groupIndex; i++) { // 累加所选组之前的的个数和
            index += groups.get(i).getChilds().size();
        }
        for (int j = 0; j < childIndex; j++) { // 组内累加
            index++;
        }
        return index;
    }

    /**
     * 判断两个时间的日期是否相同
     *
     * @param date
     * @param date2
     * @return
     */
    private boolean isDayEqual(Date date, Date date2) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String strdate1 = dateFormat.format(date);
        String strdate2 = dateFormat.format(date2);

        return strdate1.equals(strdate2);
    }

    /**
     * 删除某Practice中的某条PracticeSub记录
     */
    public void removePracticeSub(UUID id, int index) {
        Practice practice = getPractice(id);
        int remainder = practice.getPracticeSubs().get(index).getNum();
        practice.getPracticeSubs().remove(index);
        practice.addRemainder(remainder);
    }

    private class PracticeJSONSerializer {

        private Context mContext;
        private String mFilename;

        public PracticeJSONSerializer(Context context, String filename) {
            mContext = context;
            mFilename = filename;
        }

        private void savePractices(ArrayList<Practice> practices)
                throws JSONException, IOException {
            // Build an string in JSON
            String json = GsonUtils.createGsonString(practices);

            // Write the file to disk
            Writer writer = null;
            try {
                OutputStream out = mContext.openFileOutput(mFilename,
                        Context.MODE_PRIVATE);
                writer = new OutputStreamWriter(out);
                writer.write(json.toString());
            } finally {
                if (writer != null) {
                    writer.close();
                }
            }
        }

        private ArrayList<Practice> loadPractices() throws IOException,
                JSONException {
            ArrayList<Practice> practices = new ArrayList<Practice>();
            BufferedReader reader = null;
            try {
                // Open and read the file into a StringBuilder
                InputStream in = mContext.openFileInput(mFilename);
                reader = new BufferedReader(new InputStreamReader(in));

                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }

                // Parse the JSON using JSONTokener
                Gson gson = new Gson();
                practices = gson.fromJson(sb.toString(), new TypeToken<ArrayList<Practice>>() {
                }.getType());


            } catch (FileNotFoundException e) {
                // 捕捉当没有文件时的异常，不向外抛出
            } finally {
                if (reader != null) {
                    reader.close();
                }
            }
            return practices;
        }

    }

}
