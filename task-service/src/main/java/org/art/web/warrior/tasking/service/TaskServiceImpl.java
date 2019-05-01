package org.art.web.warrior.tasking.service;

import org.art.web.warrior.tasking.model.CodingTask;
import org.art.web.warrior.tasking.service.api.TaskService;
import org.springframework.stereotype.Service;

@Service
public class TaskServiceImpl implements TaskService {


    @Override
    public CodingTask publishTask(CodingTask task) {
        //Storing Coding Task in the database
        return null;
    }
}
