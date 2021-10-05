package au.edu.curtin.assignment.mathematics;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button student, teacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        student = (Button) findViewById(R.id.IAMSTUDENT);
        teacher = (Button) findViewById(R.id.IAMATEACHER);

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