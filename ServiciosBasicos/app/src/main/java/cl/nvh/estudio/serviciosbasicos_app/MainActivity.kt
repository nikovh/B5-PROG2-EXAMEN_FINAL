package cl.nvh.estudio.serviciosbasicos_app

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import cl.nvh.estudio.serviciosbasicos_app.ui.theme.ServiciosbasicosappTheme
import cl.nvh.estudio.serviciosbasicos_app.ui.theme.btnColor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar
import java.util.Locale


class MainActivity : ComponentActivity() {

    private val classVm by viewModels<claseVM>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ServiciosbasicosappTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppServicios(classVm)

                }
            }
        }
    }
}

@Composable
fun AppServicios(
    vm: claseVM = viewModel(),
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = "Listado"
    ) {
        composable("Listado") {
            ListadoUI(
                onClick = {
                    navController.navigate("Registro")
                }, vm
            )
        }
        composable("Registro") {
            FormRegistroUI(
                onButtonRegistrarClicked = {
                    navController.navigate("Listado")
                }, vm
            )
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun ListadoUI(
    onClick: () -> Unit = {},
    vm: claseVM = viewModel()
) {
    //variable de contexto
    val contexto = LocalContext.current
    val mainScope = MainScope()


    //se ejecuta 1 vez al iniciar el composable ListadoUI y actualiza la lista
    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            vm.obtenerListado(contexto)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 10.dp)
    ) {
        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            items(vm.itemList) { registro ->
                Column() {
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        if (registro.option == contexto.getString(R.string.serv_agua)) {
                            Icon(
                                painter = painterResource(id = R.drawable.agua),
                                contentDescription = "",
                                modifier = Modifier.width(25.dp)
                            )
                        }
                        if (registro.option == contexto.getString(R.string.serv_luz)) {
                            Icon(
                                painter = painterResource(id = R.drawable.luz),
                                contentDescription = "",
                                modifier = Modifier.width(25.dp)
                            )
                        }
                        if (registro.option == contexto.getString(R.string.serv_gas)) {
                            Icon(
                                painter = painterResource(id = R.drawable.gas),
                                contentDescription = "",
                                modifier = Modifier.width(25.dp)
                            )
                        }
                        Text(registro.option.uppercase())
                        Text(registro.medidor.toString())
                        Text(registro.fecha)
                        IconButton(
                            onClick = {
                                mainScope.launch { vm.borrarRegistro(contexto, registro) }
                            },
                            modifier = Modifier.size(20.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.trash),
                                contentDescription = "Borrar registros"
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    HorizontalDivider()
                }
                Spacer(modifier = Modifier.height(30.dp))
            }
        }
        Row(modifier = Modifier
            .align(alignment = Alignment.CenterHorizontally)
            .padding(vertical = 20.dp)
        ) {
            Button(
                onClick = { onClick() },
                colors = ButtonDefaults.buttonColors(containerColor = btnColor),
                modifier = Modifier
                    .width(160.dp)
                    .height(35.dp)
            ) {
                Text(text = contexto.getString(R.string.btn_Ingresar))
            }
        }
    }
}


//////////////////////////////////////////////////////////

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun FormRegistroUI(
    onButtonRegistrarClicked: () -> Unit = {},
    vm: claseVM = viewModel(),
) {

    //variables
    val mainScope = MainScope()
    val contexto = LocalContext.current
    var medidor by remember { mutableStateOf("") }
    var fecha by remember { mutableStateOf("") }

    val calendar = Calendar.getInstance()
    var datePickerDialogState by remember { mutableStateOf<DatePickerDialog?>(null) }


    val options = listOf(
        contexto.getString(R.string.serv_agua),
        contexto.getString(R.string.serv_luz),
        contexto.getString(R.string.serv_gas)
    )
    var selectedoption by remember { mutableStateOf("") }
    //

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(horizontal = 0.dp, vertical = 50.dp)
    ) {
        Text(
            text = contexto.getString(R.string.titulo_app),
            fontSize = 20.sp,
            fontWeight = FontWeight.ExtraBold
        )
        Spacer(modifier = Modifier.height(30.dp))
        TextField(
            value = medidor,
            onValueChange = { medidor = it },
            label = { Text(contexto.getString(R.string.input_medidor)) },
            singleLine = true,
            maxLines = 1
        )
        TextField(
            value = fecha,
            modifier = Modifier
                .clickable { datePickerDialogState?.show() },
            onValueChange = { fecha = it },
            label = { Text(text = contexto.getString(R.string.input_fecha)) },
            singleLine = true,
            maxLines = 1,
            enabled = false,
        )
        Spacer(modifier = Modifier.height(30.dp))
        Column(
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                contexto.getString(R.string.h3_Medidor),
                fontSize = 15.sp,
                textAlign = TextAlign.Start,
                modifier = Modifier.padding(horizontal = 60.dp)
            )
            options.forEach { opcion ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                        .selectable(
                            selected = selectedoption == opcion,
                            onClick = { selectedoption = opcion }
                        )
                        .padding(horizontal = 70.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = selectedoption == opcion,
                        onClick = { selectedoption = opcion }
                    )
                    Text(text = opcion.replaceFirstChar {
                        if (it.isLowerCase()) it.titlecase(
                            Locale.ROOT
                        ) else it.toString()
                    })
                }
            }
        }
        Spacer(modifier = Modifier.height(30.dp))
        Button(
            onClick = {
                onButtonRegistrarClicked(); mainScope.launch {
                vm.crearRegistro(medidor, fecha, selectedoption, contexto)
            }
            },
            colors = ButtonDefaults.buttonColors(containerColor = btnColor)
        ) {
            Text(contexto.getString(R.string.btn_Registrar))
        }
    }


    DisposableEffect(contexto) {
        datePickerDialogState = DatePickerDialog(
            contexto,
            { _, year, month, dayOfMonth ->
                fecha = "$dayOfMonth/${month + 1}/$year"
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        onDispose {
            datePickerDialogState?.dismiss()
        }
    }
}