package com.rupayan_housing.view.fragment.QC_QA;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.rupayan_housing.R;
import com.rupayan_housing.databinding.EditQcqaModelBinding;
import com.rupayan_housing.serverResponseModel.GetEditQcQaResponse;
import com.rupayan_housing.serverResponseModel.QcDetail;
import com.rupayan_housing.serverResponseModel.TestList;

import java.util.List;

public class EditQcQaAdapter extends RecyclerView.Adapter<EditQcQaAdapter.MyClass> {
    private FragmentActivity activity;
    private List<String> testMainNameList;
    private GetEditQcQaResponse getEditQcQaResponse;
    public static List<TestList> testNameList;
    private List<QcDetail> testList;
    private EditQcQaSelectionHandle clickHandle;

    public EditQcQaAdapter(FragmentActivity activity, List<String> testMainNameList, GetEditQcQaResponse getEditQcQaResponse, List<TestList> testNameList, List<QcDetail> testList, EditQcQaSelectionHandle clickHandle) {
        this.activity = activity;
        this.testMainNameList = testMainNameList;
        this.getEditQcQaResponse = getEditQcQaResponse;
        this.testNameList = testNameList;
        this.testList = testList;
        this.clickHandle = clickHandle;
    }

    @NonNull
    @Override
    public MyClass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        EditQcqaModelBinding binding
                = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.edit_qcqa_model, parent, false);
        return new MyClass(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyClass holder, int position) {
        try {
            holder.binding.testName.setItem(testMainNameList);
            QcDetail currentId = getEditQcQaResponse.getQcDetails().get(position);
            holder.binding.value.setText("" +  currentId.getParameterValue());


            for (int i = 0; i < getEditQcQaResponse.getTestList().size(); i++) {
                if (currentId.getTestID().equals(getEditQcQaResponse.getTestList().get(i).getTestID())) {
                    holder.binding.testName.setSelection(i);
                    holder.binding.testName.setEnableErrorLabel(false);

                    holder.binding.shortName.setText("" + getEditQcQaResponse.getTestList().get(i).getShortName());
                    holder.binding.reference.setText("" + getEditQcQaResponse.getTestList().get(i).getTestName());

                }
            }

/*
 for (int i = 0; i < getEditQcQaResponse.getQcDetails().size(); i++) {
for (int i1 = 0; i1 < getEditQcQaResponse.getTestList().size(); i1++) {
if (getEditQcQaResponse.getQcDetails().get(i).getTestID().equals(getEditQcQaResponse.getTestList().get(i1).getTestID())) {
                   holder.binding.testName.setSelection(i1);
                        holder.binding.testName.setEnableErrorLabel(false);

                        holder.binding.shortName.setText("" + getEditQcQaResponse.getTestList().get(i1).getShortName());
                        holder.binding.sampleName.setText("" + getEditQcQaResponse.getTestList().get(i1).getTestName());
                        holder.binding.value.setText("" + getEditQcQaResponse.getQcDetails().get(i).getParameterValue());

                    }
                }
            }

*/
            holder.binding.testName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    holder.binding.testName.setSelected(true);
                    holder.binding.testName.setSelection(position);

                    holder.binding.shortName.setText(getEditQcQaResponse.getTestList().get(position).getShortName());
                    holder.binding.reference.setText(testNameList.get(position).getReference());


//                    try {
//                        for (int i = 0; i < testNameList.size(); i++) {
//                            if (testNameList.get(i).getTestID().equals(getEditQcQaResponse.getQcDetails().get(i).getTestID())) {
//                                holder.binding.value.setText(getEditQcQaResponse.getQcDetails().get(i).getParameterValue());
//                                break;
//                            }
//                        }
//                    } catch (Exception e) {
//                        Log.d("Error", e.getMessage());
//                    }

                    try {
                        clickHandle.select(holder.getAdapterPosition(), getEditQcQaResponse.getTestList().get(position).getTestID());

                    } catch (Exception e) {
                        Log.d("Error", e.getMessage());
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

        } catch (Exception e) {
            Log.d("ERROR", "" + e.getMessage());
        }


    }

    @Override
    public int getItemCount() {
        return getEditQcQaResponse.getQcDetails().size();
    }


    class MyClass extends RecyclerView.ViewHolder {
        private EditQcqaModelBinding binding;

        public MyClass(EditQcqaModelBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }


    }
}
