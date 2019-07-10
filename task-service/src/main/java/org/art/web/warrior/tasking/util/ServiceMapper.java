package org.art.web.warrior.tasking.util;

import org.art.web.warrior.commons.tasking.dto.TaskDescriptorDto;
import org.art.web.warrior.commons.tasking.dto.TaskDto;
import org.art.web.warrior.commons.tasking.event.TaskPublicationMessage;
import org.art.web.warrior.tasking.model.CodingTask;
import org.art.web.warrior.tasking.model.CodingTaskDescriptor;

import java.time.LocalDateTime;
import java.util.Objects;

public class ServiceMapper {

    private ServiceMapper() {}

    public static TaskDto mapToTaskDto(CodingTask task) {
        Objects.requireNonNull(task, "Coding task instance, passed to the mapper, should not be null!");
        CodingTaskDescriptor taskDescriptor = task.getDescriptor();
        TaskDescriptorDto taskDescriptorDto = mapToTaskDescriptorDto(taskDescriptor);
        return TaskDto.builder()
            .descriptor(taskDescriptorDto)
            .methodSign(task.getMethodSign())
            .runnerClassData(task.getRunnerClassData())
            .publicationDate(task.getPublicationDate())
            .updateDate(task.getUpdateDate())
            .build();
    }

    public static CodingTask mapToCodingTask(TaskDto taskDto) {
        Objects.requireNonNull(taskDto, "Task DTO instance, passed to the mapper, should not be null!");
        TaskDescriptorDto taskDescriptorDto = taskDto.getDescriptor();
        CodingTaskDescriptor codingTaskDescriptor = CodingTaskDescriptor.builder()
            .nameId(taskDescriptorDto.getNameId())
            .name(taskDescriptorDto.getName())
            .description(taskDescriptorDto.getDescription())
            .rating(taskDescriptorDto.getRating())
            .build();
        return CodingTask.builder()
            .descriptor(codingTaskDescriptor)
            .runnerClassData(taskDto.getRunnerClassData())
            .methodSign(taskDto.getMethodSign())
            .build();
    }

    public static TaskDescriptorDto mapToTaskDescriptorDto(CodingTaskDescriptor taskDescriptor) {
        Objects.requireNonNull(taskDescriptor, "Task description instance, passed to the mapper, should not be null!");
        return TaskDescriptorDto.builder()
            .nameId(taskDescriptor.getNameId())
            .name(taskDescriptor.getName())
            .description(taskDescriptor.getDescription())
            .rating(taskDescriptor.getRating())
            .build();
    }

    public static TaskPublicationMessage mapToTaskPubEvent(CodingTask publishedTask) {
        Objects.requireNonNull(publishedTask, "Published task instance, passed to the mapper, should not be null!");
        return TaskPublicationMessage.builder()
                .message("taskService.publication")
                .pubDate(LocalDateTime.now())
                .nameId(publishedTask.getDescriptor().getNameId())
                .name(publishedTask.getDescriptor().getName())
                .description(publishedTask.getDescriptor().getDescription())
                .build();
    }
}
