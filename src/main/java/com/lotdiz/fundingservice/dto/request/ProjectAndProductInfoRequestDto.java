package com.lotdiz.fundingservice.dto.request;

import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProjectAndProductInfoRequestDto {
    private Long projectId;
    private List<Long> productIds;

    public static ProjectAndProductInfoRequestDto toDto(Long projectId, List<Long> productIds){
        return ProjectAndProductInfoRequestDto.builder()
                .projectId(projectId)
                .productIds(productIds)
                .build();
    }
}
