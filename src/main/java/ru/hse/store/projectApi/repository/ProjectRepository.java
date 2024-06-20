package ru.hse.store.projectApi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.hse.store.projectApi.entity.Project;
import ru.hse.store.userApi.entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findByCreatorId(Long creatorId);

        @Query(value = "SELECT p FROM Project p " +
            "WHERE lower(p.shortName) LIKE lower(concat('%', :query, '%')) " +
            "OR lower(p.fullName) LIKE lower(concat('%', :query, '%'))")
    List<Project> findByQuery(@Param("query") String query);
}