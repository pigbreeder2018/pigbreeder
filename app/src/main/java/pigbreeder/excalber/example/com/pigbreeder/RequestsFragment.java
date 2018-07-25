package pigbreeder.excalber.example.com.pigbreeder;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;

import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import constructors.AllMethods;
import constructors.RecyclerTouchListener;
import constructors.chat_roomMessage;
import constructors.chat_roomMessageAdapter;
import constructors.chat_roomUser;

import static pigbreeder.excalber.example.com.pigbreeder.EndlessRecyclerOnScrollListener.TAG;

public class RequestsFragment extends Fragment {
    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference messagedb;
    public chat_roomMessageAdapter adapter;
    chat_roomUser u;
    List<chat_roomMessage> messages;
    RecyclerView rvMessage;
    FloatingActionButton sendBreed;
    private FirebaseAuth mAuth;

    public String mCurrentUserId;

    public RequestsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view;
         view= inflater.inflate(R.layout.fragment_requests, container, false);
        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        u=new chat_roomUser();
        mAuth = FirebaseAuth.getInstance();
        mCurrentUserId = mAuth.getCurrentUser().getUid();
        rvMessage=(RecyclerView)view.findViewById(R.id.rvMessage);
         sendBreed=(FloatingActionButton)view.findViewById(R.id.sendBreedR);
         final android.support.v4.app.FragmentManager fm= getFragmentManager();
         final breeds p=new breeds();
         sendBreed.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 p.show(fm,"breeds");

             }
         });


        rvMessage.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), rvMessage, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, final int position) {
                final chat_roomMessage msg = messages.get(position);
               // chat_roomUser user=new chat_roomUser();
                final String name1=msg.getName().toString();
                FirebaseUser users=FirebaseAuth.getInstance().getCurrentUser();
                String userid=users.getUid();
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
                ref.child(userid).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                     public void onDataChange(DataSnapshot dataSnapshot) {
                                                                         String name = dataSnapshot.child("name").getValue().toString();
                                                                        //stopping self reply
                                                                         if(name.equals(name1)){
                                                                             Toast.makeText(getContext(),"Can't reply to your self",Toast.LENGTH_LONG).show();

                                                                         }else {
                                                                             Toast.makeText(getActivity(), "SERVICE REPLY TO"+" "+msg.getName(), Toast.LENGTH_SHORT).show();
                                                                             Intent i=new Intent(getActivity(),user_search_reply_request.class);
                                                                             i.putExtra("username",msg.getName());
                                                                             i.putExtra("breed_names",msg.getMessage());
                                                                             i.putExtra("image",msg.getImage());
                                                                             startActivity(i);
                                                                         }
                                                                     }

                                                                     @Override
                                                                     public void onCancelled(DatabaseError databaseError) {

                                                                     }

                                                                 });
                   }



            @Override
            public void onLongClick(View view, int position) {
                adapter.deleteItem(position);
            }

            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }
        }));

return view;
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
                Log.e(TAG, "onDataChange: "+ AllMethods.name );
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        messagedb=database.getReference("Users").child("breed_request");
        messagedb.keepSynced(true);

        messagedb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                chat_roomMessage message=dataSnapshot.getValue(chat_roomMessage.class);
                message.setKey(dataSnapshot.getKey());
                messages.add(message);
                displayMessages(messages);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                chat_roomMessage message=dataSnapshot.getValue(chat_roomMessage.class);
                message.setKey(dataSnapshot.getKey());

                List<chat_roomMessage> newMessages=new ArrayList<>();
                for (chat_roomMessage m:messages){
                    if(m.getKey().equals(message.getKey())){
                        newMessages.add(message);
                    }
                    else{
                        newMessages.add(m);
                    }
                }
                messages=newMessages;
                displayMessages(messages);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                chat_roomMessage message=dataSnapshot.getValue(chat_roomMessage.class);
                message.setKey(dataSnapshot.getKey());
                List<chat_roomMessage> newMessages=new ArrayList<>();
                for (chat_roomMessage m:messages){
                    if(!m.getKey().equals(message.getKey())){
                        newMessages.add(m);
                    }
                }
                messages=newMessages;
                displayMessages(messages);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });}

    private void displayMessages(List<chat_roomMessage> messages) {
        rvMessage.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter=new chat_roomMessageAdapter(getActivity(),messages,messagedb);
        rvMessage.addItemDecoration(new MyDividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL, 16));
        rvMessage.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        messages=new ArrayList<>();

    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}


