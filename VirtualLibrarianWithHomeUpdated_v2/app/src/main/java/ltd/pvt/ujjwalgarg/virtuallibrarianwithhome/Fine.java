package ltd.pvt.ujjwalgarg.virtuallibrarianwithhome;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Fine extends AppCompatActivity {
    TextView txt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fine);
        txt=(TextView)findViewById(R.id.textView);
        String url="http://libraryapp.esy.es/JSON3.php";
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
                Toast.makeText(Fine.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        })

        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map=new HashMap<String, String>();

                map.put("bookID","455556525");

                return map;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringrequest);





    }
    private void showJSON (String response){
        try {

               JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray("result");

            JSONObject collegeData = result.getJSONObject(0);
            String str= new String();
            str=collegeData.getString("date");
            String ar[]=str.split(" ");
            String stre= new String();

            str=ar[0];
            SimpleDateFormat source = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat target= new SimpleDateFormat("dd/MM/yyyy");
            str=target.format(source.parse(str));


            Date d=target.parse(str);

            Calendar c = Calendar.getInstance();
            c.setTime(d);
            c.add(Calendar.DATE, 15);
            String output = target.format(c.getTime());
            Date intial= target.parse(output);
            c.setTime(new Date());
            Date finl=target.parse(target.format(c.getTime()));

            if(intial.after(finl)){
                Toast.makeText(Fine.this, "no fine", Toast.LENGTH_SHORT).show();
            }else{
                long diff = finl.getTime() - intial.getTime();
                long ans= TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
                Toast.makeText(Fine.this, 10*ans+" ", Toast.LENGTH_SHORT).show();
            }





        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
