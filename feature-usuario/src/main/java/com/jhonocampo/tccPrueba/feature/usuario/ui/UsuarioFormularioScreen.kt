package com.jhonocampo.tccPrueba.feature.usuario.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.jhonocampo.tccPrueba.core.database.Usuario

@Composable
fun UsuarioFormularioScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    viewModel: UsuarioViewModel
) {
    UsuarioFormularioScreen(
        usuario= viewModel.editUsuario.collectAsState().value,
        onSave = { usuario -> viewModel.guardarUsuario(usuario) },
        onDelete = { usuario ->
                        viewModel.eliminarUsuario(usuario)
                        navController.popBackStack()
                   },
        navController = navController,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun UsuarioFormularioScreen(
    usuario: Usuario?,
    onSave: (usuario: Usuario) -> Unit,
    onDelete: (usuario: Usuario) -> Unit = {},
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val state = rememberScrollState()

    var tipoIdentificacion by rememberSaveable { mutableStateOf("") }
    var identificacion by rememberSaveable { mutableStateOf("") }
    var nombre by rememberSaveable { mutableStateOf("") }
    var genero by rememberSaveable { mutableStateOf("") }

    if (usuario != null) {
        tipoIdentificacion = usuario.tipoIdentificacion
        identificacion = usuario.identificacion
        nombre = usuario.nombre
        genero = usuario.genero
    } else {
        tipoIdentificacion = ""
        identificacion = ""
        nombre = ""
        genero = ""
    }

    val openDialog = remember { mutableStateOf(false) }
    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = {
                // Dismiss the dialog when the user clicks outside the dialog or on the back
                // button. If you want to disable that functionality, simply use an empty
                // onDismissRequest.
                openDialog.value = false
            }
        ) {
            Surface(
                modifier = Modifier
                    .wrapContentWidth()
                    .wrapContentHeight(),
                shape = MaterialTheme.shapes.large,
                tonalElevation = AlertDialogDefaults.TonalElevation
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "¿Está seguro de eliminar este usuario?",
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        TextButton(
                            onClick = {
                                openDialog.value = false
                            },
                        ) {
                            Text("Cancelar")
                        }

                        TextButton(
                            colors = ButtonDefaults.textButtonColors(
                                contentColor = MaterialTheme.colorScheme.error
                            ),
                            onClick = {
                                openDialog.value = false
                                onDelete(usuario!!)
                            },
                        ) {
                            Text("Confirmar")
                        }
                    }
                }
            }
        }
    }

    Scaffold(
        topBar = {
            topAppBarTemplate(navController,  {}, true)
        },

    ) { innerPadding ->
        Column(
            modifier = modifier.padding(innerPadding).verticalScroll(state),
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(text = "Tipo documento")
                Column(Modifier.selectableGroup()) {
                    RadioButtonSelect(
                        label = "Cédula ciudadanía",
                        value = "CC",
                        state = tipoIdentificacion,
                        onChange = { tipoIdentificacion = it }
                    )
                    RadioButtonSelect(
                        label = "Cédula extranjería",
                        value = "CE",
                        state = tipoIdentificacion,
                        onChange = { tipoIdentificacion = it }
                    )
                    RadioButtonSelect(
                        label = "Pasaporte",
                        value = "PA",
                        state = tipoIdentificacion,
                        onChange = { tipoIdentificacion = it }
                    )
                    RadioButtonSelect(
                        label = "Tarjeta identidad",
                        value = "TI",
                        state = tipoIdentificacion,
                        onChange = { tipoIdentificacion = it }
                    )
                    RadioButtonSelect(
                        label = "Registro civil",
                        value = "RC",
                        state = tipoIdentificacion,
                        onChange = { tipoIdentificacion = it }
                    )
                }
                TextField(
                    value = identificacion,
                    onValueChange = { identificacion = it },
                    label = { Text("Identificación") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                TextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Text(text = "Género")

                Column(Modifier.selectableGroup()) {
                    RadioButtonSelect(
                        label = "Masculino",
                        value = "M",
                        state = genero,
                        onChange = { genero = it }
                    )
                    RadioButtonSelect(
                        label = "Femenino",
                        value = "F",
                        state = genero,
                        onChange = { genero = it }
                    )
                    RadioButtonSelect(
                        label = "Otro",
                        value = "O",
                        state = genero,
                        onChange = { genero = it }
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Button(onClick = {
                        val usuarioGuardar = Usuario(
                            id = usuario?.id,
                            tipoIdentificacion = tipoIdentificacion,
                            identificacion = identificacion,
                            nombre = nombre,
                            genero = genero,
                        )
                        onSave(usuarioGuardar)
                        navController.popBackStack()
                    }
                    ) { Text("Guardar") }
                    if (usuario != null){
                        Button(
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.errorContainer,
                                contentColor = MaterialTheme.colorScheme.error
                            ),
                            onClick = {
                                openDialog.value = true
                            }
                        ) { Text("Eliminar") }
                    }
                }
            }
        }
    }


}

@Composable
internal fun RadioButtonSelect(
    label: String,
    value: String,
    state: String,
    onChange: (String) -> Unit
){
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.primary,
                shape = RectangleShape
            )
            .padding(8.dp)

    ) {
        RadioButton(
            selected = state == value,
            onClick = { onChange(value) },
            modifier = Modifier.semantics { contentDescription = label }
        )
        Text(label)
    }
}

@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    val navController = rememberNavController()
    UsuarioFormularioScreen(
        usuario = null,
        onSave = { /*TODO*/ },
        navController = navController
    )
}
