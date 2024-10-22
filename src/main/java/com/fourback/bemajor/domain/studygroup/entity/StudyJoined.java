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

    @Column(name = "is_alarm_set")
    private Boolean isAlarmSet;

    public StudyJoined(StudyGroup studyGroup, UserEntity userEntity, Boolean isAlarmSet) {
        this.studyGroup = studyGroup;
        this.user = userEntity;
        this.isAlarmSet = isAlarmSet;
    }

    public void changeAlarmSet(Boolean isAlarmSet) {
        this.isAlarmSet = isAlarmSet;
    }
}
