package com.rupayan_housing.adapter;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.rupayan_housing.R;
import com.rupayan_housing.localDatabase.PreferenceManager;
import com.rupayan_housing.serverResponseModel.NotificationListResponse;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class NotificationListAdapter extends RecyclerView.Adapter<NotificationListAdapter.MyHolder> {
    FragmentActivity context;
    List<NotificationListResponse> notificationListResponseList;


    @NonNull
    @Override
    public NotificationListAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.notification_item_model2, parent, false);
        return new NotificationListAdapter.MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationListAdapter.MyHolder holder, int position) {
        NotificationListResponse current = notificationListResponseList.get(position);
        holder.dateTextView.setText(notificationListResponseList.get(position).getEntryDate());
        holder.referenceTextView.setText(notificationListResponseList.get(position).getRemarks() + " #" + notificationListResponseList.get(position).getRefOrderID());
        holder.noteTextView.setText(notificationListResponseList.get(position).getNote());


        if (notificationListResponseList.get(position).getOrderApproval().equals("2")) {
            holder.approvedTextView.setVisibility(View.GONE);
            holder.approved_date.setVisibility(View.GONE);
            holder.approved_date_tv.setVisibility(View.GONE);
            holder.pendingTextView.setVisibility(View.VISIBLE);
            holder.pendingTextView.setText("Pending");
        } else if (notificationListResponseList.get(position).getOrderApproval().equals("1")) {
            holder.approvedTextView.setVisibility(View.VISIBLE);
            holder.approved_date.setVisibility(View.VISIBLE);
            holder.approved_date_tv.setVisibility(View.VISIBLE);
            holder.pendingTextView.setVisibility(View.GONE);
            holder.approvedTextView.setText("Approved by " + notificationListResponseList.get(position).getProcessedBy().getFullName());
            holder.approved_date_tv.setText(notificationListResponseList.get(position).getApprovedDateTime());
        } else if (notificationListResponseList.get(position).getStatus().equals("0")) {
            holder.approvedTextView.setVisibility(View.GONE);
            holder.approved_date.setVisibility(View.GONE);
            holder.approved_date_tv.setVisibility(View.GONE);
            holder.pendingTextView.setVisibility(View.VISIBLE);
            holder.pendingTextView.setText("Old");
        } else {
            holder.approvedTextView.setVisibility(View.GONE);
            holder.approved_date.setVisibility(View.GONE);
            holder.approved_date_tv.setVisibility(View.GONE);
            holder.pendingTextView.setVisibility(View.VISIBLE);
            holder.pendingTextView.setText("Declined");
        }

        holder.detailsBtn.setOnClickListener(v -> {

            Log.d("TYPE", String.valueOf(current.getType()));

            /**
             * if current notification getOrderApproval status is  = 2 then we can understand this is pending notification otherWise next page will  be
             * only view part
             *
             * Implement the logic below all condition
             * send status by bundle here status
             * 0 means next should be only details hide approve and decline btn
             * 1 means next should be approve and delineable
             *
             */


            /**
             * here 5 is a unique key for approve and decline pending purchase only
             */


            if (current.getType() == 22) {
                /**
                 * customer
                 */
                Bundle bundle = new Bundle();
                bundle.putString("typeKey", String.valueOf(current.getType()));
                bundle.putString("customerId", current.getRefOrderID());
                if (current.getOrderApproval().equals("2")) {
                    bundle.putString("status","2");
                    bundle.putString("pageName","Pending  Customer Details");
                }
                else{
                    bundle.putString("pageName","Customer Details");
                }
                Navigation.createNavigateOnClickListener(R.id.notificationListFragment_to_customerDetailsFragment, bundle).onClick(v);
            }
            /**
             * for mill view
             */

            if (current.getType() == 28){
                Bundle bundle = new Bundle();
                bundle.putString("typeKey",String.valueOf(current.getType()));
                bundle.putString("slId",current.getRefOrderID());
                bundle.putString("portion","notification");
                if (current.getOrderApproval().equals("2")) {
                    bundle.putString("status","2");
                    bundle.putString("pageName","Pending  Mill Details");
                }
                bundle.putString("pageName"," Mill Details");

                Navigation.createNavigateOnClickListener(R.id.notificationListFragment_to_millerDetailsViewFragment,bundle).onClick(v);

            }


            if (current.getType() == 23) {
                /**
                 * local supplier
                 */
                Bundle bundle = new Bundle();
                bundle.putString("typeKey", String.valueOf(current.getType()));
                bundle.putString("customerId", current.getRefOrderID());
                if (current.getOrderApproval().equals("2")) {
                    bundle.putString("status","2");
                    bundle.putString("pageName","Local Supplier Pending Details");
                }
                else{
                    bundle.putString("pageName","Local Supplier Details");
                }
                //here blanks means supplier details
                Navigation.createNavigateOnClickListener(R.id.notificationListFragment_to_blankFragment, bundle).onClick(v);
            }
            if (current.getType() == 24) {
                /**
                 * foreign supplier
                 */
                Bundle bundle = new Bundle();
                bundle.putString("typeKey", String.valueOf(current.getType()));
                bundle.putString("customerId", current.getRefOrderID());
                if (current.getOrderApproval().equals("2")) {
                    bundle.putString("status","2");
                    bundle.putString("pageName","Foreign Supplier Pending Details");
                }
                else{
                    bundle.putString("pageName","Foreign Supplier Details");
                }
                Navigation.createNavigateOnClickListener(R.id.notificationListFragment_to_customerDetailsFragment, bundle).onClick(v);
            }

            if (current.getType() == 5) {
                Bundle bundle = new Bundle();
                bundle.putString("TypeKey", String.valueOf(current.getType()));
                bundle.putString("RefOrderId", current.getRefOrderID());
                bundle.putString("portion", "PENDING_PURCHASE");
                if (current.getOrderApproval().equals("2")) {
                    bundle.putString("status", "2");
                    bundle.putString("pageName", "Pending Purchase Details");
                } else {
                    bundle.putString("pageName", "Purchase Details");
                }
                Navigation.createNavigateOnClickListener(R.id.action_notificationFragment_to_pendingPurchaseDetailsFragment4, bundle).onClick(v);
                return;
            }

            if (current.getType() == 20) {
                Bundle bundle = new Bundle();
                bundle.putString("RefOrderId", current.getRefOrderID());
               // bundle.putString("portion", "PurchaseReturnDetails");
                bundle.putString("portion", "PENDING_PURCHASE");
                if (current.getOrderApproval().equals("2")) {
                    bundle.putString("status", "2");
                    bundle.putString("pageName", "Pending Purchase Return Details");
                } else {
                    bundle.putString("pageName", "Purchase Return History Details");
                }
                bundle.putString("enterprise", "current.getStoreName()");
               // Navigation.createNavigateOnClickListener(R.id.action_notificationFragment_to_purchaseAndSalesReturnDetailsFragment, bundle).onClick(v);
                Navigation.createNavigateOnClickListener(R.id.action_notificationFragment_to_purchaseReturnPendingDetailsFragment, bundle).onClick(v);
                 return;
            }
            if (current.getType() == 25) {
                Bundle bundle = new Bundle();
                bundle.putString("SL_ID", current.getRefOrderID());
                bundle.putString("portion", "QC_QA_DETAILS");
                if (current.getOrderApproval().equals("2")) {
                    bundle.putString("status", "2");
                    bundle.putString("pageName", "Pending Qc-Qa Details");
                } else {
                    bundle.putString("pageName", "Qc-Qa Details");
                }
                Navigation.createNavigateOnClickListener(R.id.action_notificationFragment_to_qcqaDetailsFragment, bundle).onClick(v);
                return;
            }


            /**
             * here 2 is a unique key for approve and decline pending sales only
             */
            if (current.getType() == 2) {
                Bundle bundle = new Bundle();
                bundle.putString("TypeKey", String.valueOf(current.getType()));
                bundle.putString("RefOrderId", current.getRefOrderID());
                bundle.putString("portion", "PENDING_SALE");
                if (current.getOrderApproval().equals("2")) {
                    bundle.putString("status", "2");
                    bundle.putString("pageName", "Pending Sales Details");
                    Navigation.createNavigateOnClickListener(R.id.notificationListFragment_to_pendingPurchaseDetailsFragment, bundle).onClick(v);
                    return;
                }
                bundle.putString("pageName", "Sales Details");
                Navigation.createNavigateOnClickListener(R.id.action_notificationFragment_to_pendingPurchaseDetailsFragment4, bundle).onClick(v);
            }

            /**
             * here 6 is a unique key for approve and decline edit purchase  only
             */
            if (current.getType() == 6) {
                Bundle bundle = new Bundle();
                bundle.putString("TypeKey", String.valueOf(current.getType()));
                bundle.putString("RefOrderId", current.getRefOrderID());

                if (!(current.getOrderApproval().equals("2"))) {
                    bundle.putString("portion", "PENDING_PURCHASE");//same page show key also same
                    bundle.putString("porson", "PurchaseHistoryDetails");
                    bundle.putString("pageName", "Purchase Details");
                    Navigation.createNavigateOnClickListener(R.id.notificationListFragment_to_pendingPurchaseDetailsFragment, bundle).onClick(v);
                    return;
                }
                bundle.putString("pageName", "Pending Purchase Details");
                bundle.putString("portion", "EDIT_PURCHASE");
                Navigation.createNavigateOnClickListener(R.id.action_notificationFragment_to_purchaseEditDetails, bundle).onClick(v);
            }


            /**
             * here 8 is a unique key for approve and decline edit SALE  only
             */
            if (current.getType() == 8) {
                Bundle bundle = new Bundle();
                bundle.putString("TypeKey", String.valueOf(current.getType()));
                bundle.putString("RefOrderId", current.getRefOrderID());
                if (!(current.getOrderApproval().equals("2"))) {
                    //bundle.putString("status", "2");
                    bundle.putString("portion", "PENDING_SALE");
                    bundle.putString("porson", "SaleHistoryDetails");
                    Navigation.createNavigateOnClickListener(R.id.notificationListFragment_to_pendingPurchaseDetailsFragment, bundle).onClick(v);
                    return;
                } else {
                    bundle.putString("pageName", "Pending Sales Details");
                    bundle.putString("portion", "EDIT_SALE");
                    Navigation.createNavigateOnClickListener(R.id.action_notificationFragment_to_purchaseEditDetails, bundle).onClick(v);
                }
            }


            /**
             * for get Washing & Crushing Details
             */
            if (current.getType() == 7) {
                Bundle bundle = new Bundle();
                bundle.putString("TypeKey", String.valueOf(current.getType()));
                bundle.putString("RefOrderId", current.getRefOrderID());
                if (current.getOrderApproval().equals("2")) {
                    bundle.putString("status", "2");
                    bundle.putString("pageName", "Pending Washing & Crushing Details");
                } else {
                    bundle.putString("pageName", "Washing & Crushing Details");
                }

                Navigation.createNavigateOnClickListener(R.id.action_notificationFragment_to_getPendingWashingAndCrushingDetails, bundle).onClick(v);
            }

            /**
             * here 9 is a unique key for get edited Washing & Crushing Details
             */
            if (current.getType() == 9) {
                Bundle bundle = new Bundle();
                bundle.putString("TypeKey", String.valueOf(current.getType()));
                bundle.putString("RefOrderId", current.getRefOrderID());
                bundle.putString("pageName", "Edited Washing & Crushing");
                //bundle.putString("portion", "EDIT_SALE");
                Navigation.createNavigateOnClickListener(R.id.action_notificationFragment_to_editedWashigAndCrushingDetails, bundle).onClick(v);
            }
            /**
             * here 11 is a unique key for get pending iodization details
             */
            if (current.getType() == 11) {
                Bundle bundle = new Bundle();
                bundle.putString("TypeKey", String.valueOf(current.getType()));
                bundle.putString("RefOrderId", current.getRefOrderID());
                if (current.getOrderApproval().equals("2")) {
                    bundle.putString("status", "2");
                    bundle.putString("pageName", "Pending Iodization Details");
                } else {
                    bundle.putString("pageName", "Iodization Details");
                }
                Navigation.createNavigateOnClickListener(R.id.action_notificationFragment_to_pendingIodizationDetailsFragment, bundle).onClick(v);
            }

            /**
             * here 10 is a unique key for get edited iodization details
             */
            /**
             * get edited iodization details have (IodizationEditDetailsFragment) design complele without data
             * unComplete for wrong API
             */
            if (current.getType() == 10) {
                Bundle bundle = new Bundle();
                bundle.putString("TypeKey", String.valueOf(current.getType()));
                bundle.putString("RefOrderId", current.getRefOrderID());
                bundle.putString("pageName", "Pending Iodization Details");
                bundle.putString("portion", "PENDING_IODIZATION_DETAILS");
                Navigation.createNavigateOnClickListener(R.id.action_notificationFragment_to_iodizationEditDetailsFragment, bundle).onClick(v);
            }

            /**
             * for get actual order id remove first Q from the Ref_orderID
             */
            String refOrderId = current.getRefOrderID();
            if (refOrderId.startsWith("Q")) {
                refOrderId = refOrderId.substring(1);
            }
            if (refOrderId.startsWith("ex")) {
                refOrderId = refOrderId.substring(2);
            }

            /**
             * here 12  is a unique key for show Pending Quotation Details
             */
            if (current.getType() == 12) {
                Bundle bundle = new Bundle();
                bundle.putString("TypeKey", String.valueOf(current.getType()));
                bundle.putString("RefOrderId", refOrderId);
                bundle.putString("pageName", "Pending Quotation Details");
                bundle.putString("portion", "PENDING_QUOTATION_DETAILS");
                Navigation.createNavigateOnClickListener(R.id.action_notificationFragment_to_pendingQuotationDetails, bundle).onClick(v);
            }
            /**
             * here 13  is a unique key for show Pending Transfer Details
             */
            if (current.getType() == 13) {
                Bundle bundle = new Bundle();
                bundle.putString("TypeKey", String.valueOf(current.getType()));
                bundle.putString("RefOrderId", refOrderId);
                if (current.getOrderApproval().equals("2")) {
                    bundle.putString("status", "2");
                    bundle.putString("pageName", "Pending Transfer Details");
                } else {
                    bundle.putString("pageName", "Transfer Details");
                }
                Navigation.createNavigateOnClickListener(R.id.action_notificationFragment_to_transferDetailsFragment, bundle).onClick(v);
            }
            /**
             * here 14 is a unique key for show pending expense details
             */

            if (current.getType() == 14) {
                Bundle bundle = new Bundle();
                bundle.putString("TypeKey", String.valueOf(current.getType()));
                bundle.putString("RefOrderId", refOrderId);
                bundle.putString("pageName", "Pending Expense Details");
                bundle.putString("portion", "PENDING_EXPENSE_DETAILS");
                Navigation.createNavigateOnClickListener(R.id.action_notificationFragment_to_pendingExpenseDetails, bundle).onClick(v);
            }

            /**
             * here 15 is a unique key for show pending Edited Payment Details
             */
            if (current.getType() == 15) {
                Bundle bundle = new Bundle();
                bundle.putString("TypeKey", String.valueOf(current.getType()));
                bundle.putString("RefOrderId", refOrderId);
                bundle.putString("pageName", "Edited Payment Details");
                bundle.putString("portion", "EDITED_PENDING_PURCHASE_DETAILS");
                Navigation.createNavigateOnClickListener(R.id.action_notificationFragment_to_pendingEditedPurchasePendingDetails, bundle).onClick(v);
            }

            /**
             * here 16 is a unique key for show pending reconciliation details
             */
            if (current.getType() == 16) {
                Bundle bundle = new Bundle();
                bundle.putString("TypeKey", String.valueOf(current.getType()));
                bundle.putString("RefOrderId", refOrderId);
                bundle.putString("portion", "PENDING_RECONCILIATION_DETAILS");
                if (current.getOrderApproval().equals("2")) {
                    bundle.putString("status", "2");
                    bundle.putString("pageName", "Pending Reconciliation Details");
                } else {
                    bundle.putString("pageName", "Reconciliation Details");
                }
                Navigation.createNavigateOnClickListener(R.id.action_notificationFragment_to_reconciliationDetailsFragment, bundle).onClick(v);
            }

            /**
             * here 17 is a unique key for show pending reconciliation details
             */
            if (current.getType() == 17) {
                Bundle bundle = new Bundle();
                bundle.putString("TypeKey", String.valueOf(current.getType()));
                bundle.putString("RefOrderId", refOrderId);
                bundle.putString("portion", "SALES_RETURNS_DETAILS");
                bundle.putString("pageName", "Sales Return Details");
                if (current.getOrderApproval().equals("2")) {
                    bundle.putString("status", "2");
                }
               // Navigation.createNavigateOnClickListener(R.id.action_notificationFragment_to_pendingSalesReturnDetails, bundle).onClick(v);
                Navigation.createNavigateOnClickListener(R.id.action_notificationFragment_to_purchaseReturnPendingDetailsFragment, bundle).onClick(v);
            }


            if (current.getType() == 18) {
                Bundle bundle = new Bundle();
                bundle.putString("TypeKey", String.valueOf(current.getType()));
                bundle.putString("RefOrderId", refOrderId);
                //bundle.putString("pageName", "Sales Return Details");
                //bundle.putString("portion", "SALES_RETURNS_DETAILS");
                Navigation.createNavigateOnClickListener(R.id.action_notificationFragment_to_showSalesRequisition, bundle).onClick(v);
            }

            if (current.getType() == 4) {
                Bundle bundle = new Bundle();
                bundle.putString("TypeKey", String.valueOf(current.getType()));
                bundle.putString("RefOrderId", refOrderId);
                bundle.putString("batch", current.getBatch());
                bundle.putString("customer", current.getCustomerID());
                Navigation.createNavigateOnClickListener(R.id.action_notificationFragment_to_expenseDuePaymentApproveDetails, bundle).onClick(v);
            }
            if (current.getType() == 21) {
                Bundle bundle = new Bundle();
                bundle.putString("TypeKey", String.valueOf(current.getType()));
                bundle.putString("RefOrderId", refOrderId);
                bundle.putString("portion", "SALES_WHOLE_ORDER_CANCEL");
                if (current.getOrderApproval().equals("2")) {
                    bundle.putString("pageName", "Pending Sales Return Cancel Details");
                    bundle.putString("status", "2");
                    Navigation.createNavigateOnClickListener(R.id.notificationListFragment_to_pendingPurchaseDetailsFragment, bundle).onClick(v);
                    return;
                } else {
                    bundle.putString("pageName", "Sales Return  Details");
                }
                // bundle.putString("portion", "SALES_RETURNS_CANCEL_WHOLE_ORDER_DETAILS");
                Navigation.createNavigateOnClickListener(R.id.notificationListFragment_to_pendingPurchaseDetailsFragment, bundle).onClick(v);
            }


        });
    }


    @Override
    public int getItemCount() {
        return notificationListResponseList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.date_tv)
        TextView dateTextView;
        @BindView(R.id.reference_tv)
        TextView referenceTextView;
        @BindView(R.id.note_tv)
        TextView noteTextView;
        @BindView(R.id.approved_tv)
        TextView approvedTextView;
        @BindView(R.id.pending_tv)
        TextView pendingTextView;
        @BindView(R.id.approved_date)
        TextView approved_date;
        @BindView(R.id.approved_date_tv)
        TextView approved_date_tv;
        @BindView(R.id.detailsBtn)
        ImageButton detailsBtn;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
