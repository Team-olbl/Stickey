package com.olbl.stickeymain.domain.admin.service;

import com.olbl.stickeymain.domain.admin.dto.ConfirmReq;
import com.olbl.stickeymain.domain.admin.dto.SignUpListRes;
import com.olbl.stickeymain.domain.admin.dto.SignUpOneRes;
import com.olbl.stickeymain.domain.admin.dto.WaitingSupportListRes;
import com.olbl.stickeymain.domain.admin.dto.WaitingSupportOneRes;

public interface AdminService {

    SignUpListRes getSignUpList();

    SignUpOneRes getSignUp(int id);

    void confirmOrganization(int id, ConfirmReq confirmReq);

    WaitingSupportListRes getWaitingSupportList();

    WaitingSupportOneRes getWaitingSupport(int id);

    void confirmSupport(int id, ConfirmReq confirmReq);
}
