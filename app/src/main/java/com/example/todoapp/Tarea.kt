package com.example.todoapp

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "tarea")
data class Tarea(
    @PrimaryKey val nombre : String,
    var terminada : Boolean)