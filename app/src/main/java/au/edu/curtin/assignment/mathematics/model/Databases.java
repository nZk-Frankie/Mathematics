package au.edu.curtin.assignment.mathematics.model;

import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import au.edu.curtin.assignment.mathematics.db.DB_HELPER;
import au.edu.curtin.assignment.mathematics.db.EMAIL;
import au.edu.curtin.assignment.mathematics.db.PHONE;
import au.edu.curtin.assignment.mathematics.db.StudentSchema;

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
}
