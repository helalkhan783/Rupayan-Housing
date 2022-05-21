package com.rupayan_housing.view.fragment;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import androidx.multidex.BuildConfig;
import androidx.navigation.Navigation;


import com.rupayan_housing.R;
import com.rupayan_housing.dialog.MyApplication;
import com.rupayan_housing.localDatabase.PreferenceManager;
import com.rupayan_housing.retrofit.RetrofitClient;
import com.rupayan_housing.serverResponseModel.CheckUserResponse;
import com.rupayan_housing.serverResponseModel.GetVersionResponse;
import com.rupayan_housing.utils.InternetConnection;


import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashScreen extends BaseFragment {
    private View view;
    public static String verSionName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_splash_screen, container, false);
        ButterKnife.bind(this, view);
 // getVersionName();
        /**
         * at first check the internet connection
         */
        new Handler().postDelayed(() -> {
            try {
                /**
                 * Check application version status
                 */
                applicationUpdateControl();

            } catch (Exception e) {
                Navigation.findNavController(getView()).navigate(R.id.action_splashScreen_to_loginFragment);
            }
        }, 1500);


        return view;
    }

    private void getVersionName() {
        VersionChecker versionChecker = new VersionChecker();
        try {
            verSionName = versionChecker.execute().get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    private void applicationUpdateControl() {
        try {
            PackageInfo packageInfo = getContext().getPackageManager()
                    .getPackageInfo(getContext().getPackageName(), 0);
            String packageName = packageInfo.packageName;
            String versionName = packageInfo.versionName;
            if (!InternetConnection.isOnline(getActivity())) {
                Navigation.findNavController(getView()).navigate(R.id.action_splashScreen_to_internetConnectionFaildFragment);
                return;
            }

            RetrofitClient.getInstance().getApi().getVersionHistory(versionName)
                    .enqueue(new Callback<GetVersionResponse>() {
                        @Override
                        public void onResponse(Call<GetVersionResponse> call, Response<GetVersionResponse> response) {
                            try {
                                if (response.body() == null) {
                                    errorMessage(getActivity().getApplication(), "Something Wrong");
                                    return;
                                }
                                if (response.body().getStatus() == 400) {
                                    infoMessage(getActivity().getApplication(), "" + response.body().getMessage());
                                    return;
                                }
                                if (response.body().getStatus() == 200) {

                                  /*  if (verSionName != null) {
                                        if (!versionName.equals(verSionName)) {
                                            // normal update
                                            if (response.body().getIsMaintanance() == 1) {
                                                MyApplication.updateAppDialog(getActivity(), response, packageName, versionName);
                                                return;
                                            }
                                            // force update
                                            if (response.body().getIsForceUpdate() == 1) {

                                               MyApplication.updateAppDialog(getActivity(), response, packageName, versionName);
                                                return;
                                            }
                                        }
                                    }
*/
                                    apiCallForDetectUser(getActivity());
                                }
                            }catch (Exception e) {
                                Log.e("ERROR", e.getLocalizedMessage());
                            }

                        }

                        @Override
                        public void onFailure(Call<GetVersionResponse> call, Throwable t) {

                        }
                    });

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();

        }
    }


    public void apiCallForDetectUser(FragmentActivity context) {


        if (!InternetConnection.isOnline(context)) {


            Navigation.findNavController(getView()).navigate(R.id.action_splashScreen_to_internetConnectionFaildFragment);

            return;
        }

        if (PreferenceManager.getInstance(context).getUserCredentials() == null) {
            Navigation.findNavController(getView()).navigate(R.id.action_splashScreen_to_loginFragment);
            return;
        }

        Call<CheckUserResponse> call = RetrofitClient.getInstance().getApi().checkUserToken(
                PreferenceManager.getInstance(context).getUserCredentials().getToken(),//token
                PreferenceManager.getInstance(context).getUserCredentials().getVendorID(),//vendorID
                PreferenceManager.getInstance(context).getUserCredentials().getUserId());
        call.enqueue(new Callback<CheckUserResponse>() {
            @Override
            public void onResponse(Call<CheckUserResponse> call, Response<CheckUserResponse> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().getStatus() == 200) {
                        if (PreferenceManager.getInstance(getContext()).getUserCredentials().getToken() != null) {
                            Bundle bundle = new Bundle();
                            bundle.putString("name", PreferenceManager.getInstance(getContext()).getUserCredentials().getDisplayName());
                            bundle.putString("profileImage", String.valueOf(PreferenceManager.getInstance(getContext()).getUserCredentials().getProfilePhoto()));
                            bundle.putString("phone", PreferenceManager.getInstance(getContext()).getUserCredentials().getPhone());
                            Navigation.findNavController(getView()).navigate(R.id.action_splashScreen_to_homeFragment, bundle);
                        }
                    }
                    if (response.body().getStatus() == 500) {
                        Navigation.findNavController(getView()).navigate(R.id.action_splashScreen_to_loginFragment);
                    }

                }
                if (response.body().getStatus() == 400) {//means if Expired token and response is 400 the go to login  activity
                    getActivity().runOnUiThread(() -> PreferenceManager.getInstance(getContext()).deleteUserCredentials());
                    getActivity().runOnUiThread(() -> PreferenceManager.getInstance(getContext()).deleteUserPermission());
                    Navigation.findNavController(getView()).navigate(R.id.action_splashScreen_to_loginFragment);
                }
            }

            @Override
            public void onFailure(Call<CheckUserResponse> call, Throwable t) {
                getActivity().runOnUiThread(() -> PreferenceManager.getInstance(getContext()).deleteUserCredentials());
                Navigation.findNavController(getView()).navigate(R.id.action_splashScreen_to_loginFragment);
            }
        });
    }
}