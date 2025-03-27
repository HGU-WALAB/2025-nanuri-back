package com.walab.nanuri.itemHistory.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApplicantDto {
    private Long studentId;
    private String name;
    private String rank;
}
