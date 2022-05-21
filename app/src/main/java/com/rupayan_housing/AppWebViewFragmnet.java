package com.rupayan_housing;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebViewClient;

import com.rupayan_housing.databinding.FragmentAppWebViewFragmnetBinding;
import com.rupayan_housing.utils.CrispUtil;
import com.rupayan_housing.view.fragment.BaseFragment;

import im.crisp.client.ChatActivity;
import im.crisp.client.Crisp;


public class AppWebViewFragmnet extends BaseFragment {
    FragmentAppWebViewFragmnetBinding binding;
    String url, pageName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_app_web_view_fragmnet, container, false);
        getPreviousData();
        binding.toolbar.toolbarTitle.setText("" + pageName);
        if (pageName.equals("Privacy") || pageName.equals("Privacy Policy") || pageName.equals("Terms") || pageName.equals("Help Center")) {
            binding.bottomNavigation.setVisibility(View.GONE);
            binding.aboutLayout.setVisibility(View.GONE);
            binding.webLayout.setVisibility(View.VISIBLE);

            try {
                WebSettings webSettings = binding.webview.getSettings();
                webSettings.setJavaScriptEnabled(true);
                binding.webview.setWebViewClient(new WebViewClient());
                binding.webview.loadUrl(url);
            } catch (Exception e) {
                Log.d("ERROR", e.getMessage());
            }

        }
        if (pageName.equals("About")) {
            binding.webLayout.setVisibility(View.GONE);
            binding.aboutLayout.setVisibility(View.VISIBLE);
            try {
                PackageInfo packageInfo = getContext().getPackageManager().getPackageInfo(getContext().getPackageName(),0);
         String name = packageInfo.versionName;
         binding.versionName.setText("Current Version -"+name);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

        }
        binding.toolbar.setClickHandle(() -> getActivity().onBackPressed());


        binding.bottomNavigation.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.dayBook:
                    Bundle bundle = new Bundle();
                    bundle.putString("portion", "Inbox");
                    Navigation.findNavController(getView()).navigate(R.id.action_AppWebViewFragmnet_to_managementFragment, bundle);
                    break;
                case R.id.home:
                    Navigation.findNavController(getView()).navigate(R.id.action_AppWebViewFragmnet_to_homeFragment);
                    break;
                case R.id.chat:
                    /**
                     * For open chat window
                     */
                    Crisp.configure(getContext(), CrispUtil.crispSecretKey);
                    Intent crispIntent = new Intent(getActivity(), ChatActivity.class);
                    startActivity(crispIntent);
                    break;
            }
            return true;
        });

        return binding.getRoot();
    }

    private void getPreviousData() {
        url = getArguments().getString("url");
        pageName = getArguments().getString("pageName");
    }

}