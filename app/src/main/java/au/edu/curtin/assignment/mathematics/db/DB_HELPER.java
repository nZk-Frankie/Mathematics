package au.edu.curtin.assignment.mathematics.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DB_HELPER extends SQLiteOpenHelper {
    private static final int VERSION =1;
    private static final String DATABASE_NAME="MATHEMATICS.DB";

    public DB_HELPER(Context context)
    {
        super(context,DATABASE_NAME, null,VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {
        sqLiteDatabase.execSQL("CREATE TABLE "+ StudentSchema.StudentTable.DBNAME + "(" +
                StudentSchema.StudentTable.Cols.ID + " TEXT," +
                StudentSchema.StudentTable.Cols.FIRST_NAME + " TEXT," +
                StudentSchema.StudentTable.Cols.LAST_NAME+ " TEXT," +
                StudentSchema.StudentTable.Cols.IMAGE+ " TEXT)");

        sqLiteDatabase.execSQL("CREATE TABLE "+ EMAIL.EMAIL_LIST.DBNAME + "(" +
                EMAIL.EMAIL_LIST.Cols.REFERENCE_KEY + " TEXT, " +
                EMAIL.EMAIL_LIST.Cols.EMAIL + " TEXT)");

        sqLiteDatabase.execSQL("CREATE TABLE "+ PHONE.PHONE_LIST.DBNAME+ "(" +
                PHONE.PHONE_LIST.Cols.REFERENCE_KEY + " TEXT, " +
                PHONE.PHONE_LIST.Cols.PHONE_NUMBER + " TEXT)");

        sqLiteDatabase.execSQL("CREATE TABLE "+ TESTHISTORY.TEST_HISTORY.DBNAME+ "(" +
                TESTHISTORY.TEST_HISTORY.Cols.REFERENCE_KEY + " TEXT, " +
                TESTHISTORY.TEST_HISTORY.Cols.SCORE + " TEXT, "+
                TESTHISTORY.TEST_HISTORY.Cols.DATE + " TEXT," +
                TESTHISTORY.TEST_HISTORY.Cols.TIME + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

}
