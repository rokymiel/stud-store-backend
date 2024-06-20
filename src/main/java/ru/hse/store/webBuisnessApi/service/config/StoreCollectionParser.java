package ru.hse.store.webBuisnessApi.service.config;

import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;
import ru.hse.store.projectApi.entity.Project;
import ru.hse.store.projectApi.repository.ProjectRepository;
import ru.hse.store.restApi.exceptions.StudStoreException;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StoreCollectionParser {
    private final JSONParser parser = new JSONParser();
    private final ProjectRepository projectRepository;

    public List<Project> parseBlocksFromJson(Reader reader) {
        JSONArray jsonBlocks;
        List<Project> items = new ArrayList<>();

        try {
            jsonBlocks = (JSONArray) parser.parse(reader);
        } catch (ParseException e) {
            throw new StudStoreException("Server configs error");
        } catch (IOException e) {
            throw new StudStoreException("Problems with reading EventCollectionsConfig file");
        }

        for (Object obj : jsonBlocks) {
            var json = (JSONObject) obj;
            var id = (Long) json.get("id");
            var p = projectRepository.findById(id);
            p.ifPresent(items::add);
        }
        return  items;
    }

    public List<Project> parseLandingConfig(Reader reader) {
        JSONArray jsonBlocks;
        List<Project> items = new ArrayList<>();

        try {
            var bestWorks = (JSONObject) parser.parse(reader);
            jsonBlocks = (JSONArray) bestWorks.get("best_works");
        } catch (ParseException e) {
            throw new StudStoreException("Server configs error");
        } catch (IOException e) {
            throw new StudStoreException("Problems with reading EventCollectionsConfig file");
        }

        for (Object obj : jsonBlocks) {
            var json = (JSONObject) obj;
            var id = (Long) json.get("id");
            var p = projectRepository.findById(id);
            p.ifPresent(items::add);
        }
        return  items;
    }
}
