package au.edu.curtin.assignment.mathematics;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import org.apache.commons.validator.routines.EmailValidator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import au.edu.curtin.assignment.mathematics.model.Databases;
import au.edu.curtin.assignment.mathematics.model.Student;

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


    //constant
    private static final int REQUEST_PHOTO_FROM_CAMERA = 1;
    private static final int REQUEST_PHOTO_FROM_FILE = 2;
    private static final int REQUEST_PHOTO_FROM_PIXBAY = 3;
    private static final int REQUEST_CONTACT = 4;

    private int ID;
    String DISPLAYNAME, EMAIL, PHONE;

    private void testData()
    {
        firstName.setText("Ayame");
        lastName.setText("Naikiri");

        emailList.add("dicky@gmail.com");
        emailList.add("dicky1@gmail.com");
        emailList.add("dicky2@gmail.com");
        emailList.add("dicky3@gmail.com");
        emailList.add("dicky4@gmail.com");

        phoneList.add("0431175611");
        phoneList.add("0431175614");
        phoneList.add("04311754511");
        phoneList.add("0431111611");
        phoneList.add("0431275611");

        EMAILItemHolderAdapter emailItemHolderAdapter = new EMAILItemHolderAdapter(emailList);
        studentEmailList.setLayoutManager(new LinearLayoutManager(CreateStudent.this));
        studentEmailList.setAdapter(emailItemHolderAdapter);

        PHONEADAPTER phoneadapter = new PHONEADAPTER(phoneList);
        studentPhoneList.setLayoutManager(new LinearLayoutManager(CreateStudent.this));
        studentPhoneList.setAdapter(phoneadapter);


    }

    @Override
    protected void onCreate(Bundle x)
    {
        super.onCreate(x);
        setContentView(R.layout.student_registration);

        back = (Button) findViewById(R.id.backFromStudentViewMore);
        importButton = (Button) findViewById(R.id.importStudentTeacherCreate);
        upload = (Button) findViewById(R.id.changePictureButton);

        addEmail = (Button) findViewById(R.id.addEmailButtonViewMoreStudent);
        addPhone = (Button) findViewById(R.id.addPhoneViewMore);
        addStudent = (Button) findViewById(R.id.saveInfoStudentViewMore);


        firstName = (EditText) findViewById(R.id.editTextViewMoreStudentFirstName);
        lastName = (EditText) findViewById(R.id.editTextViewMoreStudentLastName);
        email = (EditText) findViewById(R.id.editTextViewMoreStudentEmail);
        phone = (EditText) findViewById(R.id.editTextViewMoreStudentPhone);

        IMAGE = (ImageView) findViewById(R.id.imageViewStudentViewMore);


        studentEmailList = (RecyclerView) findViewById(R.id.RVViewMoreEmail);
        studentPhoneList  = (RecyclerView) findViewById(R.id.RVPhoneViewMore);

        IMAGE.setImageResource(R.drawable.images);

        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(CreateStudent.this,new String[]{Manifest.permission.READ_CONTACTS},REQUEST_CONTACT);
        }

        testData();

        addStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                studentFirstName = getStringfromEditText(firstName);
                studentLastName = getStringfromEditText(lastName);

                if (!studentFirstName.equals("") || !studentLastName.equals("")) {
                    Student newStudent = new Student(studentFirstName, studentLastName);
                    newStudent.setStudentImage(studentImageBitmap);
                    newStudent.setEmailList(emailList);
                    newStudent.setPhoneNumberList(phoneList);

                    Databases.getInstance().addStudenttoDatabase(newStudent, getApplicationContext());

                    Intent goBack = new Intent(CreateStudent.this, TeacherPage.class);
                    startActivity(goBack);
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Invalid First/Last Name", Toast.LENGTH_SHORT).show();
                }
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
                            PHONEADAPTER phoneadapter = new PHONEADAPTER(phoneList);
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
                        if(menuItem.getTitle().equals("Take A Photo"))
                        {
                            setStudentImageFromCamera();
                        }
                        else if (menuItem.getTitle().equals("Import from file"))
                        {
                            Intent getFromFile = new Intent(Intent.ACTION_GET_CONTENT);
                            getFromFile.setType("image/*");
                            startActivityForResult(getFromFile,REQUEST_PHOTO_FROM_FILE);

                        }
                        else if (menuItem.getTitle().equals("Import from PixBay"))
                        {
                            Intent intent = new Intent(CreateStudent.this,PixbayDownloader.class);
                            startActivityForResult(intent,REQUEST_PHOTO_FROM_PIXBAY);
                        }
                        return true;
                    }
                });

                popupMenu.show();
            }
        });

        importButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent contactIntent  = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(contactIntent,REQUEST_CONTACT);

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

    private void setStudentImageFromCamera()
    {
        Intent openCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            startActivityForResult(openCamera,REQUEST_PHOTO_FROM_CAMERA);
        }
        catch (ActivityNotFoundException ex)
        {

        }
    }


    private void setStudentImageFromPixBay()
    {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode,resultCode,data);
        Log.d("81781", "onActivityResult: "+requestCode);
        if (requestCode == REQUEST_PHOTO_FROM_CAMERA && resultCode == RESULT_OK)
        {
            studentImageBitmap = (Bitmap) data.getExtras().get("data");
            IMAGE.setImageBitmap(studentImageBitmap);
        }
        else if (requestCode == REQUEST_PHOTO_FROM_FILE && resultCode == RESULT_OK)
        {
            if (data!= null)
            {
                Uri uriImage = data.getData();
                try {
                    studentImageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),uriImage);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                IMAGE.setImageURI(uriImage);
            }
        }
        else if (requestCode == REQUEST_PHOTO_FROM_PIXBAY && resultCode == RESULT_OK)
        {
            if (data!=null)
            {
                String imagePath = "data/data/au.edu.curtin.assignment.mathematics/app_pixbayImages/";
                try {
                    File file = new File(imagePath,"pixbay.png");
                    Bitmap image = BitmapFactory.decodeStream(new FileInputStream(file));
                    studentImageBitmap = image;
                    IMAGE.setImageBitmap(image);
                }catch (FileNotFoundException ex)
                {
                    ex.printStackTrace();
                }
            }
        }else if (requestCode == REQUEST_CONTACT && resultCode == RESULT_OK)
        {
            Uri contactUri = data.getData();
            String [] queryFields = new String [] {
                    ContactsContract.Contacts._ID,
                    ContactsContract.Contacts.DISPLAY_NAME
            };
            Cursor cursor = getContentResolver().query(contactUri,queryFields,null,null,null);
            try {
                if (cursor.getCount()>0)
                {
                    cursor.moveToFirst();
                    ID = cursor.getInt(0);
                    DISPLAYNAME = cursor.getString(1);

                }
            }finally {
                cursor.close();
            }

            Cursor emailAddress = getContentResolver().query(
                    ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                    new String [] {ContactsContract.CommonDataKinds.Email.ADDRESS},
                    ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                    new String [] {String.valueOf(ID)},
                    null,
                    null
            );

            try {
                emailAddress.moveToFirst();
                do {
                    emailList.add(emailAddress.getString(0));
                }while (emailAddress.moveToNext());
            }finally {

                EMAILItemHolderAdapter emailItemHolderAdapter = new EMAILItemHolderAdapter(emailList);
                studentEmailList.setLayoutManager(new LinearLayoutManager(CreateStudent.this));
                studentEmailList.setAdapter(emailItemHolderAdapter);
                emailAddress.close();
            }

            Cursor phoneNumber = getContentResolver().query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    new String[] {ContactsContract.CommonDataKinds.Phone.NUMBER},
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                    new String [] {String.valueOf(ID)},
                    null,
                    null
            );

            try {
                Log.d("98", "onActivityResult: "+phoneNumber.getCount());
                phoneNumber.moveToFirst();
                do {
                    phoneList.add(phoneNumber.getString(0));
                }while (phoneNumber.moveToNext());
            }
            finally {
                PHONEADAPTER phoneadapter = new PHONEADAPTER(phoneList);
                studentPhoneList.setLayoutManager(new LinearLayoutManager(CreateStudent.this));
                studentPhoneList.setAdapter(phoneadapter);
                phoneNumber.close();
            }

            this.firstName.setText(SplitName(DISPLAYNAME)[0]);
            this.lastName.setText(SplitName(DISPLAYNAME)[1]);
        }
    }

    public void onRequestPermissionResult(int requestCode, @NonNull String [] permissions, @NonNull int [] grantResults_)
    {
        super.onRequestPermissionsResult(requestCode,permissions,grantResults_);
        if (requestCode == REQUEST_CONTACT)
        {
            if (grantResults_.length>0 && grantResults_[0]== PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(getApplicationContext(),"Contact Reading permission GRANTED", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String [] SplitName(String DISPLAYNAME)
    {
        return DISPLAYNAME.split("\\s+");
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
                    notifyItemRangeChanged(0,data.size());
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

