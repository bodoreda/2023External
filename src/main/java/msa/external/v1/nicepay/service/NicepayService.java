package msa.external.v1.nicepay.service;

import msa.external.v1.nicepay.dto.*;
import msa.external.v1.nicepay.util.DataEncrypt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import static msa.external.v1.nicepay.util.NicepayUtil.*;
import static msa.external.v1.nicepay.util.AdditionalUtil.*;

@Service
public class NicepayService {
    @Value("${nicepay.merchantKey}")
    private String merchantKey;
    @Value("${nicepay.merchantID}")
    private String merchantID;

    public OrderOutDto order(OrderInDto inDto) {
        String ediDate 			= getyyyyMMddHHmmss();
        /*
         *******************************************************
         * <결제요청 파라미터> Web Manual의 [결제창 호출 파라미터]
         * 결제시 Form 에 보내는 결제요청 파라미터입니다.
         * 샘플페이지에서는 기본(필수) 파라미터만 예시되어 있으며,
         * 추가 가능한 옵션 파라미터는 연동메뉴얼을 참고하세요.
         *******************************************************
         */
        String goodsName 		= "테스트상품명"; 				// 결제상품명
        String amt     			= randomPrice(); 				// 결제상품금액(1000~2000 랜덤)
        String buyerName 		= "백보현"; 	        			// 구매자명
        String buyerTel 		= "01041555752"; 				// 구매자연락처
        String buyerEmail 		= "ai_love99@naver.com"; 		// 구매자메일주소
        String moid 			= "order"+ediDate; 			    // 상품주문번호
        String charSet          = "euc-kr";

        /*
         *******************************************************
         * <해쉬암호화> (수정하지 마세요)
         * SHA-256 해쉬암호화는 거래 위변조를 막기위한 방법입니다.
         *******************************************************
         */
        DataEncrypt sha256Enc 	= new DataEncrypt();
        String signData 		= sha256Enc.encrypt(ediDate + merchantID + amt + merchantKey);

        OrderOutDto outDto = new OrderOutDto();

        outDto.setGoodsName(goodsName);
        outDto.setAmt(amt);
        outDto.setEdiDate(ediDate);
        outDto.setMoid(moid);
        outDto.setSignData(signData);
        outDto.setBuyerName(buyerName);
        outDto.setBuyerTel(buyerTel);
        outDto.setBuyerEmail(buyerEmail);
        outDto.setMid(merchantID);
        outDto.setCharSet(charSet);

        return outDto;
    }

