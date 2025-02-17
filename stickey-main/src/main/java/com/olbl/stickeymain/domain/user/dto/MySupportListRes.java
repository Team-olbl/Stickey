package com.olbl.stickeymain.domain.user.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MySupportListRes {

    private List<MySupportRes> mySupportResList;

    private int pageNumber;
    private int pageSige;
    private boolean lase;

}
