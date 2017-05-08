package ltd.pvt.ujjwalgarg.virtuallibrarianwithhome;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class AdminViewDataAdapter extends ArrayAdapter<String> {
    public final Context context;
    public final List<String> bookname;
    public final List<String> username;
    public final List<String> bookid;

    public AdminViewDataAdapter(Context context, List<String> bookname, List<String> username, List<String> bookid) {
        super(context,R.layout.adminissueview,bookname);
        this.bookname=bookname;
        this.context=context;
        this.username=username;
        this.bookid=bookid;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflator = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView=inflator.inflate( R.layout.adminissueview,parent,false);

        TextView txtview1=(TextView)rowView.findViewById(R.id.adminlist_book);
        txtview1.setText(bookname.get(position));

        TextView txt=(TextView)rowView.findViewById(R.id.adminlist_user);
        txt.setText(username.get(position));

        TextView txt1=(TextView)rowView.findViewById(R.id.adminlist_bookid);
        txt1.setText(bookid.get(position));

        return rowView;
    }
}
