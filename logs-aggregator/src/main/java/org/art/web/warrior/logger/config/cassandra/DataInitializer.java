package org.art.web.warrior.logger.config.cassandra;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;
import org.art.web.warrior.logger.repository.cassandra.KeyspaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import static org.art.web.warrior.logger.ServiceCommonConstants.TASK_MESSAGE_KEYSPACE_NAME;

@Slf4j
@Component
public class DataInitializer implements CommandLineRunner {

    private final KeyspaceRepository keyspaceRepository;

    @Value("${cassandra.replicationStrategy}")
    private String replicationStrategy;

    @Value("${cassandra.replicas}")
    private String replicas;

    @Autowired
    public DataInitializer(KeyspaceRepository keyspaceRepository) {
        this.keyspaceRepository = keyspaceRepository;
    }

    @Override
    public void run(String... args) {
        log.info("Keyspace initialization. Keyspace name: {}, replication strategy: {}, replicas number: {}",
            TASK_MESSAGE_KEYSPACE_NAME, replicationStrategy, replicas);
        keyspaceRepository.createKeyspace(TASK_MESSAGE_KEYSPACE_NAME, replicationStrategy, NumberUtils.toInt(replicas, 1));
    }
}
