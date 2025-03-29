package com.walab.nanuri.history.dto.response;

import com.walab.nanuri.commons.util.Time;
import com.walab.nanuri.history.entity.History;
import com.walab.nanuri.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
//(물건 나눔자만 볼 수 있음)
// 나눔 신청한 사람 리스트
public class ApplicantDto {
    private Long historyId;
    private String nickName;
    private String applicationTime;

    public static ApplicantDto from(History history, User user){
        return ApplicantDto.builder()
                .historyId(history.getId())
                .nickName(user.getNickname())
                .applicationTime(Time.calculateTime(Timestamp.valueOf(history.getCreatedTime())))
                .build();
    }
}
