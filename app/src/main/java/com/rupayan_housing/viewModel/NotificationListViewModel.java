package com.rupayan_housing.viewModel;

import android.app.ProgressDialog;
import android.util.Log;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.rupayan_housing.localDatabase.PreferenceManager;
import com.rupayan_housing.retrofit.RetrofitClient;
import com.rupayan_housing.serverResponseModel.NotificationResponse;

import org.jetbrains.annotations.NotNull;

import lombok.AllArgsConstructor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@AllArgsConstructor
public class NotificationListViewModel extends ViewModel {
    /**
     * for get notification list list
     *
     * @param vendorID
     */
    public MutableLiveData<NotificationResponse> apiCallForGetNotificationList(FragmentActivity context, String pageNumber,String token, String vendorID,String status,String date) {
        MutableLiveData<NotificationResponse> liveData = new MutableLiveData<>();

        String userId = PreferenceManager.getInstance(context).getUserCredentials().getUserId();


        Call<NotificationResponse> call = RetrofitClient.getInstance().getApi().getNotificationList(token,pageNumber, vendorID, userId,status,date);
        call.enqueue(new Callback<NotificationResponse>() {
            @Override
            public void onResponse(@NotNull Call<NotificationResponse> call, @NotNull Response<NotificationResponse> response) {
                if (response.isSuccessful()) {
                    try {
                        if (response == null) {
                            liveData.postValue(null);
                            return;
                        }
                        if (response.body().getStatus() == 400) {
                            liveData.postValue(response.body());
                            return;
                        }
                        liveData.postValue(response.body());

                    } catch (Exception e) {
                        Log.d("ERROR", "" + e.getMessage());
                        liveData.postValue(null);
                    }

                }
            }

            @Override
            public void onFailure(@NotNull Call<NotificationResponse> call, @NotNull Throwable t) {
                Log.d("ERROR", t.getMessage());
                liveData.postValue(null);
                Toast.makeText(context, "Something Wrong Contact to Support \n" + this.getClass().getSimpleName() + " " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        return liveData;
    }

}