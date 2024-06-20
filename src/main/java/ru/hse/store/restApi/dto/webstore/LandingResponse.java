package ru.hse.store.restApi.dto.webstore;

import ru.hse.store.restApi.dto.project.ProjectResponse;
import ru.hse.store.restApi.dto.user.ShortUserResponse;

import java.util.List;

public record LandingResponse(
        List<LandingProjectResponse> bestProjects
) {
}
