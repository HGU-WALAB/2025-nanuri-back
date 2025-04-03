package com.walab.nanuri.user.entity;

import com.walab.nanuri.auth.dto.AuthDto;
import com.walab.nanuri.commons.util.Tag;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class User {

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

    @ElementCollection(targetClass = Tag.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "user_tags", joinColumns = @JoinColumn(name = "unique_id"))
    @Column(name = "interest_tag")
    private List<Tag> interestTag;

    @Column(name = "hobby", length = 50)
    private String hobby;

    @Column(name = "introduction", length = 300)
    private String introduction;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

//    public void update(AuthDto dto) {
//        this.name = dto.getName();
//        this.email = dto.getEmail();
//        this.grade = dto.getGrade();
//        this.semester = dto.getSemester();
//        this.department = dto.getDepartment();
//        this.major1 = dto.getMajor1();
//        this.major2 = dto.getMajor2();
//    }

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
                .interestTag(dto.getInterestTag())
                .hobby(dto.getHobby())
                .introduction(dto.getIntroduction())
                .build();
    }

    public void editUserDetails(String nickname, String mbti, List<Tag> interestTag,
                                String hobby, String introduction) {
        this.nickname = nickname;
        this.mbti = mbti;
        this.interestTag = interestTag;
        this.hobby = hobby;
        this.introduction = introduction;
    }

}
