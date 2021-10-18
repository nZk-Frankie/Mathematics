package au.edu.curtin.assignment.mathematics;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;
import java.util.Locale;

import au.edu.curtin.assignment.mathematics.model.Databases;
import au.edu.curtin.assignment.mathematics.model.Student;

public class StudentLanding extends AppCompatActivity {

    Button back, proceed;
    Spinner studentSpinner;
    List<Student> studentList;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_choose);


        back =  (Button) findViewById(R.id.backButtonFromStudentLanding);
        proceed = (Button) findViewById(R.id.proceedButtonStudent);
        studentSpinner = (Spinner) findViewById(R.id.studentSpinnerObject);
        studentList = Databases.getInstance().loadStudentObject();

        SpinnerAdapter spinnerAdapter = new SpinnerAdapter(this,R.layout.spinner_student_name, studentList);
        studentSpinner.setAdapter(spinnerAdapter);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StudentLanding.this,MainActivity.class);
                startActivity(intent);
            }
        });

        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StudentLanding.this,StudentTestPage.class);
                if (studentList.size()>0) {
                    intent.putExtra("refID", studentList.get(studentSpinner.getSelectedItemPosition()).getID());
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Invalid Student", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private class SpinnerAdapter extends ArrayAdapter<Student>
    {
        private List<Student> studentList;
        private Context context;

        public SpinnerAdapter(@NonNull Context context, int resource, List<Student> studentList)
        {
            super(context,R.layout.spinner_student_name,R.id.studentSpinnerObject,studentList);
            this.studentList = studentList;
            this.context = context;
        }

        @Override
        public View getDropDownView(int position, View container, ViewGroup parent)
        {
            return makeCustomView(position,container,parent);
        }

        @Override
        public View getView(int position, View container, ViewGroup parent)
        {
            return makeCustomView(position,container,parent);

        }

        private View makeCustomView(int position, View container, ViewGroup parent)
        {
            LayoutInflater layoutInflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.spinner_student_name,parent,false);

            TextView sName = (TextView) row.findViewById(R.id.studentNameTextViewForSpinner);
            sName.setText(this.studentList.get(position).getFirstName()+" "+this.studentList.get(position).getLastName().toUpperCase(Locale.ROOT));

            return row;
        }
    }
}
