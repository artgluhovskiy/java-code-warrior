package org.art.web.warrior.tasking.repository;

import org.art.web.warrior.tasking.model.CodingTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CodingTaskRepository extends JpaRepository<CodingTask, Long> {

    @Query("select t from CodingTask t where t.descriptor.nameId = :nameId")
    Optional<CodingTask> getCodingTaskByNameId(@Param("nameId") String nameId);

    @Modifying
    @Query("delete from CodingTask t where t.descriptor.nameId = :nameId")
    void deleteByNameId(@Param("nameId") String nameId);
}
