package ltd.pvt.ujjwalgarg.virtuallibrarianwithhome;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Naman on 03-Dec-16.
 */

public class CurrentlyBooksIssued extends AppCompatActivity {
    ListView lv;
    String []bookname;

    String []authorname;

    String message;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.books_currently_issued);
        Bundle bp=getIntent().getExtras();
        message=bp.getString("username");

        lv=(ListView)findViewById(R.id.listView);
        String url="http://libraryapp.esy.es/currentissued.php";
        final ProgressDialog dialog=new ProgressDialog(this);
        dialog.setTitle("Searching");
        dialog.setMessage("Fetching Data");
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.show();
        StringRequest stringrequest= new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                Toast.makeText(CurrentlyBooksIssued.this, message, Toast.LENGTH_SHORT).show();
                showJSON(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(CurrentlyBooksIssued.this,error.getMessage() , Toast.LENGTH_LONG).show();
            }



        })

        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map=new HashMap<String, String>();
                map.put("username",message);


                return map;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringrequest);
    }
    private void showJSON(String response){
        try{
            JSONObject jsonObject= new JSONObject(response);
            JSONArray result=jsonObject.getJSONArray("result");
            bookname= new String[result.length()];
            authorname=new String[result.length()];


            for(int i=0;i<result.length();++i){
                JSONObject collegeData=result.getJSONObject(i);
                bookname[i]=collegeData.getString("bookname");
                authorname[i]=collegeData.getString("authorname");


            }


        }catch(Exception e){
            e.printStackTrace();
        }

        lv.setAdapter(new CurrentIssueAdapter(this,bookname,authorname));


    }


}
