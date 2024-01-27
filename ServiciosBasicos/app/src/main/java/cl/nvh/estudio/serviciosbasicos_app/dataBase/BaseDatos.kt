package cl.nvh.estudio.serviciosbasicos_app.dataBase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Registro::class], version = 1)
@TypeConverters(LocalDateConverter::class)
abstract class BaseDatos : RoomDatabase(){
    abstract fun RegistroDao(): RegistroDao

    companion object {
        @Volatile
        private var BASE_DATOS : BaseDatos? = null
        const val BD_NOMBRE = "serviciosbasicos.db"

        fun getInstance (contexto : Context) : BaseDatos {
            return BASE_DATOS ?: synchronized(this) {
                Room.databaseBuilder(
                    contexto.applicationContext,
                    BaseDatos::class.java,
                    BD_NOMBRE
                ).fallbackToDestructiveMigration().build().also { BASE_DATOS = it }
            }
        }
    }
}