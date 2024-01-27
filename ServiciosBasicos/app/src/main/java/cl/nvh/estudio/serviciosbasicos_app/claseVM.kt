package cl.nvh.estudio.serviciosbasicos_app

import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import cl.nvh.estudio.serviciosbasicos_app.dataBase.BaseDatos
import cl.nvh.estudio.serviciosbasicos_app.dataBase.Registro

class claseVM : ViewModel() {

    var itemList = mutableStateListOf<Registro>()

    suspend fun crearRegistro(
        medida: String,
        fecha: String,
        opcion: String,
        contexto: Context
    ) {
        if (!medida.isNullOrBlank()) {
            var reg = Registro(medidor = medida.toInt(), fecha = fecha, option = opcion)
            BaseDatos.getInstance(contexto).RegistroDao().insert(reg)
        }
    }

    suspend fun obtenerListado(contexto: Context) {
        itemList.clear()
        //BaseDatos.getInstance(contexto).RegistroDao().delete()
        itemList.addAll(BaseDatos.getInstance(contexto).RegistroDao().obtenerTodos())
    }

    suspend fun borrarRegistro(contexto:Context, registro: Registro) {
        BaseDatos.getInstance(contexto).RegistroDao().borrarUnRegistro(registro)
        obtenerListado(contexto)
        }
}
