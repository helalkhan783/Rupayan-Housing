package com.rupayan_housing.viewModel;

import android.util.Log;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.rupayan_housing.localDatabase.PreferenceManager;
import com.rupayan_housing.retrofit.RetrofitClient;
import com.rupayan_housing.serverResponseModel.AddNewUserResponse;
import com.rupayan_housing.serverResponseModel.DuePaymentResponse;
import com.rupayan_housing.serverResponseModel.EditUserDataResponse;
import com.rupayan_housing.serverResponseModel.UserListResponse;
import com.rupayan_housing.serverResponseModel.UserTrashListResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import lombok.Data;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.PUT;

public class UserViewModel extends ViewModel {
    public MutableLiveData<AddNewUserResponse> getAddNewUserPageData(FragmentActivity context) {
        MutableLiveData<AddNewUserResponse> liveData = new MutableLiveData<>();

        String vendorId = PreferenceManager.getInstance(context).getUserCredentials().getVendorID();
        String token = PreferenceManager.getInstance(context).getUserCredentials().getToken();

        Call<AddNewUserResponse> call = RetrofitClient.getInstance().getApi().getAddNewUserPageData(token, vendorId);


        call.enqueue(new Callback<AddNewUserResponse>() {
            @Override
            public void onResponse(Call<AddNewUserResponse> call, Response<AddNewUserResponse> response) {
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
                        Log.e("ERROR", e.getMessage());
                        liveData.postValue(null);
                    }
                }
            }

            @Override
            public void onFailure(Call<AddNewUserResponse> call, Throwable t) {
                liveData.postValue(null);
            }
        });
        return liveData;
    }

    public MutableLiveData<DuePaymentResponse> submitAddNewUserInfo(
            FragmentActivity context, String designation, String departmentID, String Title,
            String DisplayName, String FullName, String Gender, String PrimaryMobile, String email, String storeID,
            String about, String dateOfBirth, String BloodGroup, String nationality, String alternativeEmail,
            String otherContactNumbers, String website, String joiningDate, MultipartBody.Part user_photo, String password
    ) {
        MutableLiveData<DuePaymentResponse> liveData = new MutableLiveData<>();

        String token = PreferenceManager.getInstance(context).getUserCredentials().getToken();
        String vendorId = PreferenceManager.getInstance(context).getUserCredentials().getVendorID();
        String userId = PreferenceManager.getInstance(context).getUserCredentials().getUserId();
        String profileTypeId = PreferenceManager.getInstance(context).getUserCredentials().getProfileTypeId();
        String storeId = PreferenceManager.getInstance(context).getUserCredentials().getStoreID();
        List<Integer> storeAccessList = new ArrayList<>();
        storeAccessList.add(Integer.parseInt(storeId));

        RequestBody vendorid = RequestBody.create(MediaType.parse("multipart/form-data"), vendorId);
        RequestBody userid = RequestBody.create(MediaType.parse("multipart/form-data"), userId);

        RequestBody profileTypeid = RequestBody.create(MediaType.parse("multipart/form-data"), profileTypeId);
        RequestBody designationn = null;
        if (designation != null) {
            designationn = RequestBody.create(MediaType.parse("multipart/form-data"), designation);
        }
        RequestBody departmentId = null;
        if (departmentID != null) {
            departmentId = RequestBody.create(MediaType.parse("multipart/form-data"), departmentID);
        }
        RequestBody title = RequestBody.create(MediaType.parse("multipart/form-data"), Title);
        RequestBody displayName = RequestBody.create(MediaType.parse("multipart/form-data"), DisplayName);
        RequestBody fullName = RequestBody.create(MediaType.parse("multipart/form-data"), FullName);
        RequestBody gender = null;
        if (Gender != null) {
            gender = RequestBody.create(MediaType.parse("multipart/form-data"), Gender);
        }
        RequestBody primaryMobile = RequestBody.create(MediaType.parse("multipart/form-data"), PrimaryMobile);
        RequestBody emaill = null;
        if (email != null) {
            emaill = RequestBody.create(MediaType.parse("multipart/form-data"), email);
        }
        RequestBody storeiD = null;
        if (storeID != null) {
            storeiD = RequestBody.create(MediaType.parse("multipart/form-data"), storeID);

        }
        RequestBody aboutt = null;
        if (about != null) {
            aboutt = RequestBody.create(MediaType.parse("multipart/form-data"), about);

        }
        RequestBody dateOfB = null;
        if (dateOfBirth != null) {
            dateOfB = RequestBody.create(MediaType.parse("multipart/form-data"), dateOfBirth);

        }
        String selectedBloodGroup = BloodGroup;

        RequestBody bloodGroup = null;
        if (selectedBloodGroup != null) {
            bloodGroup = RequestBody.create(MediaType.parse("multipart/form-data"), selectedBloodGroup);
        }
        RequestBody nationalityy = null;
        if (nationality != null) {
            nationalityy = RequestBody.create(MediaType.parse("multipart/form-data"), nationality);
        }

        RequestBody alternativeEmaill = null;
        if (alternativeEmail != null) {
            alternativeEmaill = RequestBody.create(MediaType.parse("multipart/form-data"), alternativeEmail);

        }
        RequestBody othersContactNumbers = null;
        if (otherContactNumbers != null) {
            othersContactNumbers = RequestBody.create(MediaType.parse("multipart/form-data"), otherContactNumbers);

        }
        RequestBody websitee = null;

        if (website != null || website.isEmpty()) {
            websitee = RequestBody.create(MediaType.parse("multipart/form-data"), website);
        }

        RequestBody joiningdate = null;
        if (joiningDate != null) {
            joiningdate = RequestBody.create(MediaType.parse("multipart/form-data"), joiningDate);
        }

        RequestBody passwordd = null;
        if (password != null) {
            passwordd = RequestBody.create(MediaType.parse("multipart/form-data"), password);

        }


        Call<DuePaymentResponse> call = RetrofitClient.getInstance().getApi()
                .submitAddNewUserInfo(
                        token, vendorid, userid, profileTypeid, designationn, departmentId, title, displayName, fullName,
                        gender, primaryMobile, emaill, storeiD, aboutt, dateOfB, bloodGroup, nationalityy, alternativeEmaill, othersContactNumbers,
                        websitee, joiningdate, user_photo, passwordd, storeAccessList
                );
        call.enqueue(new Callback<DuePaymentResponse>() {
            @Override
            public void onResponse(Call<DuePaymentResponse> call, Response<DuePaymentResponse> response) {
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
                        liveData.postValue(null);
                    }

                }
            }

            @Override
            public void onFailure(Call<DuePaymentResponse> call, Throwable t) {
                liveData.postValue(null);
            }
        });
        return liveData;
    }


    public MutableLiveData<UserListResponse> getUserList(FragmentActivity context, String deptNameId, String designationId) {
        MutableLiveData<UserListResponse> liveData = new MutableLiveData<>();
        String token = PreferenceManager.getInstance(context).getUserCredentials().getToken();
        String vendorId = PreferenceManager.getInstance(context).getUserCredentials().getVendorID();
        String userId = PreferenceManager.getInstance(context).getUserCredentials().getUserId();


        Call<UserListResponse> call = RetrofitClient.getInstance().getApi().getUserList(token, vendorId, userId, deptNameId, designationId);

        call.enqueue(new Callback<UserListResponse>() {
            @Override
            public void onResponse(Call<UserListResponse> call, Response<UserListResponse> response) {
                if (response.isSuccessful()) {
                    if (response == null) {
                        liveData.postValue(null);
                        return;
                    }

                    if (response.body().getStatus() == 400) {
                        liveData.postValue(response.body());
                        return;
                    }

                    liveData.postValue(response.body());

                } else {
                    liveData.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<UserListResponse> call, Throwable t) {
                liveData.postValue(null);
            }
        });
        return liveData;
    }

    public MutableLiveData<EditUserDataResponse> getEditUserPageData(FragmentActivity context, String employee_profile_id, String userType) {
        MutableLiveData<EditUserDataResponse> liveData = new MutableLiveData<>();
        String token = PreferenceManager.getInstance(context).getUserCredentials().getToken();
        String vendorId = PreferenceManager.getInstance(context).getUserCredentials().getVendorID();
        String userId = PreferenceManager.getInstance(context).getUserCredentials().getUserId();

        Call<EditUserDataResponse> call = RetrofitClient.getInstance().getApi().getEditUserPageData(token, vendorId, userId, employee_profile_id, userType);

        call.enqueue(new Callback<EditUserDataResponse>() {
            @Override
            public void onResponse(Call<EditUserDataResponse> call, Response<EditUserDataResponse> response) {
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
                        Log.d("Error", e.getMessage());
                        liveData.postValue(null);
                    }


                }
            }

            @Override
            public void onFailure(Call<EditUserDataResponse> call, Throwable t) {
                Log.d("Error", t.getMessage());
                liveData.postValue(null);
            }
        });
        return liveData;
    }

    public MutableLiveData<DuePaymentResponse> submitEditUserInfo(
            FragmentActivity context, String employee_profile_id, String employee_vendorID,
            String designation, String departmentID, String Title,
            String DisplayName, String FullName, String Gender, String PrimaryMobile, String email, String storeID,
            String about, String dateOfBirth, String BloodGroup, String nationality, String alternativeEmail,
            String otherContactNumbers, String website, String joiningDate, String updateProfile, MultipartBody.Part user_photo, String userType
    ) {
        MutableLiveData<DuePaymentResponse> liveData = new MutableLiveData<>();

        String token = PreferenceManager.getInstance(context).getUserCredentials().getToken();
        String vendorId = PreferenceManager.getInstance(context).getUserCredentials().getVendorID();
        String userId = PreferenceManager.getInstance(context).getUserCredentials().getUserId();
        String profileTypeId = PreferenceManager.getInstance(context).getUserCredentials().getProfileTypeId();

        RequestBody vendorid = RequestBody.create(MediaType.parse("multipart/form-data"), vendorId);
        RequestBody userid = RequestBody.create(MediaType.parse("multipart/form-data"), userId);
        RequestBody profileTypeid = RequestBody.create(MediaType.parse("multipart/form-data"), profileTypeId);


        RequestBody employee_profileId = RequestBody.create(MediaType.parse("multipart/form-data"), employee_profile_id);
        RequestBody employee_vendorId = null;
        if (employee_vendorID != null) {
            employee_vendorId = RequestBody.create(MediaType.parse("multipart/form-data"), employee_vendorID);
        }
        RequestBody designationn = null;
        if (designation != null) {
            designationn = RequestBody.create(MediaType.parse("multipart/form-data"), designation);

        }
        RequestBody departmentId = null;
        if (departmentID != null) {
            departmentId = RequestBody.create(MediaType.parse("multipart/form-data"), departmentID);
        }
        RequestBody title = RequestBody.create(MediaType.parse("multipart/form-data"), Title);
        RequestBody displayName = RequestBody.create(MediaType.parse("multipart/form-data"), DisplayName);
        RequestBody fullName = RequestBody.create(MediaType.parse("multipart/form-data"), FullName);

        RequestBody gender = null;
        if (Gender != null) {
            gender = RequestBody.create(MediaType.parse("multipart/form-data"), Gender);
        }

        RequestBody primaryMobile = RequestBody.create(MediaType.parse("multipart/form-data"), PrimaryMobile);
        RequestBody emaill = RequestBody.create(MediaType.parse("multipart/form-data"), email);

        RequestBody storeiD = null;
        if (storeID != null) {
            storeiD = RequestBody.create(MediaType.parse("multipart/form-data"), storeID);
        }

        RequestBody aboutt = RequestBody.create(MediaType.parse("multipart/form-data"), about);
        RequestBody dateOfB = RequestBody.create(MediaType.parse("multipart/form-data"), dateOfBirth);

        String selectedBloodGroup = BloodGroup;
        RequestBody bloodGroup = null;
        if (selectedBloodGroup != null) {
            bloodGroup = RequestBody.create(MediaType.parse("multipart/form-data"), selectedBloodGroup);
        }

        RequestBody nationalityy = null;
        if (nationality != null) {
            nationalityy = RequestBody.create(MediaType.parse("multipart/form-data"), nationality);
        }

        RequestBody alternativeEmaill = null;
        if (alternativeEmail != null) {
            alternativeEmaill = RequestBody.create(MediaType.parse("multipart/form-data"), alternativeEmail);

        }

        RequestBody othersContactNumbers = null;
        if (otherContactNumbers != null) {
            othersContactNumbers = RequestBody.create(MediaType.parse("multipart/form-data"), otherContactNumbers);

        }
        RequestBody websitee = null;
        if (website != null) {
            websitee = RequestBody.create(MediaType.parse("multipart/form-data"), website);

        }
        RequestBody joiningdate = RequestBody.create(MediaType.parse("multipart/form-data"), joiningDate);
        RequestBody update_profile = RequestBody.create(MediaType.parse("multipart/form-data"), updateProfile);
        RequestBody user_Type = RequestBody.create(MediaType.parse("multipart/form-data"), userType);

        Call<DuePaymentResponse> call = RetrofitClient.getInstance()
                .getApi().submitEditUserInfo(
                        token, vendorid, userid, profileTypeid, employee_profileId, employee_vendorId, designationn,
                        departmentId, title, displayName, fullName, gender, primaryMobile, emaill, storeiD, aboutt, dateOfB, bloodGroup,
                        nationalityy, alternativeEmaill, othersContactNumbers, websitee, joiningdate, update_profile, user_photo, user_Type
                );
        call.enqueue(new Callback<DuePaymentResponse>() {
            @Override
            public void onResponse(Call<DuePaymentResponse> call, Response<DuePaymentResponse> response) {
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
                        liveData.postValue(null);
                    }

                }
            }

            @Override
            public void onFailure(Call<DuePaymentResponse> call, Throwable t) {
                liveData.postValue(null);
            }
        });
        return liveData;
    }


    public MutableLiveData<UserTrashListResponse> getUserTrashList(FragmentActivity context) {
        MutableLiveData<UserTrashListResponse> liveData = new MutableLiveData<>();
        String token = PreferenceManager.getInstance(context).getUserCredentials().getToken();
        String vendorId = PreferenceManager.getInstance(context).getUserCredentials().getVendorID();
        String userId = PreferenceManager.getInstance(context).getUserCredentials().getUserId();


        Call<UserTrashListResponse> call = RetrofitClient.getInstance().getApi()
                .getUserTrashList(token, vendorId, userId);
        call.enqueue(new Callback<UserTrashListResponse>() {
            @Override
            public void onResponse(Call<UserTrashListResponse> call, Response<UserTrashListResponse> response) {
                if (response.isSuccessful()) {
                    if (response == null) {
                        liveData.postValue(null);
                        return;
                    }

                    if (response.body().getStatus() == 400) {
                        liveData.postValue(response.body());
                        return;
                    }

                    liveData.postValue(response.body());

                } else {
                    liveData.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<UserTrashListResponse> call, Throwable t) {
                liveData.postValue(null);
            }
        });
        return liveData;

    }


    public MutableLiveData<DuePaymentResponse> checkUserActivation(FragmentActivity context, String userProfileId, String status) {
        MutableLiveData<DuePaymentResponse> liveData = new MutableLiveData<>();
        String token = PreferenceManager.getInstance(context).getUserCredentials().getToken();
        String vendorId = PreferenceManager.getInstance(context).getUserCredentials().getVendorID();
        String userId = PreferenceManager.getInstance(context).getUserCredentials().getUserId();


        Call<DuePaymentResponse> call = RetrofitClient.getInstance().getApi()
                .checkUserActivation(token, vendorId, userId, userProfileId, status);
        call.enqueue(new Callback<DuePaymentResponse>() {
            @Override
            public void onResponse(Call<DuePaymentResponse> call, Response<DuePaymentResponse> response) {
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
                        Log.d("ERROR", e.getMessage());
                        liveData.postValue(null);
                        return;
                    }

                } else {
                    liveData.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<DuePaymentResponse> call, Throwable t) {
                Log.d("ERROr", t.getMessage());
                liveData.postValue(null);
            }
        });
        return liveData;

    }


}
