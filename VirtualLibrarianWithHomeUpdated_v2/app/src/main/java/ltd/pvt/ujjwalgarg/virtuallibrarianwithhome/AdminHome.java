package ltd.pvt.ujjwalgarg.virtuallibrarianwithhome;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class AdminHome extends AppCompatActivity implements ActionBar.TabListener {

    ViewPager viewPager;
    MyAdapter myAdapter;
    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_home);
        viewPager=(ViewPager)findViewById(R.id.myviewpager);
        myAdapter=new MyAdapter(getSupportFragmentManager());
        viewPager.setAdapter(myAdapter);
        actionBar=getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.addTab(actionBar.newTab().setText("Book Return").setTabListener(this));
        actionBar.addTab(actionBar.newTab().setText("Book Upload").setTabListener(this));
        actionBar.addTab(actionBar.newTab().setText("Issued Today").setTabListener(this));
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.admin_menu,menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.admin_logout){
            Intent intent=new Intent(AdminHome.this,AdminLogin.class);
            finish();
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        viewPager.setCurrentItem(tab.getPosition());

    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }
}
