package org.art.web

import org.gradle.api.Task
import org.gradle.api.execution.TaskExecutionGraph
import org.gradle.api.execution.TaskExecutionGraphListener

class ReleaseVersionListener implements TaskExecutionGraphListener {

    final static String releaseTaskPath = ':hello-module:makeReleaseVersionMod'

    @Override
    void graphPopulated(TaskExecutionGraph taskGraph) {
        println 'Inside of Task Graph Listener'
        println "$taskGraph.allTasks"
        if (taskGraph.hasTask(releaseTaskPath)) {
            List<Task> allTasks = taskGraph.allTasks
            Task releaseTask = allTasks.find { it.path == releaseTaskPath }
            if (releaseTask != null) {
                println 'Listener: Release Task!'
            }
        }
    }
}
