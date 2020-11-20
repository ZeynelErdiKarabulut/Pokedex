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

package com.zeynelerdi.pokedex.network

import com.zeynelerdi.pokedex.model.PokemonInfo
import com.zeynelerdi.pokedex.model.PokemonResponse
import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.toResponseDataSource
import javax.inject.Inject

class PokedexClient @Inject constructor(
  private val pokedexService: PokedexService
) {

  fun fetchPokemonList(
    page: Int,
    onResult: (response: ApiResponse<PokemonResponse>) -> Unit
  ) {
    pokedexService.fetchPokemonList(
      limit = PAGING_SIZE,
      offset = page * PAGING_SIZE
    ).toResponseDataSource()
      // retry fetching data 3 times with 7000L interval when the request gets failure.
      .retry(RETRY_COUNT, RETRY_DELAY)
      // request API network call asynchronously.
      .request(onResult)
  }

  fun fetchPokemonInfo(
    name: String,
    onResult: (response: ApiResponse<PokemonInfo>) -> Unit
  ) {
    pokedexService.fetchPokemonInfo(
      name = name
    ).toResponseDataSource()
      // retry fetching data 3 times with 7000L interval when the request gets failure.
      .retry(RETRY_COUNT, RETRY_DELAY)
      // request API network call asynchronously.
      .request(onResult)
  }

  companion object {
    private const val PAGING_SIZE = 20
    private const val RETRY_COUNT = 3
    private const val RETRY_DELAY = 7000L
  }
}
