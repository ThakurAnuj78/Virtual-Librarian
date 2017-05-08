package ltd.pvt.ujjwalgarg.virtuallibrarianwithhome;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
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

public class Wishlist extends AppCompatActivity {
    ListView lv;
    String[]isbn;

    String[]name;

    String message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlist);
        message=getIntent().getStringExtra("username");
        lv=(ListView)findViewById(R.id.listView);
        String url="http://libraryapp.esy.es/wishlist.php";
        final ProgressDialog dialog=new ProgressDialog(this);
        dialog.setTitle("Searching");
        dialog.setMessage("Fetching Data");
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.show();
        StringRequest stringrequest= new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                showJSON(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Wishlist.this,error.getMessage() , Toast.LENGTH_LONG).show();
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
            isbn= new String[result.length()];
            name=new String[result.length()];


            for(int i=0;i<result.length();++i){
                JSONObject collegeData=result.getJSONObject(i);
                isbn[i]=collegeData.getString("isbn");
                name[i]=collegeData.getString("bookname");


            }


        }catch(Exception e){
            e.printStackTrace();
        }

        lv.setAdapter(new wishlistadapter(this,name,isbn));
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                Intent i= new Intent(Wishlist.this,BookDetails.class);
                i.putExtra("bname",((TextView)view.findViewById(R.id.textView)).getText().toString());
                i.putExtra("Wishlist",true);
                startActivity(i);


            }
        });
    }
}
