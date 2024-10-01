package com.fourback.bemajor.domain.friend.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
@Data
public class AddFriendApplyResponse {
    private long applyNumber;
}