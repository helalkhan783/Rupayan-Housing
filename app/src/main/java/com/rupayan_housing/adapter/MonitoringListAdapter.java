//package com.miserp.adapter;
//
//import android.content.Context;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import androidx.annotation.NonNull;
//import androidx.databinding.DataBindingUtil;
//import androidx.fragment.app.FragmentActivity;
//import androidx.navigation.Navigation;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.miserp.R;
//import com.miserp.clickHandle.MonitoringListModelClickHandle;
//import com.miserp.databinding.MonitoringListModelBinding;
//import com.miserp.localDatabase.PreferenceManager;
//import com.miserp.serverResponseModel.ListMonitorModel;
//import com.miserp.serverResponseModel.MonitoringModel;
//import com.miserp.serverResponseModel.MonitoringType;
//import com.miserp.utils.PermissionUtil;
//import com.miserp.view.fragment.DateFormatRight;
//import com.miserp.view.fragment.monitoring.MonitoringListFragment;
//
//import org.jetbrains.annotations.NotNull;
//
//import java.util.List;
//
//import es.dmoral.toasty.Toasty;
//import lombok.AllArgsConstructor;
//
// public class MonitoringListAdapter extends RecyclerView.Adapter<MonitoringListAdapter.ViewHolder> {
//    private FragmentActivity context;
//    private List<ListMonitorModel> monitoringLists;
//    private List<MonitoringType> monitoringModelObject;
//    private View view;
//
//     public MonitoringListAdapter(FragmentActivity context, List<ListMonitorModel> monitoringLists, List<MonitoringType> monitoringModelObject, View view) {
//         this.context = context;
//         this.monitoringLists = monitoringLists;
//         this.monitoringModelObject = monitoringModelObject;
//         this.view = view;
//     }
//
//     @NonNull
//    @NotNull
//    @Override
//    public MonitoringListAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
//        MonitoringListModelBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.monitoring_list_model, parent, false);
//        return new ViewHolder(binding);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull @NotNull MonitoringListAdapter.ViewHolder holder, int position) {
//        ListMonitorModel currentmonitorList = monitoringLists.get(position);
//        MonitoringType type = monitoringModelObject.get(position);
//try {
//     if (!PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(context).getUserCredentials().getPermissions()).contains(1450)) {
//        holder.binding.monitoringEdit.setVisibility(View.GONE);
//    }
//    if (!PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(context).getUserCredentials().getPermissions()).contains(1451)) {
//        holder.binding.monitoringView.setVisibility(View.GONE);
//    }
//    if (!PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(context).getUserCredentials().getPermissions()).contains(1548)) {
//        holder.binding.history.setVisibility(View.GONE);
//    }
//try {
//    for (int i = 0; i < monitoringModelObject.size(); i++) {
//        if (currentmonitorList.getMonitoringType().equals(monitoringModelObject.get(i).getTypeID())) {
//            holder.binding.monitoringType.setText(":  " + monitoringModelObject.get(i).getMonitoringTypeName());
//        }
//    }
//
//}catch (Exception e){}
//    if (currentmonitorList.getMonitoringDate() == null) {
//        holder.binding.monitoringDate.setText(":");
//    } else {
//        holder.binding.monitoringDate.setText(":  " + new DateFormatRight(context, currentmonitorList.getMonitoringDate()).onlyDayMonthYear());
//    }
//    if (holder.binding.monitorBy != null) {
//        holder.binding.monitorBy.setText(":  " + currentmonitorList.getMonitorBy());
//    }
//
//    if (currentmonitorList.getZoneName() == null) {
//        holder.binding.zoneName.setText(":");
//    } else {
//        holder.binding.zoneName.setText(":  " + currentmonitorList.getZoneName());
//    }
//
//
//    if (currentmonitorList.getPublishDate() == null) {
//        holder.binding.publishedDate.setText(":");
//
//    } else {
//        holder.binding.publishedDate.setText(":  " + currentmonitorList.getPublishDate());
//    }
//}catch (Exception e){}
//        holder.binding.setClickHandle(new MonitoringListModelClickHandle() {
//            @Override
//            public void download() {
//               // Toasty.info(context, "Will Implement it", Toasty.LENGTH_LONG).show();
//            }
//
//            @Override
//            public void edit() {
//                try {
//                    String currentProfileTypeId = PreferenceManager.getInstance(context).getUserCredentials().getProfileTypeId();
//
//                    if (PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(context).getUserCredentials().getPermissions()).contains(1450)) {
//                        String selectedId = null;
//                        try {
//                            selectedId = monitoringLists.get(holder.getAdapterPosition()).getSlID();
//                        } catch (Exception e) {
//                            Log.d("ERROR", "ERROR");
//                        }
//                        if (monitoringLists.get(holder.getAdapterPosition()).getMonitorId() != null) {
//                            Bundle bundle = new Bundle();
//                            bundle.putString("id", selectedId);
//                            MonitoringListFragment.pageNumber = 1;
//                            Navigation.findNavController(holder.binding.getRoot()).navigate(R.id.action_monitoringListFragment_to_editMonitoringFragment, bundle);
//                        }
//                        return;
//
//
//                    } else {
//                        Toasty.info(context, PermissionUtil.permissionMessage, Toasty.LENGTH_LONG).show();
//                    }
//
//                } catch (Exception e) {
//                    Log.d("ERROR", "" + e.getMessage());
//                }
//            }
//
//            @Override
//            public void view() {
//                try {
//                    if (PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(context).getUserCredentials().getPermissions()).contains(1451)) {
//                        Bundle bundle = new Bundle();
//                        bundle.putString("slId", currentmonitorList.getSlID());
//                        MonitoringListFragment.pageNumber = 1;
//                        Navigation.findNavController(holder.binding.getRoot()).navigate(R.id.action_monitoringListFragment_to_monitoringViewFragment, bundle);
//                    } else {
//                        Toasty.info(context, PermissionUtil.permissionMessage, Toasty.LENGTH_LONG).show();
//                    }
//                } catch (Exception e) {
//                    Log.d("ERROR", "" + e.getMessage());
//                }
//            }
//        });
//
//        holder.binding.history.setOnClickListener(v -> {
//            if (PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(context).getUserCredentials().getPermissions()).contains(1548)) {
//                Bundle bundle = new Bundle();
//                bundle.putString("id", currentmonitorList.getMonitorId());
//                bundle.putString("pageName", "Monitoring History");
//                Navigation.findNavController(view).navigate(R.id.action_monitoringListFragment_to_allSubHistoryListFragmet, bundle);
//            } else {
//                Toasty.info(context, PermissionUtil.permissionMessage, Toasty.LENGTH_LONG).show();
//            }
//
//        });
//    }
//
//    @Override
//    public int getItemCount() {
//
//        return monitoringLists.size();
//    }
//
//    public class ViewHolder extends RecyclerView.ViewHolder {
//        private final MonitoringListModelBinding binding;
//
//        public ViewHolder(final MonitoringListModelBinding itemBinding) {
//            super(itemBinding.getRoot());
//            this.binding = itemBinding;
//        }
//    }
//}
