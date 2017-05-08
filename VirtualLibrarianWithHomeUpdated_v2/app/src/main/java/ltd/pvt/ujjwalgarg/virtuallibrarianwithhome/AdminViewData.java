package ltd.pvt.ujjwalgarg.virtuallibrarianwithhome;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AdminViewData extends AppCompatActivity {
    String message;
    ListView lv;
    String str;
    List<String> bookname;
    List<String> username;
    List<String> bookid;
    SwipeRefreshLayout swipeRefreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admindataview);
        message="nam";
        lv=(ListView)findViewById(R.id.adminlistView);
        swipeRefreshLayout= (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchData();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,android.R.color.holo_blue_dark,
                android.R.color.holo_green_light,android.R.color.holo_red_light);
    }

    public void fetchData() {
        String url="http://libraryapp.esy.es/adminissued.php";
        StringRequest stringrequest= new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //dialog.dismiss();
                // Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                showJSON(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AdminViewData.this,error.getMessage() , Toast.LENGTH_LONG).show();
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
            username=new ArrayList<>();
            bookname=new ArrayList<>();
            bookid  =new ArrayList<>();
            JSONObject jsonObject= new JSONObject(response);
            JSONArray result=jsonObject.getJSONArray("result");

            for(int i=0;i<result.length();++i){
                JSONObject collegeData=result.getJSONObject(i);
                str= new String();
                str=collegeData.getString("issuedate");
                String temp=str;
                String ar[]=str.split(" ");
                str=ar[0];
                DateFormat df= new SimpleDateFormat("yyyy-MM-dd");
                Date today= Calendar.getInstance().getTime();
                String current_time=df.format(today);

                      if(str.trim().equals(current_time.trim())) {
                          bookname.add(collegeData.getString("bookname"));
                          username.add(collegeData.getString("username"));
                          bookid.add(collegeData.getString("bookid"));
                      }

            }


        }catch(Exception e){
            e.printStackTrace();
        }

        lv.setAdapter(new AdminViewDataAdapter(this,bookname,username,bookid));

    }
    }
