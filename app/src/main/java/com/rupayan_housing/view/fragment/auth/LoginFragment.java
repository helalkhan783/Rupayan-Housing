package com.rupayan_housing.view.fragment.auth;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.provider.Settings;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.google.firebase.messaging.FirebaseMessaging;
import com.rupayan_housing.R;
import com.rupayan_housing.dialog.MyApplication;
import com.rupayan_housing.serverResponseModel.LoginResponse;
import com.rupayan_housing.utils.InternetConnection;
import com.rupayan_housing.view.fragment.BaseFragment;
import com.rupayan_housing.viewModel.LoginViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cc.cloudist.acplibrary.ACProgressCustom;
import es.dmoral.toasty.Toasty;

public class LoginFragment extends BaseFragment {
    private boolean isPasswordVisible = false; // variable to detect whether password is visible or not
    private LoginResponse loginResponse;

    private LoginViewModel loginViewModel;
    private View view;
    @BindView(R.id.phoneEt)
    EditText phone;
    @BindView(R.id.passwordEt)
    EditText password;
    @BindView(R.id.card_login_btn)
    Button loginBtn;
    @BindView(R.id.passwordVisibilityImgBtn)
    ImageButton passwordVisibilityImgBtn;
    ACProgressCustom progressDialog;
    @BindView(R.id.createAccount)
    TextView createAccount;
    @BindView(R.id.forgotPassword)
    TextView forgotPassword;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
// Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, view);


/**
 * for control system onBackPress
 */
// This callback will only be called when MyFragment is at least Started.
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
// Handle the back button event
                MyApplication.exitApp(getActivity());//for show exit app dialog
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);

        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);


        return view;
    }


    @OnClick(R.id.createAccount)
    public void clikCrateAccoune() {
        Navigation.findNavController(getView()).navigate(R.id.action_loginFragment_to_signupFragment);
    }

    @OnClick(R.id.card_login_btn)
    public void loginClickHandle() {
        String phoneVal = phone.getText().toString();
        String passwordVal = password.getText().toString();
        if (phoneVal.isEmpty() || phoneVal.length() < 11) {
            phone.setError("mandatory field");
            phone.requestFocus();
            return;
        }
        if (passwordVal.isEmpty()) {
            password.setError("Password is empty");
            password.requestFocus();
            return;
        }
        if (passwordVal.length() < 6) {
            password.setError("password length should be 6 or more");
            password.requestFocus();
            return;
        }
        hideKeyboard(getActivity());
        userLogin(phoneVal, passwordVal);
    }

    @OnClick(R.id.forgotPassword)
    public void forgotClick() {
        Navigation.findNavController(getView()).navigate(R.id.action_loginFragment_to_forgotpassword);
    }

    private void userLogin(String userName, String password) {


        @SuppressLint("HardwareIds") String deviceId = Settings.Secure.getString(getContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        final String[] mainToken = new String[1];

/**
 * first check the internet
 */
        if (!InternetConnection.isOnline(getActivity())) {
            Navigation.findNavController(getView()).navigate(R.id.action_loginFragment_to_internetConnectionFaildFragment);
            return;
        }

/**
 here send token to server only for push notification
 */
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {//for FCM notification get the device token and send to server
            if (!task.isSuccessful()) {
                Toast.makeText(getContext(), "Fetching FCM registration token failed", Toast.LENGTH_SHORT).show();
                return;
            }
// Get new FCM registration token
            String token = task.getResult();
            Log.d("TOKEN", "" + token);
// Toast.makeText(getContext(), " " + token, Toast.LENGTH_SHORT).show();
            progressDialog = new ACProgressCustom.Builder(getContext())
                    .useImages(R.drawable.app_logo).build();
            progressDialog.setCanceledOnTouchOutside(false);

            if (!(isInternetOn(getActivity()))) {
                infoMessage(getActivity().getApplication(), "Please Check Your Internet Connection");
                return;
            }
            progressDialog.show();
            loginViewModel.sendLoginRequest(getActivity(), userName, password, token).observe(getViewLifecycleOwner(), loginResponse -> {
                progressDialog.dismiss();
                try {
                    if (loginResponse == null) {
                        Toasty.info(getContext(), "didn't find any credential with this number", Toasty.LENGTH_SHORT).show();
                        return;
                    } else {
                        loginResponse = loginResponse;
                        Toasty.success(getContext(), "Login Successful").show();
                        Bundle bundle = new Bundle();
                        bundle.putString("name", loginResponse.getDisplayName());
                        bundle.putString("profileImage", String.valueOf(loginResponse.getProfilePhoto()));
                        bundle.putString("phone", loginResponse.getPhone());
                        Navigation.findNavController(getView()).navigate(R.id.action_loginFragment_to_homeFragment, bundle);
                    }
                } catch (Exception e) {
                    progressDialog.dismiss();
                    Log.d("ERROR", "" + e
                            .getLocalizedMessage());
                }
            });
        });

    }

    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.passwordVisibilityImgBtn)
    public void passwordVisibility() {
        if (isPasswordVisible) {
            passwordVisibilityImgBtn.setImageResource(R.drawable.ic_visibility_off_grey_24dp);
// hide password
            password.setTransformationMethod(PasswordTransformationMethod.getInstance());
            isPasswordVisible = false;
        } else {
            passwordVisibilityImgBtn.setImageResource(R.drawable.ic_visibility_grey_24dp);
// show password
            password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            isPasswordVisible = true;
        }
    }
}