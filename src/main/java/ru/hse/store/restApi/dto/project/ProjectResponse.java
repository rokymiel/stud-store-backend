package ru.hse.store.restApi.dto.project;

import java.util.List;

public record ProjectResponse(
        String id,
        String creatorId,
        String shortName,
        String fullName,
        String description,
        String defenceYear,
        String groupName,
        String icon,
        List<String> photos,
        String workBinary,
        String mentor,
        String status
) {

}
