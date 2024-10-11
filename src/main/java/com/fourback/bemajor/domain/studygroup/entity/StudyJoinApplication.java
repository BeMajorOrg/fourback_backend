package com.fourback.bemajor.domain.studygroup.entity;

import com.fourback.bemajor.domain.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.lang.reflect.Member;

@NoArgsConstructor
@Getter
@Entity
public class StudyJoinApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private UserEntity user;
    @ManyToOne
    private StudyGroup studyGroup;

    public StudyJoinApplication(UserEntity user, StudyGroup studyGroup) {
        this.user = user;
        this.studyGroup = studyGroup;
    }
}
