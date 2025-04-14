package com.walab.nanuri.user.entity;

import com.walab.nanuri.auth.dto.AuthDto;
import com.walab.nanuri.commons.entity.BaseTimeEntity;
import com.walab.nanuri.commons.util.Category;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class User extends BaseTimeEntity {

    @Id
    @Column(name = "unique_id", nullable = false, length = 50)
    private String uniqueId;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private UserStatus status;

    @Column(name = "email", length = 320)
    private String email;

    @Column(name = "grade")
    private Integer grade;

    @Column(name = "semester")
    private Integer semester;

    @Column(name = "department", length = 50)
    private String department;

    @Column(name = "major1", length = 50)
    private String major1;

    @Column(name = "major2", length = 50)
    private String major2;

    @Column(name = "nickname", length = 50)
    private String nickname;

    @Column(name = "mbti", length = 10)
    private String mbti;

    @ElementCollection(targetClass = Category.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "user_tags", joinColumns = @JoinColumn(name = "unique_id"))
    @Column(name = "interest_tag")
    private List<Category> interestCategory;

    @Column(name = "introduction", length = 300)
    private String introduction;

    public static User from(AuthDto dto) {
        return User.builder()
                .uniqueId(dto.getUniqueId())
                .name(dto.getName())
                .email(dto.getEmail())
                .status(UserStatus.USER)
                .grade(dto.getGrade())
                .semester(dto.getSemester())
                .department(dto.getDepartment())
                .major1(dto.getMajor1())
                .major2(dto.getMajor2())
                .nickname(dto.getNickname())
                .mbti(dto.getMbti())
                .interestCategory(dto.getInterestCategory())
                .introduction(dto.getIntroduction())
                .build();
    }

    public void editUserDetails(String nickname, String mbti, List<Category> interestCategory,
                                String introduction) {
        this.nickname = nickname;
        this.mbti = mbti;
        this.interestCategory = interestCategory;
        this.introduction = introduction;
    }

}
