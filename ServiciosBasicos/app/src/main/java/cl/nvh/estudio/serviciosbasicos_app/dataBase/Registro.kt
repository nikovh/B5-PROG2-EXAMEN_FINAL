package cl.nvh.estudio.serviciosbasicos_app.dataBase

import androidx.room.Entity
import androidx.room.PrimaryKey

//creacion de la tabla
@Entity
data class Registro(
    @PrimaryKey(autoGenerate = true) val id:Int =0,
    var medidor:Int,
    var fecha: String,
    var option:String
)
