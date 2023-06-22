package msa.external.v1.nicepay.dto;

import lombok.Data;

@Data
public class CancelOutDto {
    private String resultCode;
    private String resultMsg;
    private String cancelAmt;
    private String mid;
    private String moid;
    private String signature;
    private String payMethod;
    private String tid;
    private String cancelDate;
    private String cancelTime;
    private String cancelNum;
    private String remainAmt;
    private String mallReserved;
}
