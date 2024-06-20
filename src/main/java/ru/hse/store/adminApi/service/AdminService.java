package ru.hse.store.adminApi.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hse.store.projectApi.mapper.ProjectMapper;
import ru.hse.store.projectApi.repository.ProjectRepository;
import ru.hse.store.restApi.dto.admin.AdminNewStatusRequest;
import ru.hse.store.restApi.dto.admin.AdminProjectResponse;
import ru.hse.store.restApi.dto.webstore.LandingProjectResponse;
import ru.hse.store.restApi.dto.webstore.LandingResponse;
import ru.hse.store.restApi.dto.webstore.StoreScreenResponse;
import ru.hse.store.restApi.exceptions.StudStoreException;
import ru.hse.store.userApi.mapper.UserMapper;
import ru.hse.store.userApi.repository.UserRepository;
import ru.hse.store.webBuisnessApi.service.config.StoreCollectionParser;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final ObjectMapper objectMapper;


    @Transactional(readOnly = true)
    public List<AdminProjectResponse> projectsList() {
        var projects = projectRepository.findAll();
        List<AdminProjectResponse> allProjects = new ArrayList<>();

        for (var p : projects) {
            var userId = p.getCreatorId();
            userRepository.findById(userId).ifPresent((user) -> {
                var proj = projectMapper.map(p);
                var creator = userMapper.mapToShort(user);
                allProjects.add(new AdminProjectResponse(proj, creator));
            });
        }

        return allProjects;
    }

    public void updateStatus(AdminNewStatusRequest dto, Long projectId) {
//        JsonNode patchJson = objectMapper.convertValue(patch, JsonNode.class);
        String newStatus = dto.newStatus();//patchJson.get("new_status").toString();

        if (!Objects.equals(newStatus, "NEW") && !Objects.equals(newStatus, "PRIVATE") && !Objects.equals(newStatus, "PUBLIC")) {
            throw new StudStoreException("Wrong status name!");
        }

        projectRepository.findById(projectId).ifPresent(project -> {
            project.setStatus(newStatus);
            projectRepository.save(project);
        });

    }
}
