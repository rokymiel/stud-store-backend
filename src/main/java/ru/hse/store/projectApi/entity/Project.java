package ru.hse.store.projectApi.entity;

import jakarta.persistence.*;
import jakarta.ws.rs.DefaultValue;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.ColumnDefault;

import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
@Entity
@Table(name = "project")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;

    @Column(name = "creator_id")
    private Long creatorId;

    @Column(name = "short_name")
    private String shortName;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "description")
    private String description;

    @Column(name = "defence_year")
    private String defenceYear;

    @Column(name = "type")
    private String type = "project-work";

    @Column(name = "group_name")
    private String groupName;

    @Column(name = "icon")
    private String icon;

    @Column(name = "photos")
    private List<String> photos;

    @Column(name = "work_binary")
    private String workBinary;

    @Column(name = "mentor")
    private String mentor; // WIP заменить на модель!!

    @Column(name = "status")
    private String status = "NEW";

    public enum Status {
        NEW, PRIVATE, STUDENT
    }
}