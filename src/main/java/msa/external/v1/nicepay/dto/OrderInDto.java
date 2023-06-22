package msa.external.v1.nicepay.dto;

import lombok.Data;

@Data
public class OrderInDto {
    
    private String goodsName;       // 상품명(euc-kr)
    private String amt;             // 가격(only number)
    private String mid;             // 상점아이디(ex: nicepay00m)
    private String ediDate;         // 요청시간(YYYYMMDDHHMISS)
    private String moid;            // 주문번호
    private String signData;        // hex(sha256(EdiDate + MID + Amt + MerchantKey)) 
    
    
    private String buyerName;       // 구매자명(euc-kr)
    private String buyerTel;        // 구매자연락처(only number)
    private String buyerEmail;
    private String payMethod;       // CARD: 신용카드, BANK: 계좌이체, VBANK: 가상계좌, CELLPHONE: 휴대폰결제
    private String reqReserved;     // 가맹점 여분 필드
    private String charSet;         // 인증 응답 인코딩 (euc-kr / utf-8)
    private String vBankExpDate;    // 가상계좌 입금 만료일(YYYYMMDDHHMM)
    private String goodsCi;         // 휴대폰결제 추가 파라미터(0: 컨텐츠, 1: 실물)
}
