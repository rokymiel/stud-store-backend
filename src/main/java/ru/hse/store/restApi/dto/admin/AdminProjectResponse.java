package ru.hse.store.restApi.dto.admin;

import ru.hse.store.restApi.dto.project.ProjectResponse;
import ru.hse.store.restApi.dto.user.ShortUserResponse;

public record AdminProjectResponse(
        ProjectResponse project,
        ShortUserResponse creator
) {

}