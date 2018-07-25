package constructors;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import pigbreeder.excalber.example.com.pigbreeder.R;

/**
 * Created by ALAN on 7/18/2018.
 */

public class Adapters extends ArrayAdapter<String> {
    Context c;
    String[] breeds;
    int[] images;
    LayoutInflater inflater;
    public Adapters(Context context,String[]breeds,int[] images){
        super(context, R.layout.pig_breed_row,breeds);
        this.c=context;
        this.images=images;
        this.breeds=breeds;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView==null){
            inflater= (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(R.layout.pig_breed_row,null);
        }
        TextView pig_name=(TextView)convertView.findViewById(R.id.pig_breed_name);
        ImageView pig_image=(ImageView)convertView.findViewById(R.id.pig_image_list_row);
           pig_name.setText(breeds[position]);
        pig_image.setImageResource(images[position]);
        return convertView;
    }
}
