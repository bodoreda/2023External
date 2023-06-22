package msa.external.v1.nicepay.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Order {
    @JsonIgnore
    private int odid;
    @JsonIgnore
    private int cuid;
    @JsonProperty
    private String tid;
    @JsonProperty
    private String moid;
    @JsonProperty
    private String goods;
    @JsonProperty
    private String price;
    @JsonProperty("payMethod")
    private String pay_method;
    @JsonIgnore
    private String bank_code;
    @JsonProperty("vbankName")
    private String vbank_name;
    @JsonProperty("vbankNum")
    private String vbank_num;
    @JsonProperty("vbankExp")
    private String vbank_exp;
    @JsonProperty("odStatus")
    private String od_status;

    @Override
    public String toString() {
        String result =
                "tid : " + tid + " / "  +
                "moid : " + moid + " / " +
                "goods : " + goods + " / " +
                "price : " + price + " / " +
                "payMethod : " + pay_method + " / " +
                "vBankName : " + vbank_name + " / " +
                "vBankNum : " + vbank_num + " / " +
                "vBankExp : " + vbank_exp + " / " +
                "odStatus : " + od_status;
        return result;
    }
}
