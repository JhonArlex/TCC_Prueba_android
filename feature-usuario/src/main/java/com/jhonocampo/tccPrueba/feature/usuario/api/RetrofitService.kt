package com.jhonocampo.tccPrueba.feature.usuario.api

import com.jhonocampo.tccPrueba.core.database.Usuario
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitService {
    private val retrofit = Retrofit.Builder()
        .baseUrl("http://192.168.1.11:8080/api/v1/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val usuarioService: UsuarioService = retrofit.create(UsuarioService::class.java)

    fun getUsuarios() = usuarioService.getUsuarios()
    fun crearUsuario(usuario: UsuarioDto) = usuarioService.crearUsuario(usuario)
    fun actualizarUsuario(usuario: UsuarioDto) = usuarioService.actualizarUsuario(usuario)
    fun eliminarUsuario(id: String) = usuarioService.eliminarUsuario(id)
}