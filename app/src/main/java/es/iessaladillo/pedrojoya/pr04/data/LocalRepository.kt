package es.iessaladillo.pedrojoya.pr04.data

import android.os.Build
import androidx.annotation.RequiresApi
import es.iessaladillo.pedrojoya.pr04.data.entity.Task
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object LocalRepository : Repository {

    private var id: Long = 1

    private val mutableTasks: MutableList<Task> = mutableListOf()
    private val tasks: List<Task> = mutableTasks

    override fun queryAllTasks(): List<Task> {
        return tasks
    }

    override fun queryCompletedTasks(): List<Task> {
        return tasks.filter { it.completed }
    }

    override fun queryPendingTasks(): List<Task> {
        return tasks.filter { !it.completed }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun addTask(concept: String) {
        val format: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")
        mutableTasks.add(
            Task(
                ++id,
                concept,
                LocalDateTime.now().format(format),
                false,
                LocalDateTime.now().format(format)
            )
        )
    }

    override fun insertTask(task: Task) {
        mutableTasks.add(task)
    }

    override fun deleteTask(taskId: Long) {
        val iterator = mutableTasks.iterator()
        while (iterator.hasNext()) {
            val task = iterator.next()
            if (task.id == taskId) {
                iterator.remove()
            }
        }
    }

    override fun deleteTasks(taskIdList: List<Long>) {
        for (id in taskIdList) {
            deleteTask(id)
        }
    }

    override fun markTaskAsCompleted(taskId: Long) {
        for (task in mutableTasks) {
            if (task.id == taskId && !task.completed) {
                task.completed = true
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    task.complete()
                }
            }
        }
    }

    override fun markTasksAsCompleted(taskIdList: List<Long>) {
        for (id in taskIdList) {
            markTaskAsCompleted(id)
        }
    }

    override fun markTaskAsPending(taskId: Long) {
        for (task in mutableTasks) {
            if (task.id == taskId && task.completed) {
                task.completed = false
            }
        }
    }

    override fun markTasksAsPending(taskIdList: List<Long>) {
        for (id in taskIdList) {
            markTaskAsPending(id)
        }
    }

}