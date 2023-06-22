package msa.external.v1.nicepay.dto;

import lombok.Data;

@Data
public class CancelInDto {
    private String tid;
    private String mid;
    private String moid;
    private String cancelAmt;       //취소금액
    private String cancelMsg;       //취소사유(euc-kr)
    private String partialCancelCode;   //0:전체취소, 1:부분취소(별도 계약 필요)
    private String ediDate;
    private String signData;
    private String charSet;
    private String ediType;
    private String mallReserved;
    private String refundAcctNo;    //환불계좌번호(숫자만)
    private String refundBankCd;    //환불계좌코드(은행코드 참고)
    private String refundAcctNm;    //환불계좌주명(euc-kr)
}
