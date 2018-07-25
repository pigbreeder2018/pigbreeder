package pigbreeder.excalber.example.com.pigbreeder;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.squareup.picasso.Picasso;

import java.sql.Time;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Send_Breed_Request_Reply extends AppCompatActivity {
ImageButton accept;
ImageView pig_image_;
private DatabaseReference mRootRef;
EditText serviceCosts,Location;
TextView recieverName,breedType_,currType;
Intent i;
String mChatUser,user_name,breed_name,cost_,locc,mCurrentUserId,currency;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send__breed__request__reply);
        accept=(ImageButton)findViewById(R.id.send_reply);
        pig_image_=(ImageView)findViewById(R.id.pig_image);
        serviceCosts=(EditText)findViewById(R.id.svsCosts);
        Location=(EditText)findViewById(R.id.location);
        recieverName=(TextView)findViewById(R.id.reciever_name);
        breedType_=(TextView)findViewById(R.id.breedType);
        currType=(TextView)findViewById(R.id.currencyType);
        mRootRef = FirebaseDatabase.getInstance().getReference();
        i=getIntent();
        mChatUser=i.getStringExtra("user_id");
        user_name=i.getStringExtra("name");
        breed_name=i.getStringExtra("breed_name");
        mCurrentUserId=FirebaseAuth.getInstance().getCurrentUser().getUid();

       /* cost_=serviceCosts.getText().toString();
        locc=Location.getText().toString();
        currency=currType.getText().toString();*/

        Picasso.with(getApplicationContext()).load(i.getIntExtra("image",1)).into(pig_image_);

        recieverName.setText(recieverName.getText().toString()+""+"To: "+" "+user_name.toString());
        breedType_.setText("Pig Breed: "+" "+breed_name.toString());

        currType.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                PopupMenu pop=new PopupMenu(Send_Breed_Request_Reply.this,currType);
                pop.getMenuInflater().inflate(R.menu.popup_currency_menu,pop.getMenu());
                pop.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        currType.setText(item.getTitle());
                        return true;
                    }
                });
                pop.show();
                return false;
            }
        });


        accept.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(currType.getText().toString())&&TextUtils.isEmpty(Location.getText().toString())&&TextUtils.isEmpty(breedType_.getText().toString())&&TextUtils.isEmpty(recieverName.getText().toString())&&TextUtils.isEmpty(serviceCosts.getText().toString())){
                    Toast.makeText(getApplicationContext(),"Enter the empty fields",Toast.LENGTH_LONG).show();
                }else {
cost_=serviceCosts.getText().toString();
currency=currType.getText().toString();
locc=Location.getText().toString();
              sendMessage(recieverName.getText().toString(),breedType_.getText().toString(),cost_,currency,locc);
                    Toast.makeText(getApplicationContext(),"Reply Sent Successfully",Toast.LENGTH_LONG).show();
              breedType_.setText("");recieverName.setText("");currType.setText("");Location.setText("");serviceCosts.setText("");
         finish();
             }
            }
        });
    }
    private void sendMessage(String name,String brd,String cost,String Ctype,String location) {
String message=name+"\n"+"Pig breed:"+" "+brd+"\n"+"Service Cost:"+""+cost+" "+Ctype+"\n"+"Service Location:"+""+location;

        if(!TextUtils.isEmpty(message)){

            String current_user_ref = "messages/" + mCurrentUserId + "/" + mChatUser;
            String chat_user_ref = "messages/" + mChatUser + "/" + mCurrentUserId;

            DatabaseReference user_message_push = mRootRef.child("messages")
                    .child(mCurrentUserId).child(mChatUser).push();

            String push_id = user_message_push.getKey();

            Map messageMap = new HashMap();
            messageMap.put("message", message);
            messageMap.put("seen", false);
            messageMap.put("type", "text");
            messageMap.put("time", ServerValue.TIMESTAMP);
            messageMap.put("from", mCurrentUserId);

            Map messageUserMap = new HashMap();
            messageUserMap.put(current_user_ref + "/" + push_id, messageMap);
            messageUserMap.put(chat_user_ref + "/" + push_id, messageMap);


            mRootRef.child("Chat").child(mCurrentUserId).child(mChatUser).child("seen").setValue(true);
            mRootRef.child("Chat").child(mCurrentUserId).child(mChatUser).child("timestamp").setValue(ServerValue.TIMESTAMP);

            mRootRef.child("Chat").child(mChatUser).child(mCurrentUserId).child("seen").setValue(false);
            mRootRef.child("Chat").child(mChatUser).child(mCurrentUserId).child("timestamp").setValue(ServerValue.TIMESTAMP);

            mRootRef.updateChildren(messageUserMap, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                    if(databaseError != null){

                        Log.d("CHAT_LOG", databaseError.getMessage().toString());

                    }

                }
            });

        }

    }
}
