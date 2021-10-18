package au.edu.curtin.assignment.mathematics.model;

import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import au.edu.curtin.assignment.mathematics.db.DB_HELPER;
import au.edu.curtin.assignment.mathematics.db.EMAIL;
import au.edu.curtin.assignment.mathematics.db.PHONE;
import au.edu.curtin.assignment.mathematics.db.StudentSchema;
import au.edu.curtin.assignment.mathematics.db.TESTHISTORY;

public class Databases {
    SQLiteDatabase database;
    public static Databases instance = null;
    private Context context;

    public static Databases getInstance()
    {
        if (instance ==null)
        {
            instance = new Databases();
        }
        return instance;
    }

    public Databases()
    {
    }

    public void addTestHistorytoDB(String refID, String Score, String Date, String Time)
    {
        ContentValues cv = new ContentValues();
        cv.put(TESTHISTORY.TEST_HISTORY.Cols.REFERENCE_KEY,refID);
        cv.put(TESTHISTORY.TEST_HISTORY.Cols.SCORE,Score);
        cv.put(TESTHISTORY.TEST_HISTORY.Cols.DATE,Date);
        cv.put(TESTHISTORY.TEST_HISTORY.Cols.TIME,Time);

        database.insert(TESTHISTORY.TEST_HISTORY.DBNAME,null,cv);
    }

    public List<TestHistory> loadTest()
    {
        List<TestHistory> results = new ArrayList<>();

        TestHistoryCursor testHistoryCursor = new TestHistoryCursor(
                this.database.query(TESTHISTORY.TEST_HISTORY.DBNAME,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null)
        );
        try{
            testHistoryCursor.moveToFirst();
            while (!testHistoryCursor.isAfterLast())
            {
                results.add(testHistoryCursor.getTestHistory());
                testHistoryCursor.moveToNext();
            }
        }finally {
            testHistoryCursor.close();
        }

        return results;
    }

    public List<TestHistory> findStudentTestHistory(String ID)
    {
        List<TestHistory> testHistoryList = new ArrayList<>();
        TestHistoryCursor testHistoryCursor= new TestHistoryCursor(
                this.database.query(TESTHISTORY.TEST_HISTORY.DBNAME,
                null,
                TESTHISTORY.TEST_HISTORY.Cols.REFERENCE_KEY+" = ?",
                new String[]{ID},
                null,
                null,
                null));

        try{
            testHistoryCursor.moveToFirst();
            while (!testHistoryCursor.isAfterLast())
            {
                testHistoryList.add(testHistoryCursor.getTestHistory());
                testHistoryCursor.moveToNext();
            }
        }finally {
            testHistoryCursor.close();
        }

        return testHistoryList;
    }

    public Student findStudentWithID(String ID)
    {
        Student resultStudent = null;
        StudentCursor studentCursor = new StudentCursor(
                this.database.query(StudentSchema.StudentTable.DBNAME,
                        null,
                        StudentSchema.StudentTable.Cols.ID+" = ?",
                        new String[]{ID},
                        null,
                        null,
                        StudentSchema.StudentTable.Cols.FIRST_NAME+" ASC")
        );
        try {
            studentCursor.moveToFirst();
            if (studentCursor.getCount() == 1)
            {
                resultStudent = studentCursor.getStudent();
                resultStudent.setEmailList(getEmail(ID));
                resultStudent.setPhoneNumberList(phoneList(ID));
            }
        }finally {
            studentCursor.close();
        }
        return resultStudent;
    }

    public void createDatabase(Context context)
    {
        this.database=new DB_HELPER(context.getApplicationContext()).getWritableDatabase();
    }


    public void addStudenttoDatabase(Student e,Context context)
    {
        ContentValues newStudent = new ContentValues();
        newStudent.put(StudentSchema.StudentTable.Cols.ID,e.getID());
        newStudent.put(StudentSchema.StudentTable.Cols.FIRST_NAME,e.getFirstName());
        newStudent.put(StudentSchema.StudentTable.Cols.LAST_NAME,e.getLastName());
        newStudent.put(StudentSchema.StudentTable.Cols.IMAGE,e.getID()+".jpg");

        if (e.getEmailList().size()>0)
        {
            addEmailtoDatabase(e.getEmailList(), e.getID());
        }
        if (e.getPhoneNumberList().size()>0)
        {
            addPhonetoDatabase(e.getPhoneNumberList(),e.getID());
        }
        saveImage(e.getStudentImage(),e.getID()+".jpg",context);


        database.insert(StudentSchema.StudentTable.DBNAME,null,newStudent);
    }

