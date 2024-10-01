package com.fourback.bemajor.domain.friend.dto;

import lombok.Data;

@Data
public class FriendRequest {
    public record Apply(
            Long userId,
            Long friendId
    ) {}

    public record Delete(
            Long userId,
            Long friendId
    ) {}
    }