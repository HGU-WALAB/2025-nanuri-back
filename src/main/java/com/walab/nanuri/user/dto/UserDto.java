package com.walab.nanuri.user.dto;

import com.walab.nanuri.commons.util.Category;
import com.walab.nanuri.user.entity.User;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class UserDto {
  private String uniqueId;
  private String name;
  private String status;
  private String email;
  private Integer grade;
  private Integer semester;
  private String department;
  private String major1;
  private String major2;
  private String nickname;
  private String mbti;
  private List<Category> interestCategory;
  private String hobby;
  private String introduction;


  public static UserDto from(User user) {
    return UserDto.builder()
        .uniqueId(user.getUniqueId())
        .name(user.getName())
        .status(user.getStatus().toString())
        .email(user.getEmail())
        .grade(user.getGrade())
        .semester(user.getSemester())
        .department(user.getDepartment())
        .major1(user.getMajor1())
        .major2(user.getMajor2())
        .nickname(user.getNickname())
        .mbti(user.getMbti())
        .interestCategory(user.getInterestCategory())
        .hobby(user.getHobby())
        .introduction(user.getIntroduction())
        .build();
  }
}