    private void addEmailtoDatabase(List<String> email, String ID)
    {
        ContentValues emails = new ContentValues();
        for (int i=0; i<email.size();i++)
        {
            emails.put(EMAIL.EMAIL_LIST.Cols.REFERENCE_KEY,ID);
            emails.put(EMAIL.EMAIL_LIST.Cols.EMAIL,email.get(i));
            database.insert(EMAIL.EMAIL_LIST.DBNAME,null,emails);
        }

    }

    private void addPhonetoDatabase(List<String> phoneList, String ID)
    {
        ContentValues phone = new ContentValues();
        for (int i=0; i<phoneList.size();i++)
        {
            phone.put(PHONE.PHONE_LIST.Cols.REFERENCE_KEY,ID);
            phone.put(PHONE.PHONE_LIST.Cols.PHONE_NUMBER,phoneList.get(i));
            database.insert(PHONE.PHONE_LIST.DBNAME,null,phone);
        }
    }

    public void editStudent(Student e, Context context)
    {
        ContentValues newStudent = new ContentValues();
        newStudent.put(StudentSchema.StudentTable.Cols.FIRST_NAME,e.getFirstName());
        newStudent.put(StudentSchema.StudentTable.Cols.LAST_NAME,e.getLastName());
        newStudent.put(StudentSchema.StudentTable.Cols.IMAGE,e.getID()+".jpg");

        //remove all entry of phonenumber and email
        removeStudentPhoneAndEmail(new String[]{e.getID()});
        //add them back in
        addPhonetoDatabase(e.getPhoneNumberList(),e.getID());
        addEmailtoDatabase(e.getEmailList(),e.getID());

        String [] whereValue = new String[]{e.getID()};
        database.update(StudentSchema.StudentTable.DBNAME,newStudent, StudentSchema.StudentTable.Cols.ID + " = ?",whereValue);

        saveImage(e.getStudentImage(),e.getID()+".jpg",context);
    }

    public void removeStudent(String uniqueID){
        String [] whereValue = {uniqueID};
        database.delete(StudentSchema.StudentTable.DBNAME,StudentSchema.StudentTable.Cols.ID+" = ?",whereValue);
        //we also need to remove all the phone number and email
        removeStudentPhoneAndEmail(whereValue);
    }

    public void removeStudentPhoneAndEmail(String [] whereValue)
    {
        database.delete(EMAIL.EMAIL_LIST.DBNAME, EMAIL.EMAIL_LIST.Cols.REFERENCE_KEY+" = ?",whereValue);
        database.delete(PHONE.PHONE_LIST.DBNAME, PHONE.PHONE_LIST.Cols.REFERENCE_KEY+" = ?",whereValue);
    }

    public List<Student> loadStudentObject()
    {
        List<Student> studentList = new ArrayList<>();
        if (!studentList.isEmpty())
        {
            studentList.clear();
        }
        StudentCursor studentCursor = new StudentCursor(
                this.database.query(StudentSchema.StudentTable.DBNAME,
                        null,
                        null,
                        null,
                        null,
                        null,
                        StudentSchema.StudentTable.Cols.FIRST_NAME+" ASC")
        );
        try{
            studentCursor.moveToFirst();
            while(!studentCursor.isAfterLast())
            {
                Student student = studentCursor.getStudent();
                student.setEmailList(getEmail(student.getID()));
                student.setPhoneNumberList(phoneList(student.getID()));
                studentList.add(student);
                studentCursor.moveToNext();
            }
        }finally {
            studentCursor.close();
        }

        return studentList;
    }

