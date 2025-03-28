package com.walab.nanuri.history.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
//(물건 나눔자만 볼 수 있음)
// 나눔 신청한 사람 리스트
public class ApplicantDto {
    private String nickName;
    private String applicationTime;
}
