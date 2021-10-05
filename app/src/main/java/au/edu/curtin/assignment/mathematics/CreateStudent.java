package au.edu.curtin.assignment.mathematics;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import org.apache.commons.validator.routines.EmailValidator;

import java.util.ArrayList;
import java.util.List;

import au.edu.curtin.assignment.mathematics.db.PHONE;

public class CreateStudent extends AppCompatActivity {

    private Button back, addEmail, addPhone, addStudent,upload, importButton;
    private EditText firstName, lastName, email, phone;
    private RecyclerView studentEmailList, studentPhoneList;
    private ImageView IMAGE;

    private String studentFirstName, studentLastName, studentEmailAddress, studentPhoneNumber;
    private List<String> phoneList = new ArrayList<>();
    private List<String> emailList = new ArrayList<>();
    private Bitmap studentImageBitmap;
    private List<String> test = new ArrayList<>();


    @Override
    protected void onCreate(Bundle x)
    {
        super.onCreate(x);
        setContentView(R.layout.student_registration);

        back = (Button) findViewById(R.id.backFromCreate);
        importButton = (Button) findViewById(R.id.importStudentTeacherCreate);
        upload = (Button) findViewById(R.id.uploadStudentImageButton);

        addEmail = (Button) findViewById(R.id.addEmail);
        addPhone = (Button) findViewById(R.id.addStudentPhone);
        addStudent = (Button) findViewById(R.id.addStudent);


        firstName = (EditText) findViewById(R.id.editTextStudentFirstName);
        lastName = (EditText) findViewById(R.id.editTextStudentLastName);
        email = (EditText) findViewById(R.id.editTextStudentEmail);
        phone = (EditText) findViewById(R.id.editTextPhoneNumber);

        IMAGE = (ImageView) findViewById(R.id.imageViewStudentPicture);


        studentEmailList = (RecyclerView) findViewById(R.id.recyclerViewStudentEmail);
        studentPhoneList  = (RecyclerView) findViewById(R.id.recyclerViewPhoneNumber);

        test.add("1");
        test.add("2");
        test.add("3");
        test.add("4");
        test.add("5");
        test.add("6");
        test.add("7");
        test.add("8");
        test.add("9");
        test.add("10");


        addStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                studentFirstName = getStringfromEditText(firstName);
                studentLastName = getStringfromEditText(lastName);
            }
        });

        addEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (emailList.size()<=10)
                {
                    studentEmailAddress = getStringfromEditText(email);
                    if (isValidEmail(studentEmailAddress)) {
                        if (!emailList.contains(studentEmailAddress)) {
                            emailList.add(studentEmailAddress);
                            EMAILItemHolderAdapter emailItemHolderAdapter = new EMAILItemHolderAdapter(emailList);
                            studentEmailList.setLayoutManager(new LinearLayoutManager(CreateStudent.this));
                            studentEmailList.setAdapter(emailItemHolderAdapter);
                        } else {
                            Toast.makeText(getApplicationContext(), "The Email Address already Exist", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Toast.makeText(getApplicationContext(),"Invalid Email Address",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        addPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (phoneList.size()<=10)
                {
                    studentPhoneNumber = getStringfromEditText(phone);
                    if (studentPhoneNumber.length()==10)
                    {
                        if (!phoneList.contains(studentPhoneNumber))
                        {
                            phoneList.add(studentPhoneNumber);
                            PHONEADAPTER phoneadapter = new PHONEADAPTER(test);
                            studentPhoneList.setLayoutManager(new LinearLayoutManager(CreateStudent.this));
                            studentPhoneList.setAdapter(phoneadapter);
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), "The Phone Number Already Exist", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Invalid Phone Number", Toast.LENGTH_SHORT).show();
                    }
                }else
                {
                    Toast.makeText(getApplicationContext(), "You've reached the maximum allowable phone number.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(CreateStudent.this,upload);
                popupMenu.getMenuInflater().inflate(R.layout.menu_drop,popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        Toast.makeText(getApplicationContext(),menuItem.getTitle(),Toast.LENGTH_SHORT).show();
                        return true;
                    }
                });

                popupMenu.show();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goback = new Intent(CreateStudent.this,TeacherPage.class);
                startActivity(goback);
            }
        });




    }

    private boolean isValidEmail(String email)
    {
        return EmailValidator.getInstance().isValid(email);
    }

    private void setStudentImageFromCamera(ImageView image)
    {

    }

    private void setStudentImageFromFIles(ImageView image)
    {

    }

    private void setStudentImageFromPixBay(ImageView image)
    {

    }

    private String getStringfromEditText(EditText text)
    {
        String res="";
        if (!text.getText().toString().equals(null) || !text.getText().toString().equals(""));
        {
            res = text.getText().toString();
        }

        return res;
    }

    private class EMAILItemHolderAdapter extends RecyclerView.Adapter<EMAILItemHolder>
    {
        private List<String> itemList = new ArrayList<>();

        public EMAILItemHolderAdapter(List<String> data)
        {
            this.itemList = data;
        }

        @Override
        public EMAILItemHolder onCreateViewHolder(ViewGroup container, int viewType)
        {
            LayoutInflater layoutInflater = LayoutInflater.from(container.getContext());
            View view = layoutInflater.inflate(R.layout.each_list_create,container,false);
            EMAILItemHolder itemHolder = new EMAILItemHolder(view);

            return itemHolder;
        }

        @Override
        public void onBindViewHolder(EMAILItemHolder itemHolder, @SuppressLint("RecyclerView") int position)
        {
            itemHolder.bind(this.itemList.get(position));
            itemHolder.removeItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemList.remove(position);
                    notifyItemChanged(position);
                    notifyItemRangeChanged(position,itemList.size());
                    notifyDataSetChanged();
                }
            });
        }

        @Override
        public int getItemCount()
        {
            return this.itemList.size();
        }
    }

    private class EMAILItemHolder extends RecyclerView.ViewHolder{

        TextView itemName;
        Button removeItem;

        public EMAILItemHolder(View view)
        {
            super(view);

            itemName = (TextView) view.findViewById(R.id.ItemNameEachLayout);
            removeItem = (Button) view.findViewById(R.id.removeButtonxxx);


        }

        public void bind(String item)
        {
            itemName.setText(item);
        }
    }

    private class PHONEViewHolder extends RecyclerView.ViewHolder {
        TextView itemName;
        Button removeItem;

        public PHONEViewHolder(View view)
        {
            super(view);

            itemName = (TextView) view.findViewById(R.id.ItemNameEachLayout);
            removeItem = (Button) view.findViewById(R.id.removeButtonxxx);

        }

        public void bind(String item)
        {
            itemName.setText(item);
        }
    }

    private class PHONEADAPTER extends RecyclerView.Adapter<PHONEViewHolder>
    {
        private List<String> data;

        public PHONEADAPTER(List<String> data)
        {
            this.data = data;
        }

        @NonNull
        @Override
        public PHONEViewHolder onCreateViewHolder(@NonNull ViewGroup container, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(container.getContext());
            View view = layoutInflater.inflate(R.layout.each_list_create,container,false);
            PHONEViewHolder itemHolder = new PHONEViewHolder(view);

            return itemHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull PHONEViewHolder holder, @SuppressLint("RecyclerView") int position) {
            holder.bind(this.data.get(position));
            holder.removeItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    data.remove(position);
                    notifyItemChanged(position);
                    notifyItemRangeChanged(position,data.size());
                    notifyDataSetChanged();
                }
            });
        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }

    public void print(String message)
    {
        Log.d("91", "print: "+message);
    }
}

