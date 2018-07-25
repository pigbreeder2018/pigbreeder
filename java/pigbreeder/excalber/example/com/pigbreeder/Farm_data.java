package pigbreeder.excalber.example.com.pigbreeder;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.view.MotionEvent;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import constructors.Model;
import constructors.ModelAdapter;
import constructors.RecyclerTouchListener;


public class Farm_data extends AppCompatActivity {
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mRef;
    private FirebaseAuth mAuth;
    private String mCurrentUserId;
    private List<Model> modelList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ModelAdapter mAdapter;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farm_data);
        recyclerView=(RecyclerView)findViewById(R.id.recyclerView);

        //send a query to firebaseDatabase
        mAuth = FirebaseAuth.getInstance();
        mCurrentUserId = mAuth.getCurrentUser().getUid();
        mFirebaseDatabase=FirebaseDatabase.getInstance();
        mRef=mFirebaseDatabase.getReference("Users").child(mCurrentUserId).child("breed history").child("breeds");
            mAdapter = new ModelAdapter(modelList);
            LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(mAdapter);
            recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
                @Override
                public void onClick(View view, int position) {

                }

                @Override
                public void onLongClick(View view, int position) {
                    mAdapter.deleteItem(position);
                }

                @Override
                public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                    return false;
                }

                @Override
                public void onTouchEvent(RecyclerView rv, MotionEvent e) {

                }
            }));


        // Attach a listener to read the data at our posts reference
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    Model value = dataSnapshot1.getValue(Model.class);
                    Model fire=new Model();
                    String date=value.getDate();
                    String female=value.getFemale();
                    String male=value.getMale();
                    fire.setDate(date);
                    fire.setFemale(female);
                    fire.setMale(male);
                    modelList.add(fire);

                  //  Model model = new Model(post.getDate().toString(), post.getFemale().toString(), post.getMale().toString());
                  //  modelList.add(model);


                }





                    mAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
        }



}





