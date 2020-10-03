package com.example.todoapp

import androidx.room.*

@Dao
interface TareaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertarTarea(tarea: Tarea)

    @Update
    fun actualizarTarea(tarea: Tarea)

    @Delete
    fun eliminarTarea(tarea: Tarea)

    @Query("SELECT * FROM tarea")
    fun getTareas(): List<Tarea>
}