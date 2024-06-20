package ru.hse.store.projectApi.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.hse.store.common.config.MapStructConfig;
import ru.hse.store.projectApi.entity.Project;
import ru.hse.store.restApi.dto.project.ProjectRequest;
import ru.hse.store.restApi.dto.project.ProjectResponse;

@Mapper(config = MapStructConfig.class, componentModel = "spring")
public interface ProjectMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "type", ignore = true)
    @Mapping(target = "photos", ignore = true)
    @Mapping(target = "icon", ignore = true)
    @Mapping(target = "workBinary", ignore = true)
    @Mapping(target = "creatorId", ignore = true)
    @Mapping(target = "status", ignore = true)
    Project map(ProjectRequest dto);

    ProjectResponse map(Project project);
}
