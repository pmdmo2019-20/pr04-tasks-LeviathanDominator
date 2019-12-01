package es.iessaladillo.pedrojoya.pr04.data

import android.os.Build
import androidx.annotation.RequiresApi
import es.iessaladillo.pedrojoya.pr04.data.entity.Task
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

// TODO: Crea una clase llamada LocalRepository que implemente la interfaz Repository
//  usando una lista mutable para almacenar las tareas.
//  Los id de las tareas se irán generando secuencialmente a partir del valor 1 conforme
//  se van agregando tareas (add).

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
        for (task in tasks){
            if (task.id == taskId){
                mutableTasks.remove(task)
            }
        }
    }

    override fun deleteTasks(taskIdList: List<Long>) {
        for (id in taskIdList){
            System.out.println(id)
            System.out.println("Los tasks que buscan este id")
            for (task in tasks){
                System.out.println(task.id)
                if (task.id == id){

                    System.out.println("ESTE ID HA SIDO BORRADO")
                    mutableTasks.remove(task)
                    return
                }
            }
        }
    }

    override fun markTaskAsCompleted(taskId: Long) {
        for (task in mutableTasks) {
            if (task.id == taskId) {
                task.completed = true
            }
        }
    }

    override fun markTasksAsCompleted(taskIdList: List<Long>) {
        for (task in mutableTasks) {
            for (id in taskIdList) {
                if (task.id == id) {
                    task.completed = true
                }
            }
        }
    }

    override fun markTaskAsPending(taskId: Long) {
        for (task in mutableTasks) {
            if (task.id == taskId) {
                task.completed = false
            }
        }
    }

    override fun markTasksAsPending(taskIdList: List<Long>) {
        for (task in mutableTasks) {
            for (id in taskIdList) {
                if (task.id == id) {
                    task.completed = false
                }
            }
        }
    }

}