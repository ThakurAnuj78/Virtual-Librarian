package ltd.pvt.ujjwalgarg.virtuallibrarianwithhome;




import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by nitin thakur on 11/29/2016.
 */
public class CurrentIssueAdapter extends ArrayAdapter<String> {
    public final Context context;
    public final String[] bookname;
    public final String[] authorname;

    public CurrentIssueAdapter(Context context, String[]bookname, String[]authorname) {
        super(context, R.layout.currentissueview,bookname);
        this.bookname=bookname;
        this.context=context;
        this.authorname=authorname;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflator = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView=inflator.inflate( R.layout.currentissueview,parent,false);

        TextView txtview1=(TextView)rowView.findViewById(R.id.textView);
        txtview1.setText(bookname[position]);

        TextView txt=(TextView)rowView.findViewById(R.id.textView2);
        txt.setText(authorname[position]);




        return rowView;
    }
}


