package ru.hse.store.adminApi.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jsonpatch.JsonPatch;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.hse.store.adminApi.service.AdminService;
import ru.hse.store.projectApi.service.ProjectService;
import ru.hse.store.restApi.dto.admin.AdminNewStatusRequest;
import ru.hse.store.restApi.dto.admin.AdminProjectResponse;
import ru.hse.store.restApi.dto.project.ProjectResponse;
import ru.hse.store.restApi.endpoints.AdminConsoleApi;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1")
public class AdminConsoleController implements AdminConsoleApi {

    private final AdminService adminService;

    @Override
    public ResponseEntity<?> updateProjectStatus(AdminNewStatusRequest dto, Long projectId) {
        adminService.updateStatus(dto, projectId);
        return ResponseEntity.ok().build();
    }

    @Override
    public List<AdminProjectResponse> projectsList() {
        return adminService.projectsList();
    }
}
