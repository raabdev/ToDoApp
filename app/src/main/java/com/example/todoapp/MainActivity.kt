package com.example.todoapp

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), TareaAdapter.TareaAdapterListener {

    val AGREGAR_TAREA_CODE = 1

    lateinit var tareaAdapter: TareaAdapter
    lateinit var mainLayout : ConstraintLayout
    lateinit var tareaDao: TareaDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tareaDao = AppDatabase.getInstance(this).tareaDao()

        mainLayout = activity_content
        val tareas = ArrayList<Tarea>(tareaDao.getTareas())
        tareaAdapter = TareaAdapter(tareas, this)
        recycler_view.adapter = tareaAdapter
        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))

        agregar_tarea.setOnClickListener {
            startActivityForResult(Intent(this, AgregarTareaActivity::class.java), AGREGAR_TAREA_CODE)
        }

        val itemTouchHelperCallback = object : ItemTouchHelper.Callback() {
            override fun getMovementFlags(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ): Int {
                return makeMovementFlags(ItemTouchHelper.UP.or(ItemTouchHelper.DOWN), ItemTouchHelper.RIGHT)
            }

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                val posicionInicial = viewHolder.adapterPosition
                val posicionFinal = target.adapterPosition
                tareaAdapter.cambiarPosicionItem(posicionInicial, posicionFinal)
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val posicion = viewHolder.adapterPosition
                val tarea = tareaAdapter.getTarea(posicion)
                tareaDao.eliminarTarea(tarea)
                tareaAdapter.eliminarTarea(posicion)
                val snackbar = Snackbar.make(mainLayout, "Eliminaste una tarea", Snackbar.LENGTH_LONG)
                snackbar.setAction("Deshacer") {
                    tareaDao.insertarTarea(tarea)
                    tareaAdapter.restaurarTarea(posicion, tarea)
                }
                snackbar.setActionTextColor(Color.YELLOW)
                snackbar.show()
            }
        }

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(recycler_view)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == AGREGAR_TAREA_CODE) {
            if(resultCode == Activity.RESULT_OK) {
                val tareaDescripcion = data!!.extras!!["tarea"] as String
                val tareaCreada = Tarea(tareaDescripcion, false)
                tareaDao.insertarTarea(tareaCreada)
                tareaAdapter.agregarTarea(tareaCreada)
            }
        }
    }

    override fun onTareaChecked(tarea: Tarea, terminada: Boolean) {
        tarea.terminada = terminada
        tareaDao.actualizarTarea(tarea)
    }
}