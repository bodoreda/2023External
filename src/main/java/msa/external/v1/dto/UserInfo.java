package msa.external.v1.dto;

import lombok.Data;

import java.util.List;

/**
 * packageName : msa.external.v1.dto
 * fileName : UserInfo
 * author : BH
 * date : 2023-07-05
 * description :
 * ================================================
 * DATE                AUTHOR              NOTE
 * ================================================
 * 2023-07-05       UserInfo       최초 생성
 */
@Data
public class UserInfo {
    private String loginId;
    private String userName;
    private String phone;
    private String email;
    private String addr;
    private String addrDtl;
    private List<String> roles;
}
