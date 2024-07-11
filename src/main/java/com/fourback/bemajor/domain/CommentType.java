package com.fourback.bemajor.domain;

public enum CommentType {
    DEFAULT(0), DELETED(1);

    private int value;

    CommentType(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }
}
