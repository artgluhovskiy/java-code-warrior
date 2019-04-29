package org.art.web.warrior.client.service;

import org.art.web.warrior.commons.tasking.CodingTask;
import org.art.web.warrior.client.service.api.TaskServiceClient;
import org.art.web.warrior.client.service.api.CompServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class TaskServiceClientImpl implements TaskServiceClient {

    private final CompServiceClient compServiceClient;

    private final Map<Long, CodingTask> codingTaskStorage = new HashMap<>();

    @Autowired
    public TaskServiceClientImpl(CompServiceClient compServiceClient) {
        this.compServiceClient = compServiceClient;
    }

    @Override
    public CodingTask publishNewCodingTask(CodingTask task) {
        return null;
    }
}
