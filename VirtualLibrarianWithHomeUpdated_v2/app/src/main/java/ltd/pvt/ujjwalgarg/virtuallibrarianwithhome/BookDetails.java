package ltd.pvt.ujjwalgarg.virtuallibrarianwithhome;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Rating;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
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

import static ltd.pvt.ujjwalgarg.virtuallibrarianwithhome.R.id.ratingBar;

public class BookDetails extends AppCompatActivity {

    TextView name,author,publish,category,subcat,avail;
    ImageView img;
    RatingBar rating;
    String username,isbn;
    ImageButton button;
    int flag=0;
    int c=0;
    float rate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);
        name=(TextView)findViewById(R.id.b_name);
        author=(TextView)findViewById(R.id.b_author);
        category=(TextView)findViewById(R.id.b_cat);
        publish=(TextView)findViewById(R.id.b_pub);
        subcat=(TextView)findViewById(R.id.b_subcat);
        rating=(RatingBar)findViewById(ratingBar);
        button=(ImageButton)findViewById(R.id.imgbtn);
        avail=(TextView)findViewById(R.id.b_avail);
        img=(ImageView)findViewById(R.id.b_cover);

        if(getIntent().getBooleanExtra("Wishlist",false)){
            c++;
            button.setBackgroundResource(R.drawable.heart_red);
        }

        SharedPreferences settings = getSharedPreferences(Constants.SHARED_PREF, Context.MODE_PRIVATE);
        username = settings.getString("username","1");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(c%2==0){
                    StringRequest request = new StringRequest(Request.Method.POST, "http://libraryapp.esy.es/addWishlist.php",
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String s) {
                                    if(s.equals("success")){
                                        button.setBackgroundResource(R.drawable.heart_red);
                                        c++;
                                        Toast.makeText(BookDetails.this, "Added to wishlist", Toast.LENGTH_SHORT).show();
                                    }
                                    else{
                                        Toast.makeText(BookDetails.this, "Could not add to wishlist", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {

                        }
                    }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String,String> param = new HashMap<String, String>();
                            param.put("username",username);
                            param.put("isbn",isbn);
                            return param;
                        }
                    };
                    RequestQueue queue=Volley.newRequestQueue(BookDetails.this);
                    queue.add(request);
                }
                else if(c%2!=0){
                    StringRequest request = new StringRequest(Request.Method.POST, "http://libraryapp.esy.es/removeWishlist.php",
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String s) {
                                    if(s.equals("success")){
                                        button.setBackgroundResource(R.drawable.heart);
                                        c++;
                                        Toast.makeText(BookDetails.this, "Removed from wishlist", Toast.LENGTH_SHORT).show();
                                    }
                                    else{
                                        Toast.makeText(BookDetails.this, "Could not remove from wishlist", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {

                        }
                    }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String,String> param = new HashMap<String, String>();
                            param.put("username",username);
                            param.put("isbn",isbn);
                            return param;
                        }
                    };
                    RequestQueue queue=Volley.newRequestQueue(BookDetails.this);
                    queue.add(request);
                }
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Submit the rating")
                .setCancelable(false)
                .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //  Action for 'NO' Button
                        flag=0;
                        dialog.cancel();
                        rating.setRating(0);
                    }
                });
        final AlertDialog alert = builder.create();

        //Rating Bar
        rating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float value, boolean fromUser) {
                alert.show();
                rate= rating.getRating();
                // if(flag==0)
                //   rating.setRating(0);
            }
        });
        //dialog1 = new Dialog(this); // Context, this, etc.
        //dialog1.setContentView(R.layout.dialog_layout);
        final String bname=getIntent().getStringExtra("bname");
        String url="http://libraryapp.esy.es/JSON1.php";
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
                Toast.makeText(BookDetails.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        })

        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map=new HashMap<String, String>();
                map.put("bname",bname);


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
            category.setText(collegeData.getString("cat").toUpperCase());
            String image = collegeData.getString("cover");
            img.setImageBitmap(StringToBitMap(image));
            subcat.setText(collegeData.getString("subcat").toUpperCase());
            author.setText(collegeData.getString("author").toUpperCase());
            publish.setText(collegeData.getString("publish").toUpperCase());
            isbn = collegeData.getString("isbn");
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





}