    public CallbackOutDto callback(Map<String, String> map) throws Exception {
        /*
         ****************************************************************************************
         * <인증 결과 파라미터> Web Manual의 [결제창 응답 파라미터]
         ****************************************************************************************
         */
        String authResultCode 	= map.get("AuthResultCode"); 	// 인증결과 : 0000(성공)
        String authResultMsg 	= map.get("AuthResultMsg"); 	// 인증결과 메시지
        String nextAppURL 		= map.get("NextAppURL"); 		// 승인 요청 URL
        String txTid 			= map.get("TxTid"); 			// 거래 ID
        String authToken 		= map.get("AuthToken"); 		// 인증 TOKEN
        String payMethod 		= map.get("PayMethod"); 		// 결제수단
        String mid 				= map.get("MID"); 				// 상점 아이디
        String moid 			= map.get("Moid"); 			    // 상점 주문번호
        String amt 				= map.get("Amt"); 				// 결제 금액
        String reqReserved 		= map.get("ReqReserved"); 		// 상점 예약필드
        String netCancelURL 	= map.get("NetCancelURL");  	// 망취소 요청 URL
        /*
         ****************************************************************************************
         * Signature : 요청 데이터에 대한 무결성 검증을 위해 전달하는 파라미터로 허위 결제 요청 등 결제 및 보안 관련 이슈가 발생할 만한 요소를 방지하기 위해 연동 시 사용하시기 바라며
         * 위변조 검증 미사용으로 인해 발생하는 이슈는 당사의 책임이 없음 참고하시기 바랍니다.
         ****************************************************************************************
         */
        DataEncrypt sha256Enc 	= new DataEncrypt();

        /*
         ****************************************************************************************
         * <승인 결과 파라미터 정의> Web Manual의 [승인 API 응답 파라미터]
         * 샘플페이지에서는 승인 결과 파라미터 중 일부만 예시되어 있으며,
         * 추가적으로 사용하실 파라미터는 연동메뉴얼을 참고하세요.
         ****************************************************************************************
         */
        String ResultCode 	= "";
        String ResultMsg 	= "";
        String PayMethod 	= "";
        String GoodsName 	= "";
        String Amt 	    	= "";
        String TID 		    = "";
        // 추가
        String BuyerName    = "";
        String BuyerEmail   = "";
        String VbankBankName = "";
        String VbankBankCode = "";
        String VbankNum     = "";
        String VbankExpDate = "";
        String VbankExpTime = "";
        String Moid = "";

        /*
         ****************************************************************************************
         * <인증 결과 성공시 승인 진행>
         ****************************************************************************************
         */
        String resultJsonStr = "";
        if(authResultCode.equals("0000")){
            /*
             ****************************************************************************************
             * <해쉬암호화> (수정하지 마세요)
             * SHA-256 해쉬암호화는 거래 위변조를 막기위한 방법입니다.
             ****************************************************************************************
             */
            String ediDate			= getyyyyMMddHHmmss();
            String signData 		= sha256Enc.encrypt(authToken + mid + amt + ediDate + merchantKey);

            /*
             ****************************************************************************************
             * <승인 요청> Web Manual의 [승인 API 요청 파라미터]
             * 승인에 필요한 데이터 생성 후 server to server 통신을 통해 승인 처리 합니다.
             ****************************************************************************************
             */
            StringBuffer requestData = new StringBuffer();
            requestData.append("TID=").append(txTid).append("&");
            requestData.append("AuthToken=").append(authToken).append("&");
            requestData.append("MID=").append(mid).append("&");
            requestData.append("Amt=").append(amt).append("&");
            requestData.append("EdiDate=").append(ediDate).append("&");
            requestData.append("CharSet=").append("utf-8").append("&");
            requestData.append("SignData=").append(signData);
            resultJsonStr = connectToServer(requestData.toString(), nextAppURL);

            HashMap resultData = new HashMap();
            boolean paySuccess = false;
            if("9999".equals(resultJsonStr)){
                /*
                 *************************************************************************************
                 * <망취소 요청>
                 * 승인 통신중에 Exception 발생시 망취소 처리를 권고합니다.
                 *************************************************************************************
                 */
                StringBuffer netCancelData = new StringBuffer();
                requestData.append("&").append("NetCancel=").append("1");
                String cancelResultJsonStr = connectToServer(requestData.toString(), netCancelURL);

                HashMap cancelResultData = jsonStringToHashMap(cancelResultJsonStr);
                ResultCode = (String)cancelResultData.get("ResultCode");
                ResultMsg = (String)cancelResultData.get("ResultMsg");
            }else{
                resultData = jsonStringToHashMap(resultJsonStr);
                ResultCode 	= (String)resultData.get("ResultCode");	// 결과코드 (정상 결과코드:3001)
                ResultMsg 	= (String)resultData.get("ResultMsg");	// 결과메시지
                PayMethod 	= (String)resultData.get("PayMethod");	// 결제수단
                GoodsName   = (String)resultData.get("GoodsName");	// 상품명
                Amt       	= (String)resultData.get("Amt");		// 결제 금액
                TID       	= (String)resultData.get("TID");		// 거래번호

                //추가
                BuyerName       = (String)resultData.get("BuyerName");
                BuyerEmail      = (String)resultData.get("BuyerEmail");
                VbankNum        = (String)resultData.get("VbankNum");
                VbankBankName   = (String)resultData.get("VbankBankName");
                VbankBankCode   = (String)resultData.get("VbankBankCode");
                VbankExpDate    = (String)resultData.get("VbankExpDate");
                VbankExpTime    = (String)resultData.get("VbankExpTime");
                Moid            = (String)resultData.get("Moid");

                /*
                 *************************************************************************************
                 * <결제 성공 여부 확인>
                 *************************************************************************************
                 */
                if(PayMethod != null){
                    if(PayMethod.equals("CARD")){
                        if(ResultCode.equals("3001")) paySuccess = true; // 신용카드(정상 결과코드:3001)
                    }else if(PayMethod.equals("BANK")){
                        if(ResultCode.equals("4000")) paySuccess = true; // 계좌이체(정상 결과코드:4000)
                    }else if(PayMethod.equals("CELLPHONE")){
                        if(ResultCode.equals("A000")) paySuccess = true; // 휴대폰(정상 결과코드:A000)
                    }else if(PayMethod.equals("VBANK")){
                        if(ResultCode.equals("4100")) paySuccess = true; // 가상계좌(정상 결과코드:4100)
                    }else if(PayMethod.equals("SSG_BANK")){
                        if(ResultCode.equals("0000")) paySuccess = true; // SSG은행계좌(정상 결과코드:0000)
                    }else if(PayMethod.equals("CMS_BANK")){
                        if(ResultCode.equals("0000")) paySuccess = true; // 계좌간편결제(정상 결과코드:0000)
                    }
                }
            }
        }else/*if(authSignature.equals(authComparisonSignature))*/{
            ResultCode 	= authResultCode;
            ResultMsg 	= authResultMsg;
        }/*else{
            System.out.println("인증 응답 Signature : " + authSignature);
            System.out.println("인증 생성 Signature : " + authComparisonSignature);
        }*/

        CallbackOutDto outDto = new CallbackOutDto();
        outDto.setResultCode(ResultCode);
        outDto.setResultMsg(ResultMsg);
        outDto.setPayMethod(PayMethod);
        outDto.setGoodsName(GoodsName);
        outDto.setAmt(Amt);
        outDto.setTid(TID);
        // 추가
        outDto.setBuyerName(BuyerName);
        outDto.setBuyerEmail(BuyerEmail);
        outDto.setVBankBankName(VbankBankName);
        outDto.setVBankBankCode(VbankBankCode);
        outDto.setVBankNum(VbankNum);
        outDto.setVBankExpDate(VbankExpDate);
        outDto.setVBankExpTime(VbankExpTime);
        outDto.setMoid(Moid);

        return outDto;
    }


