package ru.hse.store.restApi.dto.webstore;

import ru.hse.store.restApi.dto.project.ProjectResponse;
import ru.hse.store.restApi.dto.user.ShortUserResponse;

public record LandingProjectResponse(
        ProjectResponse project,
        ShortUserResponse creator
) {

}