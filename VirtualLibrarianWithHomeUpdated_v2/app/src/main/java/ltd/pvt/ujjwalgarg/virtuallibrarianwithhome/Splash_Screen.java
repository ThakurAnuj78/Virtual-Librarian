package ltd.pvt.ujjwalgarg.virtuallibrarianwithhome;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Splash_Screen extends Activity {

    String PREFRENCES_NAME=Constants.SHARED_PREF;
    SharedPreferences settings;
    String username=null,password=null;

    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Window window = getWindow();
        window.setFormat(PixelFormat.RGBA_8888);
    }
   // Animation fadeIn;
    Thread splashThread;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash__screen);

        StartAnimations();

        /*fadeIn = AnimationUtils.loadAnimation(this,R.anim.fade);
       txt.setVisibility(View.VISIBLE);
       txt.startAnimation(fadeIn);
       new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {

                Intent mainIntent = new Intent(Splash_Screen.this,HomeUser.class);
                startActivity(mainIntent);
                finish();
            }
        }, 4*1000); */
        }
    private void StartAnimations() {
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.fade);
        anim.reset();
        RelativeLayout l=(RelativeLayout) findViewById(R.id.splash__screen);
        l.clearAnimation();
        ImageView img=(ImageView)findViewById(R.id.splash1);
        img.setVisibility(View.VISIBLE);
        l.startAnimation(anim);

        Animation anim1 = AnimationUtils.loadAnimation(this, R.anim.translate);
        anim.reset();
        TextView txt=(TextView)findViewById(R.id.splash);
        Typeface externalFont=Typeface.createFromAsset(getBaseContext().getAssets(),"fonts/logo_font.ttf");
        txt.setTypeface(externalFont);
        txt.clearAnimation();
        txt.setVisibility(View.VISIBLE);
        txt.startAnimation(anim1);
        img.startAnimation(anim);

        splashThread = new Thread() {
            @Override
            public void run() {
                try {
                    int waited = 0;
                    // Splash screen pause time
                    while (waited < 5000) {
                        sleep(100);
                        waited += 100;
                    }
                    settings = getSharedPreferences(PREFRENCES_NAME,
                            Context.MODE_PRIVATE);
                    username = settings.getString("username", "1");
                    password  = settings.getString("password", "1");
                    if(username.equals("1") && password.equals("1")) {
                        Intent intent = new Intent(Splash_Screen.this, Login.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent);
                        Splash_Screen.this.finish();
                    }
                    else{
                        Intent intent = new Intent(Splash_Screen.this, HomeUser.class);
                        startService(new Intent(getBaseContext(),NotificationListener.class));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        intent.putExtra("username",username);
                        intent.putExtra("password",username);
                        startActivity(intent);
                        Splash_Screen.this.finish();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    Splash_Screen.this.finish();
                }

            }
        };
        splashThread.start();

    }

}
