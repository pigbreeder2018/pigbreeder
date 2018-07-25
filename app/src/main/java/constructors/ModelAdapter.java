package constructors;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import pigbreeder.excalber.example.com.pigbreeder.R;

/**
 * Created by ALAN on 7/8/2018.
 */

public class ModelAdapter extends RecyclerView.Adapter<ModelAdapter.MyViewHolder> {

    private List<Model> moviesList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView date, female, male;

        public MyViewHolder(View view) {
            super(view);
            date = (TextView) view.findViewById(R.id.date_of_insermination);
            female = (TextView) view.findViewById(R.id.female_breed_inserminated);
            male = (TextView) view.findViewById(R.id.male_breed_used_);
        }
    }


    public ModelAdapter(List<Model> moviesList) {
        this.moviesList = moviesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_farm_data, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Model model = moviesList.get(position);
        holder.date.setText(model.getDate());
        holder.female.setText(model.getFemale());
        holder.male.setText(model.getMale());
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }
    //delete an item from view

    public void deleteItem(int index) {
        moviesList.remove(index);
        notifyItemRemoved(index);
    }
}