package com.walab.nanuri.auth.dto;

import com.walab.nanuri.auth.dto.request.LoginRequest;
import com.walab.nanuri.commons.util.Tag;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class AuthDto {
  private String uniqueId;
  private String hisnetToken; // 히즈넷에서 받은 토큰
  private String token; // jwt
  private String name;
  private String email;
  private String department;
  private String major1;
  private String major2;
  private Integer grade;
  private Integer semester;
  private String nickname;
  private String mbti;
  private List<Tag> interestTag;
  private String hobby;
  private String introduction;


  public static AuthDto from(LoginRequest request) {
    return AuthDto.builder().hisnetToken(request.getHisnetToken()).build();
  }
}
