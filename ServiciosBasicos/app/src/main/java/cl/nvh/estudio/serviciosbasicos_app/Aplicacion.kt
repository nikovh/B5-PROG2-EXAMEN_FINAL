package cl.nvh.estudio.serviciosbasicos_app

import android.app.Application
import androidx.room.Room
import cl.nvh.estudio.serviciosbasicos_app.dataBase.BaseDatos

class Aplicacion : Application() {

    val db by lazy { Room.databaseBuilder(this, BaseDatos::class.java, "serviciosbasicos.db").build() }
    val registroDao by lazy { db.RegistroDao() }
}