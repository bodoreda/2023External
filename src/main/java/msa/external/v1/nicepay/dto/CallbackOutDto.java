package msa.external.v1.nicepay.dto;

import lombok.Data;

@Data
public class CallbackOutDto {
    private String resultCode;
    private String resultMsg;
    private String payMethod;
    private String goodsName;
    private String amt;
    private String tid;
    private String buyerName;
    private String buyerEmail;
    private String vBankBankName;
    private String vBankBankCode;
    private String vBankNum;
    private String vBankExpDate;
    private String vBankExpTime;
    private String moid;
}
