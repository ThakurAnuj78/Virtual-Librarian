package ltd.pvt.ujjwalgarg.virtuallibrarianwithhome;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
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

public class Register extends AppCompatActivity{
    EditText username;
    EditText password;
    EditText phone;
    Button login;
    String uniqueId;
    SharedPreferences settings;
    String PREFERENCES_NAME=Constants.SHARED_PREF;
    AnimationDrawable animationDrawable;
    RelativeLayout relativeLayout;
    int c=0;

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
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        relativeLayout= (RelativeLayout) findViewById(R.id.userreg);
        animationDrawable= (AnimationDrawable) relativeLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(1500);


        Firebase.setAndroidContext(getApplicationContext());
        username= (EditText) findViewById(R.id.u_username);
        password= (EditText) findViewById(R.id.u_pass);
        phone= (EditText) findViewById(R.id.u_phone);
        login= (Button) findViewById(R.id.u_login);

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


        username.addTextChangedListener(textWatcher);
        password.addTextChangedListener(textWatcher);
        phone.addTextChangedListener(textWatcher);

        checkFields();
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String us,ps,ph;
                us=username.getText().toString().trim();
                ps=password.getText().toString().trim();
                ph=phone.getText().toString().trim();
                if(ps.length() < 6 || ps.length() > 30)
                {
                    Toast.makeText(Register.this, "Password length must be between 6 & 30 characters!", Toast.LENGTH_SHORT).show();
                }
                else if(ph.length()!=10){
                    Toast.makeText(Register.this, "Phone Number must of 10 digits!", Toast.LENGTH_SHORT).show();
                }
                else
                userRegister();
            }
        });
    }

    String registerDevice(){
        Firebase firebase = new Firebase(Constants.FIREBASE_APP);
        Firebase newFirebase = firebase.push();
        Map<String, String> val = new HashMap<>();
        val.put("msg", "none");
        newFirebase.setValue(val);
        String uniqueId = newFirebase.getKey();
        return uniqueId;
    }

    void userRegister(){
        final String user=username.getText().toString();
        final String pass=password.getText().toString();
        final String phn=phone.getText().toString();
        final ProgressDialog dialog=new ProgressDialog(Register.this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setTitle("Please wait!!");
        dialog.setMessage("Registering user");
        dialog.show();
        uniqueId = registerDevice();
        StringRequest request = new StringRequest(Request.Method.POST, "http://libraryapp.esy.es/register.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                            if (response.equals("success")) {
                                dialog.dismiss();
                                settings = getSharedPreferences(Constants.SHARED_PREF, Context.MODE_PRIVATE);
                                SharedPreferences.Editor edit = settings.edit();
                                edit.putString("username", (user));
                                edit.putString("password", (pass));
                                edit.putString(Constants.UNIQUE_ID, uniqueId);
                                edit.commit();
                               // Toast.makeText(Register.this, "User Registered successfully!!", Toast.LENGTH_SHORT).show();
                                Toast toast=new Toast(Register.this);
                                toast.setDuration(Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.CENTER,0,0);
                                //toast.setText("Login Successful!!");
                                LayoutInflater li=getLayoutInflater();
                                View toastappear=li.inflate(R.layout.toast_layout1,(ViewGroup)findViewById(R.id.toast1));
                                toast.setView(toastappear);
                                toast.show();
                                startService(new Intent(getBaseContext(), NotificationListener.class));
                                Intent intent = new Intent(Register.this, HomeUser.class);
                                intent.putExtra("username", user);
                                intent.putExtra("password",pass);
                                finish();
                                startActivity(intent);
                            } else {
                                dialog.dismiss();
                                Toast.makeText(Register.this, response, Toast.LENGTH_LONG).show();
                            }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                Toast.makeText(Register.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map= new HashMap<String, String>();
                map.put("username",user);
                map.put("password",pass);
                map.put("phone",phn);
                map.put("firebaseid",uniqueId);
                return map;
            }
        };
        RequestQueue r = Volley.newRequestQueue(getApplicationContext());
        r.add(request);
    }

    public void checkFields() {
        String us,ps,ph;
        us=username.getText().toString().trim();
        ps=password.getText().toString().trim();
        ph=phone.getText().toString().trim();
        if(us.length()>0 && ps.length()>0 && ph.length()>0) {
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
