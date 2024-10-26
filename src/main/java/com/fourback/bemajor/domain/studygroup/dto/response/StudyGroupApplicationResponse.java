package com.fourback.bemajor.domain.studygroup.dto.response;

import com.fourback.bemajor.domain.studygroup.entity.StudyGroup;
import com.fourback.bemajor.domain.studygroup.entity.StudyJoinApplication;
import com.fourback.bemajor.domain.user.entity.UserEntity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StudyGroupApplicationResponse {
    private Long id;
    private String userName;
    private String imageUrl;
    private String belong;
    private String department;

    public static StudyGroupApplicationResponse fromEntity(StudyJoinApplication application){
        return new StudyGroupApplicationResponse(application.getId(), application.getUser().getUserName(),
                application.getUser().getImageUrl(),application.getUser().getBelong(),application.getUser().getDepartment());
    }
}
