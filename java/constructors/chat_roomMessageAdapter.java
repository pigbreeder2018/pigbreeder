package constructors;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;

import java.util.List;

import pigbreeder.excalber.example.com.pigbreeder.R;

/**
 * Created by root on 15/2/18.
 */

public class chat_roomMessageAdapter extends RecyclerView.Adapter<chat_roomMessageAdapter.ViewHolder> {

    Context context;
    List<chat_roomMessage> messages;
    DatabaseReference messagedb;

    public chat_roomMessageAdapter(Context context, List<chat_roomMessage> messages, DatabaseReference messagedb) {
        this.context = context;
        this.messages = messages;
        this.messagedb=messagedb;
    }

    public void addMessage(chat_roomMessage message){
        this.messages.add(message);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_message,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        chat_roomMessage message=messages.get(position);
try {
    if(message.getName().equals(AllMethods.name)){
        holder.tvTitle.setText("You");
        holder.tvTitle.setGravity(Gravity.RIGHT);
        holder.llMessage.setBackgroundColor(Color.parseColor("#EF9E73"));
        holder.ibDelete.setBackgroundColor(Color.parseColor("#EF9E73"));
    }
    else {
        holder.tvTitle.setText(message.getName());
        holder.imageView.setImageResource(message.getImage());
        holder.ibDelete.setVisibility(View.GONE);
    }
    holder.tvMessage.setText(message.getMessage());
    holder.imageView.setImageResource(message.getImage());
}catch (Exception e){
    e.printStackTrace();
}

    }

    @Override
    public int getItemCount() {
        return messages.size();
    }
    //delete an item from view

    public void deleteItem(int index) {
        messages.remove(index);
        notifyItemRemoved(index);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle,tvMessage;
        ImageButton ibDelete;
        LinearLayout llMessage;
        ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
imageView=(ImageView)itemView.findViewById(R.id.imageView);
            tvTitle=(TextView)itemView.findViewById(R.id.tvTitle);
            tvMessage=(TextView)itemView.findViewById(R.id.tvMessage);
            ibDelete=(ImageButton)itemView.findViewById(R.id.ibDelete);
            llMessage=(LinearLayout)itemView.findViewById(R.id.llMessage);

            ibDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    messagedb.child(messages.get(getPosition()).getKey()).removeValue();
                }
            });

        }
    }
}
