package com.rupayan_housing.view.fragment.miller;

public interface CertificateApprovalDataGet {
    void approve(int adapterPosition,String certificateId,String reviewStatus);
    void decline(int adapterPosition,String certificateId);
}
