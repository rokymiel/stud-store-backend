package ru.hse.store.webBuisnessApi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hse.store.projectApi.mapper.ProjectMapper;
import ru.hse.store.projectApi.repository.ProjectRepository;
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

@Service
@RequiredArgsConstructor
public class AppStoreService {

    @Value("${store.public.collections-config.path}")
    private String publicConfigPath;
    @Value("${store.student.collections-config.path}")
    private String studentConfigPath;
    @Value("${store.public.landing-config.path}")
    private String landingConfigPath;

    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;
    private final StoreCollectionParser parser;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional(readOnly = true)
    public StoreScreenResponse storeScreen(Boolean student) {
        var projects = projectRepository.findAll();
        var appsList = projects.stream().map(projectMapper::map).toList();

        var config = student ? studentConfigPath : publicConfigPath;
        try {
            Reader reader = new FileReader(config);
            var configItems = parser.parseBlocksFromJson(reader);
            var collection = configItems.stream().map(projectMapper::map).toList();

            return new StoreScreenResponse(collection, appsList);
        } catch (FileNotFoundException e) {
            throw new StudStoreException("Internal Error");
        }
    }

    @Transactional(readOnly = true)
    public LandingResponse landing() {
        try {
            Reader reader = new FileReader(landingConfigPath);
            var configItems = parser.parseLandingConfig(reader);
            List<LandingProjectResponse> bestProjects = new ArrayList<>();

            for (var p : configItems) {
                var userId = p.getCreatorId();
                userRepository.findById(userId).ifPresent( (user) -> {
                    var proj = projectMapper.map(p);
                    var creator = userMapper.mapToShort(user);
                    bestProjects.add(new LandingProjectResponse(proj, creator));
                });
            }

            return new LandingResponse(bestProjects);
        } catch (FileNotFoundException e) {
            throw new StudStoreException("Internal Error");
        }
    }
}
