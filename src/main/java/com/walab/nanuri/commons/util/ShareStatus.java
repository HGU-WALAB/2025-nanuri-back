package com.walab.nanuri.commons.util;

import lombok.Getter;

@Getter
public enum ShareStatus {
    NONE("None"), //나눔중
    IN_PROGRESS("In Progress"), // 거래중 (누군가 나눔신청하여 서로 얘기를 해서 나눔이 성사된 상태)
    COMPLETED("Completed"); //나눔 완료

    private final String label;

    ShareStatus(String label) {
        this.label = label;
    }
}
