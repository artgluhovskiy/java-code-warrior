package org.art.web.warrior.tasking.repository;

import org.art.web.warrior.tasking.model.CodingTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CodingTaskRepository extends JpaRepository<CodingTask, Long> {

    CodingTask getCodingTaskByNameId(String nameId);

    <T> List<T> getCodingTasks(Class<T> type);
}
