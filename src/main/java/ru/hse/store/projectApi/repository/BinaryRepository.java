package ru.hse.store.projectApi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.hse.store.projectApi.entity.Binary;
import ru.hse.store.projectApi.entity.Project;

@Repository
public interface BinaryRepository extends JpaRepository<Binary, Long> {
}
