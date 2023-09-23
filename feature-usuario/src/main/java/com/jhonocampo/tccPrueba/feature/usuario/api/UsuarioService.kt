package com.jhonocampo.tccPrueba.feature.usuario.api

import com.jhonocampo.tccPrueba.core.database.Usuario
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface UsuarioService {
    @GET("usuario/consultar-todos")
    fun getUsuarios(): Call<List<UsuarioDto>>

    @POST("usuario/crear")
    fun crearUsuario(@Body usuario: UsuarioDto): Call<UsuarioDto>

    @PUT("usuario/actualizar")
    fun actualizarUsuario(@Body usuario: UsuarioDto): Call<UsuarioDto>

    @DELETE("usuario/eliminar/{id}")
    fun eliminarUsuario(@Path("id") id: String): Call<UsuarioDto>
}