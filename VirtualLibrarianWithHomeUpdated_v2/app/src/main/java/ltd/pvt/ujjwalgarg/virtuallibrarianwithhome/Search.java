package ltd.pvt.ujjwalgarg.virtuallibrarianwithhome;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
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

public class Search extends AppCompatActivity  {
    ListView lv;
    String []books;
    Bitmap []images;
    String []cat;
    String []image;
    String message;
    String []subcat;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);
        Bundle bp=getIntent().getExtras();
        message=bp.getString("search");
        lv=(ListView)findViewById(R.id.listView);
        String url="http://libraryapp.esy.es/JSON.php";
        final ProgressDialog dialog=new ProgressDialog(this);
        dialog.setTitle("Searching");
        dialog.setMessage("Fetching Data");
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.show();
        StringRequest stringrequest= new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
               // Toast.makeText(Search.this, message, Toast.LENGTH_SHORT).show();
                showJSON(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Search.this,error.getMessage() , Toast.LENGTH_LONG).show();
            }



        })

        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map=new HashMap<String, String>();
                map.put("query",message);


                return map;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringrequest);


    }
    private void showJSON(String response){
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray("result");
            books = new String[result.length()];
            cat = new String[result.length()];
            images = new Bitmap[result.length()];
            image = new String[result.length()];
            subcat = new String[result.length()];
            if (result.length() == 0) {
                Toast.makeText(this, "no book found", Toast.LENGTH_SHORT).show();
            } else {
                for (int i = 0; i < result.length(); ++i) {
                    JSONObject collegeData = result.getJSONObject(i);
                    books[i] = collegeData.getString("bname");
                    cat[i] = collegeData.getString("cat");
                    image[i] = collegeData.getString("image");
                    images[i] = StringToBitMap(image[i]);
                    subcat[i] = collegeData.getString("subcat");
                }

            }
            }catch(Exception e){
                e.printStackTrace();
            }

            lv.setAdapter(new MobileListAdapter(this, books, images, cat));
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                    Intent i = new Intent(Search.this, BookDetails.class);
                    i.putExtra("bname", ((TextView) view.findViewById(R.id.textView)).getText().toString());
                    startActivity(i);

                    // Toast.makeText(Search.this, ((TextView)view.findViewById(R.id.textView)).getText().toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }

    public Bitmap StringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }
}
