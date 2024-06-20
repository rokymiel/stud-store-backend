package ru.hse.store.restApi.dto.project;

public record ProjectRequest(
        String shortName,
        String fullName,
        String description,
        String defenceYear,
        String groupName,
        String mentor
) {

}