    private List<String> phoneList(String studentID)
    {
        List<String> phoneList = new ArrayList<>();
        if (!phoneList.isEmpty()){
            phoneList.clear();
        }

        PHONECursor phoneCursor = new PHONECursor(
                this.database.query(PHONE.PHONE_LIST.DBNAME,
                        null,
                        PHONE.PHONE_LIST.Cols.REFERENCE_KEY +" = ?",
                        new String[]{studentID},
                        null,
                        null,
                        null)
        );
        try{
            phoneCursor.moveToFirst();
            while (!phoneCursor.isAfterLast())
            {
                phoneList.add(phoneCursor.getPhone());
                phoneCursor.moveToNext();
            }
        }finally {
            phoneCursor.close();
        }
        return phoneList;
    }

    private List<String> getEmail(String studentID)
    {
        List<String> emailList = new ArrayList<>();
        if (!emailList.isEmpty())
        {
            emailList.clear();
        }
        EMAILCursor emailCursor = new EMAILCursor(
                this.database.query(EMAIL.EMAIL_LIST.DBNAME,
                        null,
                        EMAIL.EMAIL_LIST.Cols.REFERENCE_KEY +" = ?",
                        new String[]{studentID},
                        null,
                        null,
                        null)
        );
        try{
            emailCursor.moveToFirst();
            while (!emailCursor.isAfterLast())
            {
                emailList.add(emailCursor.getEmail());
                emailCursor.moveToNext();
            }
        }finally {
            emailCursor.close();
        }

        return emailList;
    }

    private void saveImage(Bitmap Image, String ID,Context context)
    {
        ContextWrapper contextWrapper = new ContextWrapper(context);
        File studentImageDirectory = contextWrapper.getDir("studentImageDirectory",Context.MODE_PRIVATE);

        File pixbayPath = new File(studentImageDirectory,ID);
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(pixbayPath);
            Image.compress(Bitmap.CompressFormat.PNG,100,fileOutputStream);
        }catch (FileNotFoundException ex)
        {
            ex.printStackTrace();
        }finally {
            //close the file output strema
            try {
                fileOutputStream.close();
            }catch (IOException ex)
            {
                ex.printStackTrace();
            }
        }
    }

    private class StudentCursor extends CursorWrapper{
        public StudentCursor (Cursor newCursor)
        {
            super(newCursor);
        }

        public Student getStudent()
        {
            String ID = getString(getColumnIndex(StudentSchema.StudentTable.Cols.ID));
            String firstName = getString(getColumnIndex(StudentSchema.StudentTable.Cols.FIRST_NAME));
            String lastName =  getString(getColumnIndex(StudentSchema.StudentTable.Cols.LAST_NAME));
            String ImageName = getString(getColumnIndex(StudentSchema.StudentTable.Cols.IMAGE));

            return new Student(ID,firstName,lastName,ImageName);
        }
    }

    private class EMAILCursor extends CursorWrapper{
        public EMAILCursor (Cursor cursor)
        {
            super(cursor);
        }

        public String getEmail()
        {
            return getString(getColumnIndex(EMAIL.EMAIL_LIST.Cols.EMAIL));
        }
    }

    private class PHONECursor extends CursorWrapper{
        public PHONECursor (Cursor cursor)
        {
            super(cursor);
        }

        public String getPhone()
        {
            return getString(getColumnIndex(PHONE.PHONE_LIST.Cols.PHONE_NUMBER));
        }
    }

    private class TestHistoryCursor extends CursorWrapper{
        public TestHistoryCursor(Cursor cursor)
        {
            super(cursor);
        }

        public TestHistory getTestHistory()
        {
            String ref = getString(getColumnIndex(TESTHISTORY.TEST_HISTORY.Cols.REFERENCE_KEY));
            String date = getString(getColumnIndex(TESTHISTORY.TEST_HISTORY.Cols.DATE));
            String time = getString(getColumnIndex(TESTHISTORY.TEST_HISTORY.Cols.TIME));
            String results = getString(getColumnIndex(TESTHISTORY.TEST_HISTORY.Cols.SCORE));

            return new TestHistory(ref,Integer.parseInt(results),date,time);
        }
    }

}
