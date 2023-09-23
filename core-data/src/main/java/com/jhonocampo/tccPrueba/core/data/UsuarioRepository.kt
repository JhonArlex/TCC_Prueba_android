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

package com.jhonocampo.tccPrueba.core.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import com.jhonocampo.tccPrueba.core.database.Usuario
import com.jhonocampo.tccPrueba.core.database.UsuarioDao
import javax.inject.Inject

interface UsuarioRepository {
    val usuarios: Flow<List<Usuario>>

    suspend fun add(usuario: Usuario)
    suspend fun update(usuario: Usuario)
    suspend fun delete(usuario: Usuario)
}

class DefaultUsuarioRepository @Inject constructor(
    private val usuarioDao: UsuarioDao
) : UsuarioRepository {

    override val usuarios: Flow<List<Usuario>> =
        usuarioDao.getUsuarios()

    override suspend fun add(usuario: Usuario) {
        usuarioDao.insertUsuario(usuario)
    }

    override suspend fun update(usuario: Usuario) {
        usuarioDao.updateUsuario(usuario)
    }

    override suspend fun delete(usuario: Usuario) {
        usuarioDao.deleteUsuario(usuario)
    }
}
