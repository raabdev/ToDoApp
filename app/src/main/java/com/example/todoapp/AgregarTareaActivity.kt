package com.example.todoapp

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_agregar_tarea.*
import kotlinx.android.synthetic.main.activity_main.*

class AgregarTareaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_tarea)

        crear_tarea.setOnClickListener {
            val tareaDescripcion = tarea.text.toString()
            if(tareaDescripcion.isEmpty())
                Toast.makeText(this, "La tarea no puede estar vacía", Toast.LENGTH_LONG).show()
            else {
                val intent = Intent()
                intent.putExtra("tarea", tareaDescripcion)
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
        }
    }
}