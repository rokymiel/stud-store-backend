package ru.hse.store.webBuisnessApi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.hse.store.projectApi.service.ProjectService;
import ru.hse.store.restApi.dto.project.ProjectResponse;
import ru.hse.store.restApi.dto.webstore.LandingResponse;
import ru.hse.store.restApi.dto.webstore.StoreScreenResponse;
import ru.hse.store.restApi.endpoints.webstore.PublicWebStoreApi;
import ru.hse.store.restApi.endpoints.webstore.StudentWebStoreApi;
import ru.hse.store.webBuisnessApi.service.AppStoreService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1")
public class WebStoreController implements PublicWebStoreApi, StudentWebStoreApi {

    private final ProjectService projectService;
    private final AppStoreService appStoreService;

    //
    // PUBLIC ZONE
    //

    @Override
    public List<ProjectResponse> publicSearch(@RequestParam String query) {

        return projectService.search(query, true);
    }

    @Override
    public StoreScreenResponse publicStoreScreen() {
        return appStoreService.storeScreen(false);
    }

    @Override
    public LandingResponse landingProjects() {
        // Из конфига вытаскиваем проекты
        return appStoreService.landing();
    }

    //
    // STUDENT ZONE
    //

    @Override
    public List<ProjectResponse> studentSearch(@RequestParam String query) {
        return projectService.search(query, false);
    }

    @Override
    public StoreScreenResponse studentStoreScreen() {
        return appStoreService.storeScreen(true);
    }
}
