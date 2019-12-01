package es.iessaladillo.pedrojoya.pr04.data.entity

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class Task(
    val id: Long,
    val concept: String,
    val createdAt: String,
    var completed: Boolean,
    var completedAt: String
) {

    override fun toString(): String {
        return if (completed) {
            String.format("-%s: Completed at %s\n", concept, completedAt)
        } else {
            String.format("-%s: Created at %s\n", concept, createdAt)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun complete() {
        val format: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")
        completedAt = LocalDateTime.now().format(format)
    }

}
