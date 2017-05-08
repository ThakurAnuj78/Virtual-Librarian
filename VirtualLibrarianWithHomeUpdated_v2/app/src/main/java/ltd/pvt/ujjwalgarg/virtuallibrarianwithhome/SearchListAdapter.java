package ltd.pvt.ujjwalgarg.virtuallibrarianwithhome;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by nitin thakur on 11/22/2016.
 */
public class SearchListAdapter extends ArrayAdapter<String> {
    public final Context context;
    public final String[] bookname;
    public final String[] fine;
    public final String[] dte;
    public SearchListAdapter(Context context, String[] bookname, String[]fine, String[] dte) {
        super(context, R.layout.history_list,bookname);
        this.bookname=bookname;
        this.context=context;
        this.fine=fine;
        this.dte=dte;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflator = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView=inflator.inflate( R.layout.history_list,parent,false);

        TextView txtview1=(TextView)rowView.findViewById(R.id.textView);
        ((TextView)rowView.findViewById(R.id.textView2)).setText(fine[position]);
        TextView txt=(TextView)rowView.findViewById(R.id.textView3);
        txt.setText(bookname[position]);

        txtview1.setText(dte[position]);


        return rowView;
    }
}
