package au.edu.curtin.assignment.mathematics;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import au.edu.curtin.assignment.mathematics.model.Databases;
import au.edu.curtin.assignment.mathematics.model.Student;

public class ViewStudent extends AppCompatActivity {


    Button back;
    RecyclerView studentViewList;
    List<Student> studentList = new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.student_view);

        back =(Button) findViewById(R.id.backfromStudentView);
        studentViewList = (RecyclerView) findViewById(R.id.studentViewRV);

        studentList = Databases.getInstance().loadStudentObject();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goBack = new Intent(ViewStudent.this,TeacherPage.class);
                startActivity(goBack);
            }
        });

        StudentAdapter studentAdapter = new StudentAdapter(studentList);
        studentViewList.setLayoutManager(new LinearLayoutManager(ViewStudent.this));
        studentViewList.setAdapter(studentAdapter);
    }

    private class StudentViewHolder extends RecyclerView.ViewHolder {
        TextView studentName;
        Button moreInfo, remove;

        public StudentViewHolder(View view)
        {
            super(view);

            studentName = (TextView) view.findViewById(R.id.studentNameTextView);
            moreInfo= (Button) view.findViewById(R.id.moreInfoButton);
            remove = (Button) view.findViewById(R.id.removeButtonEach);

        }

        public void bind(String item)
        {
            studentName.setText(item);
        }
    }

    private class StudentAdapter extends RecyclerView.Adapter<StudentViewHolder>
    {
        private List<Student> studentData;

        public StudentAdapter(List<Student> data)
        {
            this.studentData=  data;
        }

        @NonNull
        @Override
        public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup container, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(container.getContext());
            View view = layoutInflater.inflate(R.layout.student_view_each,container,false);
            StudentViewHolder itemHolder = new StudentViewHolder(view);

            return itemHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull StudentViewHolder holder, @SuppressLint("RecyclerView") int position) {
            holder.bind(studentData.get(position).getFirstName()+" "+studentData.get(position).getLastName());
            holder.moreInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent nextStage = new Intent(ViewStudent.this,StudentViewMore.class);
                    nextStage.putExtra("studentID",studentData.get(position).getID());
                    startActivity(nextStage);
                }
            });
            holder.remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Databases.getInstance().removeStudent(studentData.get(position).getID());
                    studentData.remove(position);
                    notifyItemChanged(position);
                    notifyItemRangeChanged(0,studentData.size());
                    notifyDataSetChanged();
                }
            });
        }

        @Override
        public int getItemCount() {
            return studentData.size();
        }
    }
}
