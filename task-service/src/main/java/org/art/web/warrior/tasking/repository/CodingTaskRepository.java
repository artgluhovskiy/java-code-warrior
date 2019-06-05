package org.art.web.warrior.tasking.repository;

import org.art.web.warrior.tasking.model.CodingTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CodingTaskRepository extends JpaRepository<CodingTask, Long> {

    Optional<CodingTask> getCodingTaskByNameId(String nameId);

    void deleteByNameId(String nameId);
}
