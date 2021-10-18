package au.edu.curtin.assignment.mathematics;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import au.edu.curtin.assignment.mathematics.model.Databases;
import au.edu.curtin.assignment.mathematics.model.Student;
import au.edu.curtin.assignment.mathematics.model.TestHistory;

public class ViewTest extends AppCompatActivity {
    private Button back;
    private Spinner searchSpinner;
    private RecyclerView recyclerViewTest;
    private List<TestHistory> testHistoryList = new ArrayList<>();
    private String x;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_test);

        back = (Button) findViewById(R.id.backfromtestHistoryView);
        searchSpinner = (Spinner) findViewById(R.id.spinnerTestHistory);
        recyclerViewTest = (RecyclerView) findViewById(R.id.recyclerviewTestView);

        testHistoryList = Databases.getInstance().loadTest();

        x = searchSpinner.getSelectedItem().toString();

        Log.d("1212", "onCreate: "+testHistoryList.size());

        searchSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                x = searchSpinner.getSelectedItem().toString();
                TestHistoryAdapter testHistoryAdapter = null;
                if (x.equals("Highest - Lowest"))
                {
                    //sort the list so that it shows in descending order
                    Collections.sort(testHistoryList,new SortDescending());
                    testHistoryAdapter = new TestHistoryAdapter(testHistoryList);
                }
                else if (x.equals("Lowest - Highest"))
                {
                    Collections.sort(testHistoryList,new SortAscending());
                    testHistoryAdapter = new TestHistoryAdapter(testHistoryList);
                }

                recyclerViewTest.setLayoutManager(new LinearLayoutManager(ViewTest.this));
                recyclerViewTest.setAdapter(testHistoryAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goBack = new Intent(ViewTest.this,TeacherPage.class);
                startActivity(goBack);
            }
        });

    }

    private class TestHistoryHolder extends RecyclerView.ViewHolder {
        TextView Date,studentName,studentScore;

        public TestHistoryHolder(View view)
        {
            super(view);

            Date = view.findViewById(R.id.dateTextViewVTE);
            studentName=view.findViewById(R.id.studentNameVTE);
            studentScore = view.findViewById(R.id.studentScoreVTE);

        }

        public void bind(String studentName, String date, String studentScore)
        {
            Date.setText(date);
            this.studentName.setText(studentName);
            this.studentScore.setText(studentScore);
        }
    }



    private class TestHistoryAdapter extends RecyclerView.Adapter<TestHistoryHolder>
    {
        private List<TestHistory> testHistoryList;

        public TestHistoryAdapter(List<TestHistory> data)
        {
            this.testHistoryList=  data;
        }

        @NonNull
        @Override
        public TestHistoryHolder onCreateViewHolder(@NonNull ViewGroup container, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(container.getContext());
            View view = layoutInflater.inflate(R.layout.view_test_each,container,false);
           TestHistoryHolder itemHolder = new TestHistoryHolder(view);

            return itemHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull TestHistoryHolder holder, @SuppressLint("RecyclerView") int position) {
            holder.bind(testHistoryList.get(position).getStudentName(),testHistoryList.get(position).getDate(),String.valueOf(testHistoryList.get(position).getFinalScore()));
        }

        @Override
        public int getItemCount() {
            return testHistoryList.size();
        }
    }

    private class SortAscending implements Comparator<TestHistory>
    {

        @Override
        public int compare(TestHistory testHistory, TestHistory t1) {
            return testHistory.getFinalScore() - t1.getFinalScore();
        }
    }

    private class SortDescending implements Comparator<TestHistory>
    {

        @Override
        public int compare(TestHistory testHistory, TestHistory t1) {
            return t1.getFinalScore() - testHistory.getFinalScore();
        }
    }
}
