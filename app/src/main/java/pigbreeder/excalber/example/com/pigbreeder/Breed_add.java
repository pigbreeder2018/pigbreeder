package pigbreeder.excalber.example.com.pigbreeder;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import constructors.Model;

public class Breed_add extends AppCompatActivity {
EditText female_breed_,male_breed_;
Button save;
TextView cal_view;
ImageButton cal;
    private FirebaseAuth mAuth;
    private String mCurrentUserId;
    DatePicker simpleDatePicker;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    Calendar myCalendar = Calendar.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_breed_add);
        female_breed_=(EditText)findViewById(R.id.female_breed);
        male_breed_=(EditText)findViewById(R.id.male_breed);
        cal=(ImageButton)findViewById(R.id.calender);
        cal_view=(TextView)findViewById(R.id.dateview);
        save=(Button)findViewById(R.id.add_breed);
        mAuth = FirebaseAuth.getInstance();
        mCurrentUserId = mAuth.getCurrentUser().getUid();
       // Calendar myCalendar = Calendar.getInstance();
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        cal_view.setText(date);

       cal.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               // TODO Auto-generated method stub
               new DatePickerDialog(Breed_add.this, date_, myCalendar
                       .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                       myCalendar.get(Calendar.DAY_OF_MONTH)).show();
           }
       });

male_breed_.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        PopupMenu pop=new PopupMenu(Breed_add.this,male_breed_);
        pop.getMenuInflater().inflate(R.menu.menu_sample_breeds,pop.getMenu());
        pop.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                male_breed_.setText(item.getTitle());
                return false;
            }
        });
        pop.show();
    }
});
female_breed_.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        PopupMenu pop=new PopupMenu(Breed_add.this,female_breed_);
        pop.getMenuInflater().inflate(R.menu.menu_sample_breeds,pop.getMenu());
        pop.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                male_breed_.setText(item.getTitle());
                return false;
            }
        });
        pop.show();
    }
});

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(TextUtils.isEmpty(female_breed_.getText().toString())&&TextUtils.isEmpty(male_breed_.getText().toString())){
                    Toast.makeText(getApplicationContext(),"Fields empty",Toast.LENGTH_LONG).show();
                }else {
                    DatabaseReference ref = database.getReference();
                    Model breed=new Model(cal_view.getText().toString(),female_breed_.getText().toString(),male_breed_.getText().toString());
                    ref.child("Users").child(mCurrentUserId).child("breed history").child("breeds").push().setValue(breed);
                    female_breed_.setText("");
                    male_breed_.setText("");
                }
                finish();
            }
        });





    }
    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        cal_view.setText(sdf.format(myCalendar.getTime()));
    }
    DatePickerDialog.OnDateSetListener date_ = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }

    };
}
