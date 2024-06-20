package au.gov.defence.royalaustraliannavyfitness;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.material.tabs.TabLayout;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import au.gov.defence.royalaustraliannavyfitness.Fragment.AskPTIFragment;
import au.gov.defence.royalaustraliannavyfitness.Fragment.InformationFragment;
import au.gov.defence.royalaustraliannavyfitness.Fragment.ProfileFragment;
import au.gov.defence.royalaustraliannavyfitness.Fragment.WorkoutFragment;
import au.gov.defence.royalaustraliannavyfitness.Util.NonSwipeAbleViewPager;


public class TabbarActivity extends AppCompatActivity  {

    private TabLayout tabLayout;
    private NonSwipeAbleViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;



    private final int[] tabIcons = {
            R.drawable.anchor_white,
            R.drawable.navyclub_white,
            R.drawable.avtar_white,
            R.drawable.chat_white,

    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabbar);


        //ViewPager
        viewPager = findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        viewPager.setOffscreenPageLimit(5);


        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();

        viewPager.setCurrentItem(0);



    }



    private void setupTabIcons() {

        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
        tabLayout.getTabAt(3).setIcon(tabIcons[3]);



    }

    private void setupViewPager(ViewPager viewPager) {

        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFrag(new InformationFragment());
        viewPagerAdapter.addFrag(new WorkoutFragment());
        viewPagerAdapter.addFrag(new ProfileFragment());
        viewPagerAdapter.addFrag(new AskPTIFragment());

        viewPager.setAdapter(viewPagerAdapter);

    }

    static class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();


        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {


            return mFragmentList.get(position);
        }

        @Override
        public int getItemPosition(@NonNull @NotNull Object object) {
            return POSITION_NONE;
        }

        @Override
        public int getCount() {

            return mFragmentList.size();
        }



        public void addFrag(Fragment fragment) {
            mFragmentList.add(fragment);

        }



    }




}

