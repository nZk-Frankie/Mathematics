package au.edu.curtin.assignment.mathematics;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import au.edu.curtin.assignment.mathematics.db.DB_HELPER;
import au.edu.curtin.assignment.mathematics.model.Databases;

public class MainActivity extends AppCompatActivity {

    Button student, teacher;
    Databases databases;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //create the database

        student = (Button) findViewById(R.id.IAMSTUDENT);
        teacher = (Button) findViewById(R.id.IAMATEACHER);

        this.databases = Databases.getInstance();
        this.databases.createDatabase(this);


        student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        teacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent teacher = new Intent(MainActivity.this,TeacherLanding.class);
                startActivity(teacher);
            }
        });
}}