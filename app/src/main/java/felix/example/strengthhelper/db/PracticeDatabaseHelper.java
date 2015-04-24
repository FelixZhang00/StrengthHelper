package felix.example.strengthhelper.db;

import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import felix.example.strengthhelper.model.Practice;
import felix.example.strengthhelper.model.PracticeSub;
import felix.example.strengthhelper.utils.Logger;

/**
 * Created by felix on 15/4/24.
 */
public class PracticeDatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "PracticeDatabaseHelper";
    private static final String DB_NAME = "practices.sqlite";

    private static final int VERSION = 1;

    /**
     * practice表的列名 *****
     */
    private static final String TABLE_PRACTICE = "practice";
    private static final String COLUMN_PRACTICE_ID = "_id";
    private static final String COLUMN_PRACTICE_TITLE = "title";
    private static final String COLUMN_PRACTICE_TARGETNUM = "targetNum";
    private static final String COLUMN_PRACTICE_STARTDATE = "startDate";
    private static final String COLUMN_PRACTICE_ENDDATE = "endDate";
    private static final String COLUMN_PRACTICE_TITLEEMPTY = "titleEmpty";
    private static final String COLUMN_PRACTICE_REMAINDER = "remainder";


    /**
     * practice_sub表的列名 *****
     */
    private static final String TABLE_PRACTICESUB = "practice_sub";
    private static final String COLUMN_PRACTICESUB_ID = "_id";
    private static final String COLUMN_PRACTICESUB_NUM = "num";
    private static final String COLUMN_PRACTICESUB_DATE = "date";
    private static final String COLUMN_PRACTICESUB_PID = "pid";


    public PracticeDatabaseHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Logger.i(TAG, "db onCreate");
        String practice_sql = "CREATE TABLE \"practice\" (\n" +
                "\t \"_id\" text NOT NULL,\n" +
                "\t \"title\" TEXT,\n" +
                "\t \"targetNum\" INTEGER,\n" +
                "\t \"startDate\" TEXT,\n" +
                "\t \"endDate\" TEXT,\n" +
                "\t \"titleEmpty\" blob,\n" +
                "\t \"remainder\" integer,\n" +
                "\tPRIMARY KEY(\"_id\")\n" +
                ");";
        db.execSQL(practice_sql);


        String sub_sql = "CREATE TABLE \"practice_sub\" (\n" +
                "\t \"_id\" text NOT NULL,\n" +
                "\t \"num\" INTEGER,\n" +
                "\t \"date\" TEXT,\n" +
                "\t \"pid\" text,\n" +
                "\tPRIMARY KEY(\"_id\"),\n" +
                "\tFOREIGN KEY (\"pid\") REFERENCES \"practice\" (\"_id\")\n" +
                ");";
        db.execSQL(sub_sql);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

//    public long insertPractice(Practice practice){
//        ContentValues cv=new ContentValues();
//        cv.put();
//
//    }

    public PracticeCursor queryPractices() {
        // select * from run order by start_date asc
        Cursor wrapper = getReadableDatabase().query(TABLE_PRACTICE, null, null, null, null, null, COLUMN_PRACTICE_STARTDATE + " asc");
        return new PracticeCursor(wrapper);
    }


    public static class PracticeCursor extends CursorWrapper {

        /**
         * Creates a cursor wrapper.
         *
         * @param cursor The underlying cursor to wrap.
         */
        public PracticeCursor(Cursor cursor) {
            super(cursor);
        }

        public Practice getPractice() {
            if (isBeforeFirst() || isAfterLast()) {
                return null;
            }
            Practice practice = new Practice();

            return practice;
        }
    }

    public static class PracticeSubCursor extends CursorWrapper {

        /**
         * Creates a cursor wrapper.
         *
         * @param cursor The underlying cursor to wrap.
         */
        public PracticeSubCursor(Cursor cursor) {
            super(cursor);
        }

        public PracticeSub getPractice() {
            if (isBeforeFirst() || isAfterLast()) {
                return null;
            }
            PracticeSub sub = new PracticeSub();

            return sub;
        }
    }

}
