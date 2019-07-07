package org.art.web.warrior.tasking.repository.mongo;

import org.art.web.warrior.tasking.model.CodingTask;
import org.springframework.data.mongodb.repository.DeleteQuery;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CodingTaskMongoRepository extends MongoRepository<CodingTask, String> {

    @Query("{ 'descriptor.nameId' : ?0 }")
    Optional<CodingTask> getCodingTaskByNameId(String nameId);

    @DeleteQuery("{ 'descriptor.nameId' : ?0 }")
    void deleteByNameId(String nameId);
}
