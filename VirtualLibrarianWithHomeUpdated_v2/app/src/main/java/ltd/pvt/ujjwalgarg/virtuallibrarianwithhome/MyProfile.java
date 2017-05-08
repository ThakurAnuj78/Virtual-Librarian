package ltd.pvt.ujjwalgarg.virtuallibrarianwithhome;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.R.attr.rating;

public class MyProfile extends AppCompatActivity {

    String user,pass,num;
    EditText phone,passwrd;
    TextView showPass,u_name;
    Button button,logout;
    int c=0,flag=0;
    TextWatcher textWatcher=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //checkFields();
            button.setEnabled(true);

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_profile);

        user=getIntent().getStringExtra("username");

        button= (Button) findViewById(R.id.profile_button);

        button.setEnabled(false);
        logout=(Button)findViewById(R.id.logout);
        u_name= (TextView) findViewById(R.id.user_name);
        phone= (EditText) findViewById(R.id.phone_number);
        passwrd= (EditText) findViewById(R.id.pass_word);
        showPass=(TextView) findViewById(R.id.show);


        final ProgressDialog dialog=new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setTitle("Please wait!!");
        dialog.setMessage("Loading your data");
        dialog.show();
        StringRequest request = new StringRequest(Request.Method.POST, "http://libraryapp.esy.es/UserDetails.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dialog.dismiss();
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);
                            JSONArray result = jsonObject.getJSONArray("result");
                            JSONObject collegeData = result.getJSONObject(0);
                            user = collegeData.getString("uname");
                            pass = collegeData.getString("pass");
                            num = collegeData.getString("phone");
                            u_name.setText(user);
                            passwrd.setText(pass);
                            phone.setText(num);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                Toast.makeText(MyProfile.this, error.getMessage(), Toast.LENGTH_LONG).show();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map= new HashMap<String, String>();
                map.put("username",user);
                return map;
            }
        };

        RequestQueue r = Volley.newRequestQueue(this);
        r.add(request);

        showPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(c%2==0){
                    //    passwrd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    passwrd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    showPass.setTextColor(Color.RED);
                    showPass.setText("Hide Password");
                    c++;
                }
                else {
                    passwrd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    showPass.setTextColor(getResources().getColor(R.color.green));
                    showPass.setText("Show Password");
                    c++;
                }

            }
        });




        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Save the changes?")
                .setCancelable(false)
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        final String pwd,phn;

                        pwd=passwrd.getText().toString();
                        phn=phone.getText().toString();

                        final ProgressDialog dialog1=new ProgressDialog(MyProfile.this);
                        dialog1.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        dialog1.setTitle("Please wait!!");
                        dialog1.setMessage("Updating Details");
                        dialog1.show();
                        StringRequest request1 = new StringRequest(Request.Method.POST, "http://libraryapp.esy.es/SaveDetails.php",
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        dialog1.dismiss();
                                        if(response.equals("success")){
                                            Toast.makeText(MyProfile.this, "Profile saved successfully", Toast.LENGTH_LONG).show();
                                        }
                                        else{
                                            dialog1.dismiss();
                                            Toast.makeText(MyProfile.this, response, Toast.LENGTH_LONG).show();
                                        }
                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                dialog1.dismiss();
                                Toast.makeText(MyProfile.this, error.getMessage(), Toast.LENGTH_LONG).show();

                            }
                        })
                        {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String,String> param= new HashMap<String, String>();
                                param.put("username",user);
                                param.put("password",pwd);
                                param.put("phone",phn);

                                return param;
                            }
                        };

                        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                        queue.add(request1);

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //  Action for 'NO' Button
                        flag=0;
                        dialog.cancel();
                       }
                });

        final AlertDialog alert = builder.create();
        //Saving Profile details
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String us,ps,ph;
                ps=passwrd.getText().toString().trim();
                ph=phone.getText().toString().trim();
                if(ps.length() < 6 || ps.length() > 30)
                {
                    Toast.makeText(MyProfile.this, "Password length must be between 6 & 30 characters!", Toast.LENGTH_SHORT).show();
                }
                else if(ph.length()!=10){
                    Toast.makeText(MyProfile.this, "Phone Number must of 10 digits!", Toast.LENGTH_SHORT).show();
                }
                else
                alert.show();
            }
        });




        phone.addTextChangedListener(textWatcher);
        passwrd.addTextChangedListener(textWatcher);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent i=new Intent(MyProfile.this,Login.class);
                SharedPreferences settings= getApplicationContext().getSharedPreferences(Constants.SHARED_PREF, Context.MODE_PRIVATE);
                settings.edit().clear().commit();
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                finish();
                startActivity(i);


            }
        });

        //checkFields();
    }



}
