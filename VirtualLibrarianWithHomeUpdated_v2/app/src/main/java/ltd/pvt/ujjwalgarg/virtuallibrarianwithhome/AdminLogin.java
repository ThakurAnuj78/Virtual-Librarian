package ltd.pvt.ujjwalgarg.virtuallibrarianwithhome;

        import android.app.ProgressDialog;
        import android.content.Intent;
        import android.graphics.drawable.AnimationDrawable;
        import android.os.Bundle;
        import android.support.annotation.Nullable;
        import android.support.v4.app.Fragment;
        import android.support.v7.app.AppCompatActivity;
        import android.text.Editable;
        import android.text.TextWatcher;
        import android.text.method.HideReturnsTransformationMethod;
        import android.text.method.PasswordTransformationMethod;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.Button;
        import android.widget.CheckBox;
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

        import java.util.HashMap;
        import java.util.Map;

public class AdminLogin extends AppCompatActivity {

    EditText adminname;
    EditText pass;
    Button login;
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
        setContentView(R.layout.activity_admin_login);

        relativeLayout= (RelativeLayout) findViewById(R.id.admin);
        animationDrawable= (AnimationDrawable) relativeLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(1500);

        adminname= (EditText) findViewById(R.id.adminusername);
        pass= (EditText) findViewById(R.id.adminpass);
        login= (Button) findViewById(R.id.adminlogin);
        final CheckBox checkBox=(CheckBox)findViewById(R.id.show);
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkBox.isChecked()){
                    //    passwrd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    pass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    // showPass.setTextColor(Color.RED);
                    c++;
                }
                else {
                    pass.setTransformationMethod(PasswordTransformationMethod.getInstance());

                    //showPass.setTextColor(getResources().getColor(R.color.green));
                    c++;
                }

            }
        });

        adminname.addTextChangedListener(textWatcher);
        pass.addTextChangedListener(textWatcher);
        checkFields();
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String us, ps, ph;
                ps = pass.getText().toString().trim();
                if (ps.length() < 6 || ps.length() > 30) {
                    Toast.makeText(AdminLogin.this, "Password length must be between 6 & 30 characters!", Toast.LENGTH_SHORT).show();
                }
                else
                adminLogin();
            }

        });
    }

    void adminLogin(){
        final String user=adminname.getText().toString();
        final String password = pass.getText().toString();
        final ProgressDialog dialog=new ProgressDialog(AdminLogin.this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setTitle("Please wait!!");
        dialog.setMessage("Loading data");
        dialog.show();
        StringRequest request = new StringRequest(Request.Method.POST, "http://libraryapp.esy.es/admin.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equals("success"))
                        {
                            dialog.dismiss();
                           // Toast.makeText(AdminLogin.this, "Login successful!!", Toast.LENGTH_SHORT).show();
                            Toast toast=new Toast(AdminLogin.this);
                            toast.setDuration(Toast.LENGTH_LONG);
                            //toast.setText("Login Successful!!");
                            LayoutInflater li=getLayoutInflater();
                            View toastappear=li.inflate(R.layout.toast_layout,(ViewGroup)findViewById(R.id.toast));
                            toast.setView(toastappear);
                            toast.show();
                            Intent intent = new Intent(AdminLogin.this,AdminHome.class);
                            finish();
                            startActivity(intent);
                        }
                        else{
                            dialog.dismiss();
                            Toast.makeText(AdminLogin.this, response, Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                Toast.makeText(AdminLogin.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map= new HashMap<String, String>();
                map.put("username",user);
                map.put("password", password);

                return map;
            }
        };
        RequestQueue r = Volley.newRequestQueue(getApplicationContext());
        r.add(request);
    }

    public void checkFields() {
        String us,ps;
        us=adminname.getText().toString().trim();
        ps=pass.getText().toString().trim();
        if(us.length()>0 && ps.length()>0) {
            login.setEnabled(true);
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
