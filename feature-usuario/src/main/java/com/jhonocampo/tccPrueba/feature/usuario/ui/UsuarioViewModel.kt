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

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import com.jhonocampo.tccPrueba.core.data.UsuarioRepository
import com.jhonocampo.tccPrueba.core.database.Usuario
import com.jhonocampo.tccPrueba.feature.usuario.api.RetrofitService
import com.jhonocampo.tccPrueba.feature.usuario.api.UsuarioDto
import com.jhonocampo.tccPrueba.feature.usuario.ui.UsuarioUiState.Error
import com.jhonocampo.tccPrueba.feature.usuario.ui.UsuarioUiState.Loading
import com.jhonocampo.tccPrueba.feature.usuario.ui.UsuarioUiState.Success
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class UsuarioViewModel @Inject constructor(
    private val usuarioRepository: UsuarioRepository
) : ViewModel() {

    private var _editUsuario: MutableStateFlow<Usuario?> = MutableStateFlow(null)
    val editUsuario = _editUsuario.asStateFlow()
    val retrofitUserService: RetrofitService = RetrofitService()

    fun editUsuarioState(usuario: Usuario?) {
        _editUsuario.update { usuario }
    }

    val uiState: StateFlow<UsuarioUiState> = usuarioRepository
        .usuarios.map<List<Usuario>, UsuarioUiState> { Success(data = it) }
        .catch { emit(Error(it)) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), Loading)

    fun actualizarUsuariosServer() {
        val usuarios = retrofitUserService.getUsuarios()
        val localUsuarios = usuarioRepository.usuarios

        usuarios.enqueue(object : Callback<List<UsuarioDto>> {
            override fun onResponse(call: Call<List<UsuarioDto>>, response: Response<List<UsuarioDto>>) {
                if (response.isSuccessful) {
                    var usuariosBody = response.body()!!
                    viewModelScope.launch {
                        localUsuarios.collect {
                            if (usuariosBody.isNotEmpty()) {
                                if (it.size > 0) {
                                    val users = usuariosBody.filter { usuario ->
                                        it.find { localUsuario ->
                                            localUsuario.id == usuario.id
                                        } == null
                                    }
                                    for (usuario in users) {
                                        usuarioRepository.add(usuario.toUsuario())
                                    }
                                } else {
                                    for (usuario in usuariosBody) {
                                        usuarioRepository.add(usuario.toUsuario())
                                    }
                                }
                                usuariosBody = emptyList()
                            }
                        }
                    }
                } else {
                    // La solicitud no fue exitosa
                }
            }

            override fun onFailure(call: Call<List<UsuarioDto>>, t: Throwable) {
                // Se produjo un error
            }
        })
    }


    fun guardarUsuario(usuario: Usuario) {
        if (usuario.id != null) {
            viewModelScope.launch {
                val updateUsuario = retrofitUserService.actualizarUsuario(
                    UsuarioDto(
                        id = usuario.id,
                        tipoIdentificacion = usuario.tipoIdentificacion,
                        identificacion = usuario.identificacion,
                        nombre = usuario.nombre,
                        genero = usuario.genero
                    )
                )
                updateUsuario.enqueue(object : Callback<UsuarioDto> {
                    override fun onResponse(call: Call<UsuarioDto>, response: Response<UsuarioDto>) {
                        if (response.isSuccessful) {
                            val usuarioBody = response.body()!!
                            viewModelScope.launch {
                                usuarioRepository.update(usuario)
                            }
                        } else {
                            // La solicitud no fue exitosa
                        }
                    }

                    override fun onFailure(call: Call<UsuarioDto>, t: Throwable) {
                        // Se produjo un error
                    }
                })

            }
        } else {
            viewModelScope.launch {
                val addUsuario = retrofitUserService.crearUsuario(UsuarioDto(
                    null,
                    tipoIdentificacion = usuario.tipoIdentificacion,
                    identificacion = usuario.identificacion,
                    nombre = usuario.nombre,
                    genero = usuario.genero
                ))
                addUsuario.enqueue(object : Callback<UsuarioDto> {
                    override fun onResponse(call: Call<UsuarioDto>, response: Response<UsuarioDto>) {
                        if (response.isSuccessful) {
                            val usuarioBody = response.body()!!
                            viewModelScope.launch {
                                usuarioRepository.add(usuarioBody.toUsuario())
                            }
                        } else {
                            // La solicitud no fue exitosa
                        }
                    }

                    override fun onFailure(call: Call<UsuarioDto>, t: Throwable) {
                        // Se produjo un error
                    }
                })
            }
        }
    }

    fun eliminarUsuario(usuario: Usuario) {
        viewModelScope.launch {
            val deleteUsuario = retrofitUserService.eliminarUsuario(usuario.id.toString())
            deleteUsuario.enqueue(object : Callback<UsuarioDto> {
                override fun onResponse(call: Call<UsuarioDto>, response: Response<UsuarioDto>) {
                    if (response.isSuccessful) {
                        val usuarioBody = response.body()!!
                        viewModelScope.launch {
                            usuarioRepository.delete(usuario)
                        }
                    } else {
                        // La solicitud no fue exitosa
                    }
                }

                override fun onFailure(call: Call<UsuarioDto>, t: Throwable) {
                    // Se produjo un error
                }
            })
        }
    }
}

sealed interface UsuarioUiState {
    object Loading : UsuarioUiState
    data class Error(val throwable: Throwable) : UsuarioUiState
    data class Success(val data: List<Usuario>) : UsuarioUiState
}
