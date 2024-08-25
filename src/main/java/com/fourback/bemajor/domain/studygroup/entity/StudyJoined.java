package com.fourback.bemajor.domain.studygroup.entity;

import com.fourback.bemajor.domain.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class StudyJoined {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @ManyToOne
    private StudyGroup studyGroup;
    @ManyToOne
    private UserEntity user;

    public StudyJoined(StudyGroup studyGroup, UserEntity userEntity) {
        this.studyGroup = studyGroup;
        this.user = userEntity;
    }

    public void setStudyJoined(StudyGroup studyGroup, UserEntity userEntity){
        this.studyGroup = studyGroup;
        studyGroup.getStudyJoineds().add(this);
        this.user = userEntity;
        userEntity.getStudyJoineds().add(this);
    }
}
