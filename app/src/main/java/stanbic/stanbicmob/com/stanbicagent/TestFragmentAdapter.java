package stanbic.stanbicmob.com.stanbicagent;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;



class TestFragmentAdapter extends FragmentPagerAdapter  {
    protected static final String[] CONTENT = new String[] { "This", "Is", "A", "Test", };
    protected static final int[] ICONS = new int[] {

    };

    private int mCount = 2;
    private Context context;
    SessionManagement session;
    public TestFragmentAdapter(FragmentManager fm,Context ctx) {

        super(fm);
        this.context = ctx;
    }

    @Override
    public Fragment getItem(int position) {

        if(position == 0) // if the position is 0 we are returning the First tab
        {
            LayoutThree tab1 = new LayoutThree();
            return tab1;
        }

        else          // As we are having 2 tabs if the position is now 0 it must be 1 so we are returning second tab
        {
            LayoutThree tab1 = new LayoutThree();
            return tab1;
        }


      /*  return TestFragment.newInstance(CONTENT[position % CONTENT.length]);*/
    }

   /* @Override
    public Fragment getItem(int position) {

        if(position == 0) // if the position is 0 we are returning the First tab
        {
          LayoutFive tab1 = new LayoutFive();
            return tab1;
        }
        else if (position == 1) // if the position is 0 we are returning the First tab
        {
            LayoutTwo tab2 = new LayoutTwo();
            return tab2;
        }
        else if (position == 2) // if the position is 0 we are returning the First tab
        {
            LayoutThree tab3 = new LayoutThree();
            return tab3;
        }else if (position == 3) // if the position is 0 we are returning the First tab
        {
            LayoutFour tab4 = new LayoutFour();
            return tab4;
        } else          // As we are having 2 tabs if the position is now 0 it must be 1 so we are returning second tab
        {
            LayoutOne tab5 = new LayoutOne();
            return tab5;
        }


      *//*  return TestFragment.newInstance(CONTENT[position % CONTENT.length]);*//*
    }*/





    @Override
    public int getCount()


    {

        return mCount;
    }

    @Override
    public CharSequence getPageTitle(int position) {
      return TestFragmentAdapter.CONTENT[position % CONTENT.length];
    }


    public void setCount(int count) {
        if (count > 0 && count <= 10) {
            mCount = count;
            notifyDataSetChanged();
        }
    }
}