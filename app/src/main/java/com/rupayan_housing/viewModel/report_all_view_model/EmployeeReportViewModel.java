package com.rupayan_housing.viewModel.report_all_view_model;

import android.util.Log;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;

import com.rupayan_housing.retrofit.RetrofitClient;
import com.rupayan_housing.serverResponseModel.MillReportResponse;
import com.rupayan_housing.serverResponseModel.MillerEmployeeReportRespons;
import com.rupayan_housing.utils.CustomViewModel;
import com.rupayan_housing.view.fragment.all_report.employee_report.list.EmployeeReportListResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmployeeReportViewModel extends CustomViewModel {
    /** for list */
public  MutableLiveData<MillerEmployeeReportRespons> getEmployeeReportList(FragmentActivity context, String associationId, String millerId ){
        MutableLiveData<MillerEmployeeReportRespons> liveData = new MutableLiveData<>();

        String token =getToken(context.getApplication());

        Call<MillerEmployeeReportRespons> call = RetrofitClient.getInstance().getApi(). getEmployeeReportList(token,getUserId(context.getApplication()), associationId,millerId);
        call.enqueue(new Callback<MillerEmployeeReportRespons>() {
            @Override
            public void onResponse(Call<MillerEmployeeReportRespons> call, Response<MillerEmployeeReportRespons> response) {
                if (response.isSuccessful()){
                    if (response == null){
                        liveData.setValue(null);
                        return;
                    }
                    if (response.body().getStatus() == 400){
                        liveData.setValue(response.body());
                        return;}
                    if (response.body().getStatus() == 200){
                        liveData.setValue(response.body());
                        return;
                    }
                }
            }

            @Override
            public void onFailure(Call<MillerEmployeeReportRespons> call, Throwable t) {
                Log.d("ERROR", "onFailure: "+t.getMessage());
                liveData.setValue(null);
            }
        });

        return  liveData;

    }

    public  MutableLiveData<MillReportResponse> getMillReportList(FragmentActivity context,String startDate,String endDate, String associationId, String millerId, String processTYpe, String millType, String millStatus){
        MutableLiveData<MillReportResponse> liveData = new MutableLiveData<>();

        String token =getToken(context.getApplication());

        Call<MillReportResponse> call = RetrofitClient.getInstance().getApi(). getMillReport(token,getUserId(context.getApplication()),startDate,endDate, associationId,millerId,processTYpe,millType,millStatus);
        call.enqueue(new Callback<MillReportResponse>() {
            @Override
            public void onResponse(Call<MillReportResponse> call, Response<MillReportResponse> response) {
                if (response.isSuccessful()){
                    if (response == null){
                        liveData.setValue(null);
                        return;
                    }
                    if (response.body().getStatus() == 400){
                        liveData.setValue(response.body());
                        return;}
                    if (response.body().getStatus() == 200){
                        liveData.setValue(response.body());
                        return;
                    }
                }
            }

            @Override
            public void onFailure(Call<MillReportResponse> call, Throwable t) {
                Log.d("ERROR", "onFailure: "+t.getMessage());
                liveData.setValue(null);
            }
        });

        return  liveData;

    }

}
