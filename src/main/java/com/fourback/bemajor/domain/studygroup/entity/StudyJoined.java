package com.fourback.bemajor.domain.studygroup.entity;

import com.fourback.bemajor.domain.user.entity.User;
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
    private User user;

    public StudyJoined(StudyGroup studyGroup, User user) {
        this.studyGroup = studyGroup;
        this.user = user;
    }

    public void setStudyJoined(StudyGroup studyGroup, User user){
        this.studyGroup = studyGroup;
        studyGroup.getStudyJoineds().add(this);
        this.user = user;
        user.getStudyJoineds().add(this);
    }
}
