package ru.hse.store.userApi.security.permission;

import org.springframework.http.ResponseEntity;
import ru.hse.store.restApi.exceptions.StudStoreException;

public enum StaticRole {
    ADMIN, BASIC, STUDENT, EDU_OFFICER, MENTOR;

    public static StaticRole from(String string) {
        return switch (string) {
            case "admin" -> ADMIN;
            case "basic" -> BASIC;
            case "student" -> STUDENT;
            case "edu-officer" -> EDU_OFFICER;
            case "mentor" -> MENTOR;
            default -> throw new StudStoreException("Wrong user type!");
        };
    }

    public String getAuthorityName() {
        return "ROLE_"+this;
    }
}
