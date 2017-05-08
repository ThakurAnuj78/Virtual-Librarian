package ltd.pvt.ujjwalgarg.virtuallibrarianwithhome;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
public class HomeUser extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, SearchView.OnQueryTextListener {
    private Gallery gallery,gall;
    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    private SearchView searchView;
    private String username,password;
    private Calendar calender;
    ImageView imag;
    private static final Integer[] IMAGES= {R.drawable.financialfull,R.drawable.two,R.drawable.three,R.drawable.five};
    private ArrayList<Integer> ImagesArray = new ArrayList<Integer>();
    private Integer img[]={R.drawable.let_us_c,R.drawable.algo,R.drawable.coa,R.drawable.data_structures};
    private Integer img2[]={R.drawable.optics,R.drawable.materail_sci,R.drawable.nuclear_phy,R.drawable.quantum};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_user);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        NavigationView navigationView=(NavigationView)findViewById(R.id.nav_view);
        View headerview=navigationView.getHeaderView(0);
        TextView txtview=(TextView)headerview.findViewById(R.id.userr1);
        txtview.setText(username);
        calender = Calendar.getInstance();
        calender.set(Calendar.HOUR_OF_DAY,1);
        calender.set(Calendar.MINUTE,00);
        calender.set(Calendar.SECOND,30);
        imag=(ImageView)headerview.findViewById(R.id.imageView1);
        RelativeLayout relativeLayout=(RelativeLayout)headerview.findViewById(R.id.imgrel);
        username=getIntent().getStringExtra("username");
        password=getIntent().getStringExtra("password");
        txtview.setText(username);
        searchView = (SearchView) findViewById(R.id.search);
        searchView.setOnQueryTextListener(this);
        Button btn=(Button)findViewById(R.id.but);
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in=new Intent(HomeUser.this, MyProfile.class);
                in.putExtra("username",username);
                in.putExtra("password",password);
                startActivity(in);
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(HomeUser.this, ExListView.class);
                startActivity(i);
            }
        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        assert drawer != null;
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        assert navigationView != null;
        navigationView.setNavigationItemSelectedListener(this);
        Wall wall=new Wall();
        Wall2 wall2=new Wall2();
        gallery= (Gallery) findViewById(R.id.gallery1);
        gall= (Gallery) findViewById(R.id.gallery2);
        gallery.setAdapter(wall);
        gall.setAdapter(wall2);
        init();
        function(username);

    }

    void function(String string){
        Intent intent= new Intent(HomeUser.this,NotificationReceiver.class);
        intent.putExtra("Text", string);
        PendingIntent pendingintent =PendingIntent.getBroadcast(HomeUser.this,100,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager=(AlarmManager)getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calender.getTimeInMillis(),AlarmManager.INTERVAL_FIFTEEN_MINUTES,pendingintent);
    }

    private void init() {
        for(int i=0;i<IMAGES.length;i++)
            ImagesArray.add(IMAGES[i]);

        mPager = (ViewPager) findViewById(R.id.pager);


        mPager.setAdapter(new SlidingImage_Adapter(HomeUser.this,ImagesArray));


        CirclePageIndicator indicator = (CirclePageIndicator)
                findViewById(R.id.indicator);

        indicator.setViewPager(mPager);

        final float density = getResources().getDisplayMetrics().density;

//Set circle indicator radius
        indicator.setRadius(4 * density);

        NUM_PAGES =IMAGES.length;

        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 2200, 2200);

        // Pager listener over indicator
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                currentPage = position;

            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int pos) {

            }
        });

    }

    public void scanBarcode(){
        Intent intent = new Intent(this,ScanBarcodeActivity.class);
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 0){
            if(resultCode == CommonStatusCodes.SUCCESS){
                if(data != null){
                    Barcode barcode = data.getParcelableExtra("barcode");
                    Intent intent = new Intent(this,BookIssue.class);
                    intent.putExtra("Barcode",barcode.displayValue);
                    intent.putExtra("Username",username);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(this, "No barcode found", Toast.LENGTH_SHORT).show();;
                }
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        Intent i=new Intent(HomeUser.this,Search.class);
        i.putExtra("search",query);
        startActivity(i);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    public class Wall extends BaseAdapter {

        @Override
        public int getCount() {
            return img.length;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView i=new ImageView(HomeUser.this);
            i.setImageResource(img[position]);
            return i;
        }
    }
    public class Wall2 extends BaseAdapter {

        @Override
        public int getCount() {
            return img2.length;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView i=new ImageView(HomeUser.this);
            i.setImageResource(img2[position]);
            return i;
        }
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        assert drawer != null;
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if (id==R.id.wishlist_menu)
        {
            Intent intent=new Intent(HomeUser.this,Wishlist.class);
            intent.putExtra("username",username);
            startActivity(intent);

            //intent to wishlist
        }
        else if(id==R.id.scan_menu)
        {
            scanBarcode();
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.scan) {
            scanBarcode();

        } else if (id == R.id.shelf) {
            Intent intent=new Intent(HomeUser.this,CurrentlyBooksIssued.class);
            intent.putExtra("username",username);
            startActivity(intent);

        } else if (id == R.id.wishlist) {
            Intent intent=new Intent(HomeUser.this,Wishlist.class);
            intent.putExtra("username",username);
            startActivity(intent);


        }else if (id == R.id.history) {
            Intent intent=new Intent(HomeUser.this,History.class);
            intent.putExtra("username",username);
            startActivity(intent);

        }
        else if (id == R.id.about_us) {
            Intent intent=new Intent(HomeUser.this,About_Us.class);
            startActivity(intent);

        }
        else if (id == R.id.rate_app) {
            Intent intent=new Intent(HomeUser.this,AppRating.class);
            intent.putExtra("username",username);
            startActivity(intent);

        }else if (id == R.id.contact_us) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        assert drawer != null;
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}