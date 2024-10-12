package com.fourback.bemajor.domain.friend.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Builder
@Getter
@Data
public class GetFriendResponse {
    FriendResponse result;
}
