package org.art.web.warrior.tasking.repository;

import org.art.web.warrior.tasking.model.CodingTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CodingTaskRepository extends JpaRepository<CodingTask, Long> {
}
