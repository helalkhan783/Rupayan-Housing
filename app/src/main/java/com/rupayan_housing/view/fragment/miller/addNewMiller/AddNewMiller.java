package com.rupayan_housing.view.fragment.miller.addNewMiller;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.rupayan_housing.R;
import com.rupayan_housing.view.fragment.BaseFragment;

import org.jetbrains.annotations.NotNull;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class AddNewMiller extends BaseFragment {

    private View view;
    private TabLayout tabLayout;
    public static ViewPager viewPager;
    public static boolean isTabPermission = false;
    @BindView(R.id.toolbarTitle)
    TextView toolbar;
    public static String portion;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_add_new_miller, container, false);
        ButterKnife.bind(this, view);
        getPreviousFragmentData();
        toolbar.setText("Add New Mill Profile");
        init();
        /**
         * add tab
         */
        tabLayout.addTab(tabLayout.newTab().setText("Profile Info"));
        tabLayout.addTab(tabLayout.newTab().setText("Owner Info"));
        tabLayout.addTab(tabLayout.newTab().setText("License Info"));
        tabLayout.addTab(tabLayout.newTab().setText("QC Info"));
        tabLayout.addTab(tabLayout.newTab().setText("Employee Info"));
        if (isTabPermission) {
            tabLayout.setSelected(true);
        } else {
            tabLayout.setSelected(false);
        }

        if (!isTabPermission) {
            viewPager.setOnTouchListener((v, event) -> true);
        }


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (isTabPermission) {
                    viewPager.setCurrentItem(tab.getPosition());
                    tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
                }
                if (!isTabPermission) {
                    if (tab.getPosition() != 0) {
                       //infoMessage(getActivity().getApplication(), "Please Add Your Profile Information");
                    }
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        //below inside fragment we use getChildFragmentManager()
        FragmentPagerAdapter fragmentPagerAdapter = new FragmentPagerAdapter(getChildFragmentManager()) {
            @NonNull
            @NotNull
            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        return new MillerProfileInformation();
                    case 1:
                        return new CompanyOwnerInfo();
                    case 2:
                        return new MillerLicenseInfo();
                    case 3:
                        return new MillerQcInformation();
                    case 4:
                        return new MillerEmployeeInformation();
                }
                return null;
            }

            @Override
            public int getCount() {
                return 5;
            }
        };
        viewPager.setAdapter(fragmentPagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));//atar mane hosse fragment scroll er sathe tab o change hobe.


        if (isTabPermission) {
            //if you want to change something new below this is the function
            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    //if you want to change something new
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        }
        return view;
    }

    private void getPreviousFragmentData() {
        try {
            portion = getArguments().getString("OWNER_INFO_EDIT");
        } catch (Exception e) {
        }
    }


    private void init() {
        viewPager = view.findViewById(R.id.viewPager);
        tabLayout = view.findViewById(R.id.tabLayout);
    }

    @OnClick(R.id.backbtn)
    public void backBtnClick() {
        isTabPermission = false;
        hideKeyboard(getActivity());
        getActivity().onBackPressed();
    }
}