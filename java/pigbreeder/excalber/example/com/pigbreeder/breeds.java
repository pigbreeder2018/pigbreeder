package pigbreeder.excalber.example.com.pigbreeder;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import constructors.Adapters;
import constructors.AllMethods;
import constructors.chat_roomMessage;
import constructors.chat_roomUser;

import static android.content.ContentValues.TAG;

/**
 * Created by ALAN on 7/18/2018.
 */

public class breeds extends DialogFragment {
    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference messagedb;
    chat_roomUser u;
     String mCurrentUserId;
    ListView listView;
    final String[] breeds = {"Combourough", "Duroc", "Gloucestershire", "LandRace","Largeblack","LargeWhite","Potbellied","Sadleback","basque","Berkshire","blossom","chester White","gloucesterold spot boar","Tamworth pig","yorkshire pig","poland china pig","kunekune_pig_at_hamilton_zoo"};
    final int[] images = {R.drawable.combourough, R.drawable.duroc, R.drawable.gloucestershire, R.drawable.landrace,R.drawable.largeblack,R.drawable.largewhite,R.drawable.potbellied,R.drawable.saddleback,R.drawable.basque,R.drawable.berkshirepig,R.drawable.blossom,R.drawable.chesterwhitepig,R.drawable.gloucestershire,R.drawable.tamworth_pig,R.drawable.yorkshire_pig,R.drawable.poland_china_pig,R.drawable.kunekune_pig_at_hamilton_zoo};
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.request_listview, container, false);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        mCurrentUserId=FirebaseAuth.getInstance().getCurrentUser().getUid();
        u=new chat_roomUser();
        messagedb=database.getReference("Users").child("breed_request");
        listView = (ListView) rootView.findViewById(R.id.listview_pigs);
        getDialog().setTitle("SAMPLE PIG BREEDS");
        Adapters adapters = new Adapters(getActivity(), breeds, images);
        listView.setAdapter(adapters);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
try {
    if(isNetworkAvailable()){
        chat_roomMessage message = new chat_roomMessage(/*etMessage.getText().toString()*/breeds[position], u.getName(),images[position]);
        // etMessage.setText("");
String sender_id=u.getUid();
        messagedb.push().setValue(message);

    }
    else {
        Toast.makeText(getActivity(),"Check Your Network connection To Proceed",Toast.LENGTH_LONG).show();
    }
    dismiss();
}catch (Exception e){
    e.printStackTrace();
}

            }
        });





        return rootView;
    }



    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public void onStart() {
        super.onStart();
        final FirebaseUser currentUser=auth.getCurrentUser();
        u.setUid(currentUser.getUid());
        u.setEmail(currentUser.getEmail());
        database.getReference("Users").child(currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                u=dataSnapshot.getValue(chat_roomUser.class);
                u.setUid(currentUser.getUid());
                AllMethods.name=u.getName();
                Log.e(TAG, "onDataChange: "+AllMethods.name );
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
}}