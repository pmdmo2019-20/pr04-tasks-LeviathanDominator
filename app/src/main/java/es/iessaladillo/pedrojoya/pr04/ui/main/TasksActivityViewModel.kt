package es.iessaladillo.pedrojoya.pr04.ui.main

import android.app.Application
import android.content.Intent
import android.os.Build
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.snackbar.Snackbar
import es.iessaladillo.pedrojoya.pr04.R
import es.iessaladillo.pedrojoya.pr04.base.Event
import es.iessaladillo.pedrojoya.pr04.data.Repository
import es.iessaladillo.pedrojoya.pr04.data.entity.Task
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class TasksActivityViewModel(
    private val repository: Repository,
    private val application: Application
) : ViewModel() {

    // Estado de la interfaz

    private val mutableTasks: MutableLiveData<List<Task>> = MutableLiveData()
    val tasks: LiveData<List<Task>>
        get() = mutableTasks

    init {
        mutableTasks.value = repository.queryAllTasks()
    }

    private val _currentFilter: MutableLiveData<TasksActivityFilter> =
        MutableLiveData(TasksActivityFilter.ALL)

    private val _currentFilterMenuItemId: MutableLiveData<Int> =
        MutableLiveData(R.id.mnuFilterAll)
    val currentFilterMenuItemId: LiveData<Int>
        get() = _currentFilterMenuItemId

    private val _activityTitle: MutableLiveData<String> =
        MutableLiveData(application.getString(R.string.tasks_title_all))
    val activityTitle: LiveData<String>
        get() = _activityTitle

    private val _lblEmptyViewText: MutableLiveData<String> =
        MutableLiveData(application.getString(R.string.tasks_no_tasks_yet))
    val lblEmptyViewText: LiveData<String>
        get() = _lblEmptyViewText

    // Eventos de comunicación con la actividad

    private val _onStartActivity: MutableLiveData<Event<Intent>> = MutableLiveData()
    val onStartActivity: LiveData<Event<Intent>>
        get() = _onStartActivity

    private val _onShowMessage: MutableLiveData<Event<String>> = MutableLiveData()
    val onShowMessage: LiveData<Event<String>>
        get() = _onShowMessage

    private val _onShowTaskDeleted: MutableLiveData<Event<Task>> = MutableLiveData()
    val onShowTaskDeleted: LiveData<Event<Task>>
        get() = _onShowTaskDeleted

    // ACTION METHODS

    // Hace que se muestre en el RecyclerView todas las tareas.
    fun filterAll(itemId: Int) {
        queryTasks(TasksActivityFilter.ALL)
        _currentFilterMenuItemId.value = itemId
    }

    // Hace que se muestre en el RecyclerView sólo las tareas completadas.
    fun filterCompleted(itemId: Int) {
       queryTasks(TasksActivityFilter.COMPLETED)
        _currentFilterMenuItemId.value = itemId
    }

    // Hace que se muestre en el RecyclerView sólo las tareas pendientes.
    fun filterPending(itemId: Int) {
        queryTasks(TasksActivityFilter.PENDING)
        _currentFilterMenuItemId.value = itemId
    }

    // Agrega una nueva tarea con dicho concepto. Si la se estaba mostrando
    // la lista de solo las tareas completadas, una vez agregada se debe
    // mostrar en el RecyclerView la lista con todas las tareas, no sólo
    // las completadas.
    fun addTask(concept: String) {
        // TODO
        repository.addTask(concept)
    }

    // Agrega la tarea
    fun insertTask(task: Task) {
        // TODO
        repository.insertTask(task)
    }

    // Borra la tarea
    fun deleteTask(task: Task) {
        // TODO
        repository.deleteTask(task.id)
    }

    // Borra todas las tareas mostradas actualmente en el RecyclerView.
    // Si no se estaba mostrando ninguna tarea, se muestra un mensaje
    // informativo en un SnackBar de que no hay tareas que borrar.
    fun deleteTasks() {
        val ids: List<Long> = tasks.value!!.map { it.id }
        if (ids.isNotEmpty()) {
            repository.deleteTasks(ids)
        } else {
            _onShowMessage.value = Event(application.getString(R.string.tasks_no_tasks_to_delete))

        }
    }

    // Marca como completadas todas las tareas mostradas actualmente en el RecyclerView,
    // incluso si ya estaban completadas.
    // Si no se estaba mostrando ninguna tarea, se muestra un mensaje
    // informativo en un SnackBar de que no hay tareas que marcar como completadas.
    fun markTasksAsCompleted() {
        val ids: List<Long> = tasks.value!!.filter { it.completed }.map { it.id }
        if (ids.isNotEmpty()) {
            repository.markTasksAsCompleted(ids)
        }
    }

    // Marca como pendientes todas las tareas mostradas actualmente en el RecyclerView,
    // incluso si ya estaban pendientes.
    // Si no se estaba mostrando ninguna tarea, se muestra un mensaje
    // informativo en un SnackBar de que no hay tareas que marcar como pendientes.
    fun markTasksAsPending() {
        val ids: List<Long> = tasks.value!!.filter { !it.completed }.map { it.id }
        if (ids.isNotEmpty()) {
            repository.markTasksAsCompleted(ids)
        }
    }

    // Hace que se envíe un Intent con la lista de tareas mostradas actualmente
    // en el RecyclerView.
    // Si no se estaba mostrando ninguna tarea, se muestra un Snackbar indicando
    // que no hay tareas que compartir.
    fun shareTasks() {
        if (tasks.value!!.isNotEmpty()) {
            var text = ""
            for (task in tasks.value!!) {
                text += task.toString()
            }
            val context = application.applicationContext
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, text)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        } else {
            // TODO Snackbar
        }
    }

    // Actualiza el estado de completitud de la tarea recibida, atendiendo al
    // valor de isCompleted. Si es true la tarea es marcada como completada y
    // en caso contrario es marcada como pendiente.
    fun updateTaskCompletedState(task: Task, isCompleted: Boolean) {
        if (!isCompleted){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                task.complete()
            }
        }
            task.completed = !isCompleted
    }

    // Retorna si el concepto recibido es válido (no es una cadena vacía o en blanco)
    fun isValidConcept(concept: String): Boolean {
        return concept.isNotEmpty() || concept.isNotBlank()
    }

    // Pide las tareas al repositorio, atendiendo al filtro recibido
    private fun queryTasks(filter: TasksActivityFilter) {
        _currentFilter.value = filter
        when (filter) {
            TasksActivityFilter.ALL ->  mutableTasks.value =  repository.queryAllTasks()
            TasksActivityFilter.PENDING ->  mutableTasks.value =  repository.queryPendingTasks()
            TasksActivityFilter.COMPLETED ->  mutableTasks.value = repository.queryCompletedTasks()
        }
    }

}

