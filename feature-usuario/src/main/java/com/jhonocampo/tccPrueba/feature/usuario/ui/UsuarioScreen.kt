/*
 * Copyright (C) 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jhonocampo.tccPrueba.feature.usuario.ui

import com.jhonocampo.tccPrueba.core.ui.MyApplicationTheme
import com.jhonocampo.tccPrueba.feature.usuario.ui.UsuarioUiState.Success
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.jhonocampo.tccPrueba.core.database.Usuario

@Composable
fun UsuarioScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    viewModel: UsuarioViewModel
) {



    val items by viewModel.uiState.collectAsStateWithLifecycle()
    if (items is Success) {
        UsuarioScreen(
            usuarios = (items as Success).data,
            onUpdateServer = {
                viewModel.actualizarUsuariosServer()
            },
            onEdit = { usuario ->
                viewModel.editUsuarioState(usuario)
                navController.navigate("form")
            },
            onAdd = {
                viewModel.editUsuarioState(null)
                navController.navigate("form")
            },
            onOnceUpdate = {
                viewModel.actualizarUsuariosServer()
            },
            modifier = modifier,
            nav = navController
        )
    } else {
        UsuarioScreen(
            usuarios = null,
            onUpdateServer = {viewModel.actualizarUsuariosServer()},
            onEdit = { /*name -> viewModel.addUsuario(name)*/ },
            onAdd = { navController.navigate("form") },
            onOnceUpdate = {
                viewModel.actualizarUsuariosServer()
            },
            modifier = modifier,
            nav = navController
        )
    }

}

@Composable
internal fun UsuarioScreen(
    usuarios: List<Usuario>?,
    onUpdateServer: () -> Unit,
    onEdit: (usuario: Usuario) -> Unit,
    onAdd: () -> Unit,
    onOnceUpdate: () -> Unit,
    modifier: Modifier = Modifier,
    nav: NavHostController
) {
    var hasExecuted by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(key1 = hasExecuted) {
        // Execute the side effect
        onOnceUpdate()
        hasExecuted = true
    }
    Scaffold(
        topBar = {
            topAppBarTemplate(nav, onUpdateServer)
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {onAdd()}) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Column() {
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 128.dp),
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    if (usuarios != null){
                        items(usuarios) {
                            CardUsuario(usuario = it, onEdit = onEdit)
                        }
                    }
                }

            }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun topAppBarTemplate(nav: NavController, onUpdateServer: () -> Unit, back: Boolean = false) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        title = {
            Text("TCC Prueba técnica")
        },
        navigationIcon = {
            if (back){
                IconButton(onClick = { nav.popBackStack() }) {
                    Icon(
                        imageVector = Icons.Filled.KeyboardArrowLeft,
                        contentDescription = "Atras"
                    )
                }
            }
        },
        actions = {
            if (!back) {
                IconButton(onClick =  {onUpdateServer()} ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Refresh"
                    )
                }
            }
        }

    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun CardUsuario(usuario: Usuario, onEdit: (usuario: Usuario) -> Unit) {
    Card(
        modifier = Modifier
            .width(180.dp)
            .padding(8.dp),
        onClick = {
                    onEdit(usuario)
                  },
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text(
                text = getTipoIdentificacion(usuario.tipoIdentificacion),
                fontSize = MaterialTheme.typography.titleSmall.fontSize,
                fontWeight = MaterialTheme.typography.titleSmall.fontWeight,
                softWrap = false,
            )
            Text(
                fontSize = MaterialTheme.typography.titleMedium.fontSize,
                fontWeight = MaterialTheme.typography.titleLarge.fontWeight,
                text = usuario.identificacion,
                softWrap = false,
            )
            Text(text = usuario.nombre, softWrap = false,)
            Text(text = getGenero(usuario.genero))
        }
    }
}

fun getTipoIdentificacion(identificacion: String): String {
    return when (identificacion) {
        "CC" -> "Cédula de ciudadanía"
        "CE" -> "Cédula de extranjería"
        "PA" -> "Pasaporte"
        "TI" -> "Tarjeta de identidad"
        "RC" -> "Registro civil"
        else -> "Otro"
    }
}

fun getGenero(genero: String): String {
    return when (genero) {
        "M" -> "Masculino"
        "F" -> "Femenino"
        else -> "Otro"
    }
}


// Previews

@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    MyApplicationTheme {
        val nav = rememberNavController()
        UsuarioScreen(listOf(
            Usuario(
                tipoIdentificacion = "CC",
                identificacion = "123456789",
                nombre = "Jhon",
                genero = "M"
            ),
            Usuario(
                tipoIdentificacion = "CC",
                identificacion = "987654321",
                nombre = "Camilo",
                genero = "F"
            ),
            Usuario(
                tipoIdentificacion = "CC",
                identificacion = "123456789",
                nombre = "Jhon",
                genero = "M"
            ),
        ), onOnceUpdate={}, onUpdateServer = {}, onEdit = {}, onAdd = {}, nav = nav)
    }
}

@Preview(showBackground = false, widthDp = 480)
@Composable
private fun PortraitPreview() {
    MyApplicationTheme {
        val nav = rememberNavController()
        UsuarioScreen(listOf(
            Usuario(
                tipoIdentificacion = "CC",
                identificacion = "123456789",
                nombre = "Jhon",
                genero = "Masculino"
            ),
            Usuario(
                tipoIdentificacion = "CC",
                identificacion = "987654321",
                nombre = "Camilo",
                genero = "Masculino"
            ),
            Usuario(
                tipoIdentificacion = "CC",
                identificacion = "123456789",
                nombre = "Jhon",
                genero = "Masculino"
            ),
        ), onOnceUpdate={}, onUpdateServer = {}, onEdit = {}, onAdd = {}, nav = nav)
    }
}
