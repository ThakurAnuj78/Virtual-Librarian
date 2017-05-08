package ltd.pvt.ujjwalgarg.virtuallibrarianwithhome;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Naman on 22-Nov-16.
 */

public class MyAdapter extends FragmentPagerAdapter {
   /* public MyAdapter(FragmentManager fm)
    {
        super(fm);
    }*/

    public MyAdapter(android.support.v4.app.FragmentManager supportFragmentManager) {
        super(supportFragmentManager);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new BookReturn();
            case 1:
                return new BookUpload();
            case 2:
                return new Books_Issued_Today();
        }

        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
