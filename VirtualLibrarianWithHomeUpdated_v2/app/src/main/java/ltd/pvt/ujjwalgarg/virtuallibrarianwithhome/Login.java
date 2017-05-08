package ltd.pvt.ujjwalgarg.virtuallibrarianwithhome;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.firebase.client.Firebase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {
    AnimationDrawable animationDrawable;
    RelativeLayout relativeLayout;
    Button login,reg,admin;
    TextView textView;
    EditText username;
    EditText password;
    int c=0;
    LayoutInflater li;
    SharedPreferences settings;
    String PREFERENCES_NAME=Constants.SHARED_PREF;
    TextWatcher textWatcher=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            checkFields();
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Firebase.setAndroidContext(getApplicationContext());

        username= (EditText) findViewById(R.id.editText);
        password= (EditText) findViewById(R.id.editText2);
        login= (Button) findViewById(R.id.button);
        textView= (TextView) findViewById(R.id.textView);
        admin= (Button) findViewById(R.id.button2);
        reg= (Button) findViewById(R.id.button3);
        final TextView showPass=(TextView)findViewById(R.id.show);

        showPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(c%2==0){
                    //    passwrd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());

                    // showPass.setTextColor(Color.RED);
                    showPass.setText("Hide Password");
                    c++;
                }
                else {
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());

                    //showPass.setTextColor(getResources().getColor(R.color.green));
                    showPass.setText("Show Password?");


                    c++;
                }

            }
        });



        Typeface externalFont=Typeface.createFromAsset(getBaseContext().getAssets(),"fonts/logo_font.ttf");
        textView.setTypeface(externalFont);

        relativeLayout= (RelativeLayout) findViewById(R.id.ulogin);
        //relativeLayout.setBackgroundResource(R.id.);
        animationDrawable= (AnimationDrawable) relativeLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(1500);


        username.addTextChangedListener(textWatcher);
        password.addTextChangedListener(textWatcher);
        checkFields();
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getActivity(),"Login",Toast.LENGTH_SHORT).show();
                userLogin();

            }
        });
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Login.this,Register.class);
                startActivity(intent);
                //ft1.commit();
            }
        });
        admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Login.this,AdminLogin.class);
                startActivity(intent);

                //ft2.commit();
            }
        });
    }

    void userLogin(){
        final String user=username.getText().toString();
        final String pass=password.getText().toString();
        final ProgressDialog dialog=new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setTitle("Please wait!!");
        dialog.setMessage("Loading your data");
        dialog.show();
        StringRequest request = new StringRequest(Request.Method.POST, "http://libraryapp.esy.es/login.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);
                            JSONArray result = jsonObject.getJSONArray("result");
                            JSONObject collegeData = result.getJSONObject(0);

                            if(collegeData.getString("status").equals("success"))
                            {
                                dialog.dismiss();
                                settings = getSharedPreferences(Constants.SHARED_PREF, Context.MODE_PRIVATE);
                                SharedPreferences.Editor edit = settings.edit();
                                edit.putString("username", (user));
                                edit.putString("password", (pass));
                                edit.putString(Constants.UNIQUE_ID, collegeData.getString("firebaseid"));
                                edit.commit();
                             //   Toast.makeText(Login.this, "Login successful!!", Toast.LENGTH_SHORT).show();
                                Toast toast=new Toast(Login.this);
                                toast.setDuration(Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.CENTER,0,0);

                                //toast.setText("Login Successful!!");
                                li=getLayoutInflater();
                                View toastappear=li.inflate(R.layout.toast_layout,(ViewGroup)findViewById(R.id.toast));
                                toast.setView(toastappear);
                                toast.show();
                                startService(new Intent(getBaseContext(), NotificationListener.class));
                                Intent intent=new Intent(Login.this,HomeUser.class);
                                intent.putExtra("username",user);
                                intent.putExtra("password",pass);
                                finish();
                                startActivity(intent);
                            }
                            else{
                                dialog.dismiss();
                                Toast.makeText(Login.this, "Your username or password is incorrect!", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                Toast.makeText(Login.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map= new HashMap<String, String>();
                map.put("username",user);
                map.put("password",pass);


                return map;
            }
        };

        RequestQueue r = Volley.newRequestQueue(getApplicationContext());
        r.add(request);
    }

    private void checkFields() {
        String user,pass;
        user=username.getText().toString().trim();
        pass=password.getText().toString().trim();
        if(user.length()>0 && pass.length()>0) {
            login.setEnabled(true);
            login.setBackgroundColor(Color.WHITE);
            login.setTextColor(Color.BLACK);
        }
        else {
            login.setEnabled(false);
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (animationDrawable != null && !animationDrawable.isRunning())
            animationDrawable.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (animationDrawable != null && animationDrawable.isRunning())
            animationDrawable.stop();
    }
}
