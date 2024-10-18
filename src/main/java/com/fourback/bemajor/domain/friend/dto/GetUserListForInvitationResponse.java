package com.fourback.bemajor.domain.friend.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Builder
@Data
public class GetUserListForInvitationResponse {
    private List<UserForInvitationResponse> result;
    private int size;
}