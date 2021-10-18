package au.edu.curtin.assignment.mathematics;

import static java.net.Proxy.Type.HTTP;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import au.edu.curtin.assignment.mathematics.model.Databases;
import au.edu.curtin.assignment.mathematics.model.Student;
import au.edu.curtin.assignment.mathematics.model.TestHistory;

public class DataSend extends AppCompatActivity {


    Button back,send;
    RecyclerView recyclerView;
    List<Student> studentList = new ArrayList<>();
    List<TestHistory> studentTestHistory = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_result_data);

        back = (Button) findViewById(R.id.backButtonFromSend);
        send = (Button)findViewById(R.id.sendButton);
        recyclerView = (RecyclerView) findViewById(R.id.RVSendData);

        studentList = Databases.getInstance().loadStudentObject();

        StudentAdapter studentAdapter = new StudentAdapter(studentList);
        recyclerView.setLayoutManager(new LinearLayoutManager(DataSend.this));
        recyclerView.setAdapter(studentAdapter);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goBack = new Intent(DataSend.this,TeacherPage.class);
                startActivity(goBack);
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (generateStringFromArrayList().equals(""))
                {
                    Toast.makeText(getApplicationContext(), "You need to choose atleast 1 Student", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Intent intent = new Intent(Intent.ACTION_SENDTO);
                    intent.setData(Uri.parse("mailto:"));
                    intent.putExtra(Intent.EXTRA_EMAIL,"1948757@student.curtin.edu.au");
                    intent.putExtra(Intent.EXTRA_SUBJECT,"STUDENT TEST RESULTS DATA");
                    intent.putExtra(Intent.EXTRA_TEXT,generateStringFromArrayList());

                    startActivity(intent);
                }
            }
        });
    }

    private String generateStringFromArrayList()
    {
        String emailContent = "";
        for (TestHistory x: studentTestHistory)
        {
            emailContent = emailContent + x.toString() + "\n";
        }
        return emailContent;

    }

    private class StudentViewHolder extends RecyclerView.ViewHolder {
        TextView studentName;
        Button sendThisData, removeTheData;

        public StudentViewHolder(View view)
        {
            super(view);

            studentName = (TextView) view.findViewById(R.id.studentNameTextViewSendDATA);
            sendThisData = (Button) view.findViewById(R.id.sendButtonEachData);
            removeTheData =  (Button) view.findViewById(R.id.removeFromListButotn);

        }

        public void bind(String item)
        {
            if (item.equals(null))
            {
                Log.d("ITEM", "bind: NULL");
            }
            if (studentName==null)
            {
                Log.d("TEXTVIEW","NULL");
            }
            studentName.setText(item);
        }
    }

    private class StudentAdapter extends RecyclerView.Adapter<StudentViewHolder>
    {
        private List<Student> studentData;

        public StudentAdapter(List<Student> data)
        {
            this.studentData= data;
        }

        @NonNull
        @Override
        public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup container, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(container.getContext());
            View view = layoutInflater.inflate(R.layout.send_data_each,container,false);
            StudentViewHolder itemHolder = new StudentViewHolder(view);

            return itemHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull StudentViewHolder holder, @SuppressLint("RecyclerView") int position) {
            holder.bind(studentData.get(position).getFirstName()+" "+studentData.get(position).getLastName());
            holder.sendThisData.setClickable(true);
            holder.sendThisData.setPressed(false);

            holder.removeTheData.setClickable(false);
            holder.removeTheData.setPressed(true);

            holder.sendThisData.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    List<TestHistory> testHistoryList = new ArrayList<>();
                    testHistoryList = Databases.getInstance().findStudentTestHistory(studentData.get(position).getID());
                    for (TestHistory x: testHistoryList)
                    {
                        studentTestHistory.add(x);
                    }

                    holder.sendThisData.setClickable(false);
                    holder.sendThisData.setPressed(true);

                    holder.removeTheData.setClickable(true);
                    holder.removeTheData.setPressed(false);

                    Toast.makeText(getApplicationContext(), "Adding "+studentData.get(position).getFirstName(), Toast.LENGTH_SHORT).show();
                }
            });

            holder.removeTheData.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    List<TestHistory> testHistoryList = Databases.getInstance().findStudentTestHistory(studentData.get(position).getID());
                    for (TestHistory x: testHistoryList)
                    {
                        studentTestHistory.remove(x);
                    }
                    holder.sendThisData.setClickable(true);
                    holder.sendThisData.setPressed(false);

                    holder.removeTheData.setClickable(false);
                    holder.removeTheData.setPressed(true);
                    Toast.makeText(getApplicationContext(), "Removing "+studentData.get(position).getFirstName(), Toast.LENGTH_SHORT).show();

                }
            });
        }

        @Override
        public int getItemCount() {
            return studentData.size();
        }
    }
}
