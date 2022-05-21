package com.rupayan_housing.view.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.rupayan_housing.dialog.MyApplication;
import com.rupayan_housing.localDatabase.PreferenceManager;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import es.dmoral.toasty.Toasty;


public abstract class BaseFragment extends Fragment {
    /**
     * For hide KeyBoard
     */
    public void hideKeyboard(FragmentActivity activity) {
        try {
            View view = activity.findViewById(android.R.id.content);
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                assert imm != null;
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        } catch (Exception ignored) {
        }
    }

    public boolean isValidEmail(String email) {
        Pattern VALID_EMAIL_ADDRESS_REGEX =
                Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(email);
        return matcher.find();
    }

    public boolean isValidPhone(String phone) {
        if (!(phone.matches("(^([+]{1}[8]{2}|0088)?(01){1}[3-9]{1}\\d{8})$"))) {
            return false;
        }
        return true;
    }

    /**
     * For Handle conditional backPress
     */
    public void handleBackPressWithDialog(FragmentActivity activity) {
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                MyApplication.exitApp(getActivity());//for show exit app dialog
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
    }

    /**
     * Check Internet Connection
     */
    public boolean isInternetOn(FragmentActivity context) {
        // get Connectivity Manager object to check connection
        ConnectivityManager connec =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        // Check for network connections
        if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {
            return true;
        } else if (
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED) {
            return false;
        }
        return false;
    }


    public String getToken(Application application) {
        return PreferenceManager.getInstance(application).getUserCredentials().getToken();
    }

    public String getUserId(Application application) {
        return PreferenceManager.getInstance(application).getUserCredentials().getUserId();
    }

    public String getVendorId(Application application) {
        return String.valueOf(PreferenceManager.getInstance(application).getUserCredentials().getVendorID());
    }

    public String getProfileTypeId(Application application) {
        return PreferenceManager.getInstance(application).getUserCredentials().getProfileTypeId();
    }

    public String getProfilePhoto(Application application) {
        return String.valueOf(PreferenceManager.getInstance(application).getUserCredentials().getProfilePhoto());
    }

    public String getProfileName(Application application) {
        return PreferenceManager.getInstance(application).getUserCredentials().getFullName();
    }

    public String getPhone(Application application) {
        return PreferenceManager.getInstance(application).getUserCredentials().getPhone();
    }

    public String getEmail(Application application) {
        return String.valueOf(PreferenceManager.getInstance(application).getUserCredentials().getEmail());
    }

    public String getProfileId(Application application) {
        return PreferenceManager.getInstance(application).getUserCredentials().getProfileId();
    }

    public String getSubscriptionEndDate(Application application) {
        return PreferenceManager.getInstance(application).getUserCredentials().getSubscriptionEndDate();
    }


    public void successMessage(Application context, String message) {
        Toasty.success(context, "" + message, Toasty.LENGTH_SHORT).show();
    }

    public void errorMessage(Application context, String error) {
        Toasty.error(context, "Something Wrong Contact to Support \n" + error, Toasty.LENGTH_SHORT).show();
        Log.d("ERROR", error);
    }

    public void infoMessage(Application context, String message) {
        Toasty.info(context, "" + message, Toasty.LENGTH_SHORT).show();
    }

    public void warningMessage(Application context, String message) {
        Toasty.warning(context, "" + message, Toasty.LENGTH_LONG).show();
    }


    public boolean checkStoragePermission() {
        int result = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }


    public void requestStoragePermission(int STORAGE_PERMISSION_REQUEST_CODE) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
            Toast.makeText(getActivity(), "Write External Storage permission allows us to do store images. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_REQUEST_CODE);
        }
    }

    public void getLogoImageFromFile(Application application, int PICK_LOGO_PHOTO) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        // startActivityForResult(intent, PICK_LOGO_PHOTO);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"),
                PICK_LOGO_PHOTO);
    }
    
    
    
    public boolean checkProfileType(){
      String profileTypeId =  getProfileTypeId(getActivity().getApplication());
        String profileId = getProfileId(getActivity().getApplication());
        if (profileTypeId.equals("6") || profileTypeId.equals("7") ){
            return true;
        }else{
           return false;
        }
    }


}
