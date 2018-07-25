package pigbreeder.excalber.example.com.pigbreeder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.ByteArrayOutputStream;

import constructors.RecyclerTouchListener;
import de.hdodenhof.circleimageview.CircleImageView;

public class user_search_reply_request extends AppCompatActivity {
RecyclerView mRecyclerResult;
TextView search_input_;
DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_search_reply_request);
        search_input_ = (TextView) findViewById(R.id.search_input);
        mRecyclerResult = (RecyclerView) findViewById(R.id.recyclersearch);
        mDatabase = FirebaseDatabase.getInstance().getReference("Users");
        mRecyclerResult.setHasFixedSize(true);

        mRecyclerResult.setLayoutManager(new LinearLayoutManager(this));
                String searchtext;
                Intent i = getIntent();
                searchtext = i.getStringExtra("username");
                //String breed_name=i.getStringExtra("breed_name");
                search_input_.setText(searchtext);
                firebasesearch(searchtext);


       /* mRecyclerResult.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), mRecyclerResult, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {



            }

            @Override
            public void onLongClick(View view, int position) {

            }

            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }
        }));*/
    }

   private void firebasesearch(String searchtext) {

       if(TextUtils.isEmpty(search_input_.getText())){Toast.makeText(getApplicationContext(),"enter name",Toast.LENGTH_LONG).show();}
       Query firebaseSearchQuery=mDatabase.orderByChild("name").startAt(searchtext).endAt(searchtext+"uf8ff");
       FirebaseRecyclerAdapter<Users,UsersViewHolder>firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<Users, UsersViewHolder>(Users.class,
               R.layout.users_single_layout,UsersViewHolder.class,firebaseSearchQuery


       ) {
           @Override
           public void populateViewHolder(UsersViewHolder viewHolder, Users model, int position) {
               final String list_user_id = getRef(position).getKey();
               viewHolder.setDetails(getApplicationContext(),model.getName(),model.getStatus(),model.getImage());

               viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       Intent p= getIntent();
                       String breed_name=p.getStringExtra("breed_names");
                       int image=p.getIntExtra("image",1);
                       Intent x=new Intent(getApplicationContext(),Send_Breed_Request_Reply.class);
                       x.putExtra("user_id", list_user_id);
                       x.putExtra("name",search_input_.getText().toString());
                       x.putExtra("breed_name",breed_name);
                       x.putExtra("image",image);
                       startActivity(x);
                   }
               });
           }
        };
       mRecyclerResult.setAdapter(firebaseRecyclerAdapter);
    }
    public static class UsersViewHolder extends RecyclerView.ViewHolder {

        View mView;

        public UsersViewHolder(View itemView) {
            super(itemView);

            mView = itemView;

        }

        public void setDetails(Context ctx,String name,String status,String image){
            TextView userNameView = (TextView) mView.findViewById(R.id.user_single_name);
            TextView userStatusView = (TextView) mView.findViewById(R.id.user_single_status);
            CircleImageView userImageView = (CircleImageView) mView.findViewById(R.id.user_single_image);
            userNameView.setText(name);
            userStatusView.setText(status);
            Glide.with(ctx).load(image).into(userImageView);

        }


    }


}
