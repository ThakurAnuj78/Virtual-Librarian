package ltd.pvt.ujjwalgarg.virtuallibrarianwithhome;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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

public class BookIssue extends AppCompatActivity {

    private TextView name,author,avail,b_id;
    private ImageView img;
    private String barcode,username;
    private Button book_issue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_issue);
        name=(TextView)findViewById(R.id.b_name1);
        author=(TextView)findViewById(R.id.b_author1);
        avail=(TextView)findViewById(R.id.b_avail1);
        b_id=(TextView)findViewById(R.id.book_id1);
        img=(ImageView)findViewById(R.id.b_cover1);
        book_issue=(Button)findViewById(R.id.book_issue1);
        username=getIntent().getStringExtra("Username");
        barcode=getIntent().getStringExtra("Barcode");
        b_id.setText(barcode);
        String url="http://libraryapp.esy.es/JSON2.php";

        book_issue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                issueBook();
            }
        });

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
                Toast.makeText(BookIssue.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        })

        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map=new HashMap<>();
                map.put("bookID",barcode);
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
            name.setText(collegeData.getString("bname").toUpperCase());
            String image = collegeData.getString("cover");
            img.setImageBitmap(StringToBitMap(image));
            author.setText(collegeData.getString("author").toUpperCase());
            if(collegeData.getString("avail").equals("0")){
                avail.setText("Currently Not Available");
            }else
                avail.setText("Available");
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    public void issueBook(){
        String url2="http://libraryapp.esy.es/issueBook.php";
        final ProgressDialog dialog1=new ProgressDialog(this);
        dialog1.setTitle("Please wait...!!!");
        dialog1.setMessage("Sending data");
        dialog1.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog1.show();
        StringRequest stringrequest= new StringRequest(Request.Method.POST, url2, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog1.dismiss();
                if(response.contains("success")){
                    Toast.makeText(BookIssue.this, "Book issued successfully", Toast.LENGTH_LONG).show();
                    book_issue.setText("Issued");
                    book_issue.setClickable(false);
                }
                else{
                    Toast.makeText(BookIssue.this, "Book can't be issued. Please try again later", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(BookIssue.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        })

        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map=new HashMap<>();
                map.put("bookID",barcode);
                map.put("username",username);
                return map;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringrequest);
    }
}
