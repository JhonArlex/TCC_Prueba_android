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

package com.jhonocampo.tccPrueba.core.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import com.jhonocampo.tccPrueba.core.data.UsuarioRepository
import com.jhonocampo.tccPrueba.core.data.DefaultUsuarioRepository
import com.jhonocampo.tccPrueba.core.database.Usuario
import javax.inject.Inject
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Singleton
    @Binds
    fun bindsUsuarioRepository(
        usuarioRepository: DefaultUsuarioRepository
    ): UsuarioRepository
}

class FakeUsuarioRepository @Inject constructor() : UsuarioRepository {
    override val usuarios: Flow<List<Usuario>> = flowOf(fakeUsuarios)
    override suspend fun add(usuario: Usuario) {
        throw NotImplementedError()
    }

    override suspend fun update(usuario: Usuario) {
        throw NotImplementedError()
    }

    override suspend fun delete(usuario: Usuario) {
        throw NotImplementedError()
    }
}

val fakeUsuarios = listOf(
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
)
