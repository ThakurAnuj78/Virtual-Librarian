package ltd.pvt.ujjwalgarg.virtuallibrarianwithhome;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by nitin thakur on 11/27/2016.
 */
public class wishlistadapter extends ArrayAdapter<String> {
    public final Context context;
    public final String[] name;
    public final String[] isbn;

    public wishlistadapter(Context context, String[]name, String[]isbn) {
        super(context, R.layout.wishlistview,isbn);
       this.name=name;
        this.context=context;
        this.isbn=isbn;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflator = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView=inflator.inflate( R.layout.wishlistview,parent,false);

       TextView txtview1=(TextView)rowView.findViewById(R.id.textView);
        txtview1.setText(name[position]);

        TextView txt=(TextView)rowView.findViewById(R.id.textView2);
        txt.setText(isbn[position]);




        return rowView;
    }
}
