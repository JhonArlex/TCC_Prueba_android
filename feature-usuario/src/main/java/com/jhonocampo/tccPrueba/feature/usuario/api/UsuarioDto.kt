package com.jhonocampo.tccPrueba.feature.usuario.api

import com.google.gson.InstanceCreator
import com.jhonocampo.tccPrueba.core.database.Usuario
import java.lang.reflect.Type

data class UsuarioDto(
    val id: Int?,
    val tipoIdentificacion: String,
    val identificacion: String,
    val nombre: String,
    val genero: String
) {
    constructor() : this(null, "", "", "", "")

    fun toUsuario() = Usuario(
        id = id,
        tipoIdentificacion = tipoIdentificacion,
        identificacion = identificacion,
        nombre = nombre,
        genero = genero
    )
}

class UsuarioInstanceCreator : InstanceCreator<UsuarioDto> {
    override fun createInstance(type: Type): UsuarioDto {
        return UsuarioDto()
    }
}
