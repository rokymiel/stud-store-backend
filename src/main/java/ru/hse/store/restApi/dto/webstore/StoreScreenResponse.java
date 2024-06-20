package ru.hse.store.restApi.dto.webstore;

import ru.hse.store.restApi.dto.project.ProjectResponse;

import java.util.List;

public record StoreScreenResponse(
        List<ProjectResponse> collection,
        List<ProjectResponse> allApps
) {

}
