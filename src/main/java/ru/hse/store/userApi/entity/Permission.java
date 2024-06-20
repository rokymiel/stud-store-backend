package ru.hse.store.userApi.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Accessors(chain = true)
@Entity
@Table(name = "permission")
@SequenceGenerator(allocationSize = 1, name = "permission_seq", sequenceName = "permission_seq")
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "permission_seq")
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToMany(mappedBy = "permissions", fetch = FetchType.LAZY)
    private Set<User> users = new HashSet<>();

    @PreRemove
    private void removePermissionFromRoles() {
        users.forEach(user -> user.getPermissions().remove(this));
    }

}