    public CancelOutDto cancel(CancelInDto inDto) throws Exception {
        /*
         ****************************************************************************************
         * <취소요청 파라미터>
         * 취소시 전달하는 파라미터입니다.
         * 샘플페이지에서는 기본(필수) 파라미터만 예시되어 있으며,
         * 추가 가능한 옵션 파라미터는 연동메뉴얼을 참고하세요.
         ****************************************************************************************
         */
        String tid                  = inDto.getTid();
        String cancelAmt            = inDto.getCancelAmt();
        String partialCancelCode    = "0";
        String mid                  = merchantID;
        String moid                 = inDto.getMoid();
        String cancelMsg            = "고객요청";
        String refundAcctNo         = inDto.getRefundAcctNo();
        String refundBankCd         = inDto.getRefundBankCd();
        String refundAcctNm         = inDto.getRefundAcctNm();

        /*
         ****************************************************************************************
         * <해쉬암호화> (수정하지 마세요)
         * SHA-256 해쉬암호화는 거래 위변조를 막기위한 방법입니다.
         ****************************************************************************************
         */
        DataEncrypt sha256Enc 	= new DataEncrypt();
        String ediDate			= getyyyyMMddHHmmss();
        String signData 		= sha256Enc.encrypt(mid + cancelAmt + ediDate + merchantKey);

        /*
         ****************************************************************************************
         * <취소 요청>
         * 취소에 필요한 데이터 생성 후 server to server 통신을 통해 취소 처리 합니다.
         * 취소 사유(CancelMsg) 와 같이 한글 텍스트가 필요한 파라미터는 euc-kr encoding 처리가 필요합니다.
         ****************************************************************************************
         */
        StringBuffer requestData = new StringBuffer();
        requestData.append("TID=").append(tid).append("&");
        requestData.append("MID=").append(mid).append("&");
        requestData.append("Moid=").append(moid).append("&");
        requestData.append("CancelAmt=").append(cancelAmt).append("&");
        requestData.append("CancelMsg=").append(URLEncoder.encode(cancelMsg, "euc-kr")).append("&");
        requestData.append("PartialCancelCode=").append(partialCancelCode).append("&");
        requestData.append("EdiDate=").append(ediDate).append("&");
        requestData.append("CharSet=").append("utf-8").append("&");
        requestData.append("SignData=").append(signData).append("&");
        requestData.append("RefundAcctNo=").append(refundAcctNo).append("&");
        requestData.append("RefundBankCd=").append(refundBankCd).append("&");
        requestData.append("RefundAcctNm=").append(URLEncoder.encode(refundAcctNm, "euc-kr"));
        String resultJsonStr = connectToServer(requestData.toString(), "https://webapi.nicepay.co.kr/webapi/cancel_process.jsp");

        /*
         ****************************************************************************************
         * <취소 결과 파라미터 정의>
         * 샘플페이지에서는 취소 결과 파라미터 중 일부만 예시되어 있으며,
         * 추가적으로 사용하실 파라미터는 연동메뉴얼을 참고하세요.
         ****************************************************************************************
         */
        String ResultCode 	= "";
        String ResultMsg 	= "";
        String CancelAmt 	= "";
        String CancelDate 	= "";
        String CancelTime   = "";
        String TID 		    = "";

        if("9999".equals(resultJsonStr)){
            ResultCode 	= "9999";
            ResultMsg	= "통신실패";
        }else{
            HashMap resultData = jsonStringToHashMap(resultJsonStr);
            ResultCode 	= (String)resultData.get("ResultCode");	// 결과코드 (취소성공: 2001, 취소성공(LGU 계좌이체):2211)
            ResultMsg 	= (String)resultData.get("ResultMsg");	// 결과메시지
            CancelAmt 	= (String)resultData.get("CancelAmt");	// 취소금액
            CancelDate 	= (String)resultData.get("CancelDate");	// 취소일
            CancelTime 	= (String)resultData.get("CancelTime");	// 취소시간
            TID 		= (String)resultData.get("TID");		// 거래아이디 TID
        }

        CancelOutDto outDto = new CancelOutDto();
        outDto.setResultCode(ResultCode);
        outDto.setResultMsg(ResultMsg);
        outDto.setCancelAmt(CancelAmt);
        outDto.setCancelDate(CancelDate);
        outDto.setCancelTime(CancelTime);
        outDto.setTid(TID);

        return outDto;
    }
}
