package es.iessaladillo.pedrojoya.pr04.data.entity

// TODO: Crea una clase llamada Task con las siguientes propiedades:
//  id (Long), concept(String), createdAt (String),
//  completed (Boolean), completedAt (String)

data class Task(val id: Long, val concept: String, val createdAt: String, var completed: Boolean, val completedAt: String) {

}
