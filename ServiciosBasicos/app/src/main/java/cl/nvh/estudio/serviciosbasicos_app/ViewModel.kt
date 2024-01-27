package cl.nvh.estudio.serviciosbasicos_app

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cl.nvh.estudio.serviciosbasicos_app.dataBase.Registro
import java.util.Date

class viewModel: ViewModel() {
    fun onLoginChanged(it: String, fecha: Date) {

    }

    //pantalla FormRegistroUI
    private val _medidor = MutableLiveData<String>()
    val medidor: LiveData<String> = _medidor

    private val _fecha = MutableLiveData<Date>()
    val fecha: LiveData<Date> = _fecha


}