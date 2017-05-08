package ltd.pvt.ujjwalgarg.virtuallibrarianwithhome;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by nitin thakur on 9/21/2016.
 */
public class MobileListAdapter extends ArrayAdapter<String> {
    public final Context context;
    public final String [] values;
    public final Bitmap [] images;
    public final String [] cat;
    public MobileListAdapter(Context context, String[] values,Bitmap []images, String[] cat) {
        super(context, R.layout.list_book,values);
        this.images=images;
        this.context=context;
        this.values=values;
        this.cat=cat;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflator = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView=inflator.inflate( R.layout.list_book,parent,false);

        TextView txtview=(TextView)rowView.findViewById(R.id.textView);
        ((TextView)rowView.findViewById(R.id.textView2)).setText(cat[position]);
        ImageView imageView=(ImageView)rowView.findViewById(R.id.imageView);
        txtview.setText(values[position]);
        String s=values[position];
        imageView.setImageBitmap(images[position]);


        return rowView;
    }
}

