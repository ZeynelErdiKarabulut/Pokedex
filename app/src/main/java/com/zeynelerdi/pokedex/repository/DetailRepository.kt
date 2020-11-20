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

package com.zeynelerdi.pokedex.repository

import androidx.lifecycle.MutableLiveData
import com.zeynelerdi.pokedex.model.PokemonInfo
import com.zeynelerdi.pokedex.network.PokedexClient
import com.zeynelerdi.pokedex.persistence.PokemonInfoDao
import com.skydoves.sandwich.message
import com.skydoves.sandwich.onError
import com.skydoves.sandwich.onException
import com.skydoves.sandwich.onSuccess
import com.skydoves.whatif.whatIfNotNull
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DetailRepository @Inject constructor(
  private val pokedexClient: PokedexClient,
  private val pokemonInfoDao: PokemonInfoDao
) : Repository {

  suspend fun fetchPokemonInfo(
    name: String,
    onSuccess: () -> Unit,
    onError: (String) -> Unit
  ) = withContext(Dispatchers.IO) {
    val liveData = MutableLiveData<PokemonInfo>()
    val pokemonInfo = pokemonInfoDao.getPokemonInfo(name)
    if (pokemonInfo == null) {
      pokedexClient.fetchPokemonInfo(name = name) {
        it.onSuccess {
          data.whatIfNotNull { response ->
            liveData.postValue(response)
            pokemonInfoDao.insertPokemonInfo(response)
            onSuccess()
          }
        }
          // handle the case when the API request gets a error response.
          // e.g. internal server error.
          .onError {
            onError(message())
          }
          // handle the case when the API request gets a exception response.
          // e.g. network connection error.
          .onException {
            onError(message())
          }
      }
    } else {
      onSuccess()
    }
    liveData.apply { postValue(pokemonInfo) }
  }
}
