/*
 * Designed and developed by 2020 zeynelerdi (zeynel erdi)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.zeynelerdi.pokedex.di

import com.zeynelerdi.pokedex.network.PokedexClient
import com.zeynelerdi.pokedex.persistence.PokemonDao
import com.zeynelerdi.pokedex.persistence.PokemonInfoDao
import com.zeynelerdi.pokedex.repository.DetailRepository
import com.zeynelerdi.pokedex.repository.MainRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped

@Module
@InstallIn(ActivityRetainedComponent::class)
object RepositoryModule {

  @Provides
  @ActivityRetainedScoped
  fun provideMainRepository(
    pokedexClient: PokedexClient,
    pokemonDao: PokemonDao
  ): MainRepository {
    return MainRepository(pokedexClient, pokemonDao)
  }

  @Provides
  @ActivityRetainedScoped
  fun provideDetailRepository(
    pokedexClient: PokedexClient,
    pokemonInfoDao: PokemonInfoDao
  ): DetailRepository {
    return DetailRepository(pokedexClient, pokemonInfoDao)
  }
}
