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

package com.zeynelerdi.pokedex.ui.details

import androidx.databinding.ObservableBoolean
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import com.zeynelerdi.pokedex.base.LiveCoroutinesViewModel
import com.zeynelerdi.pokedex.model.PokemonInfo
import com.zeynelerdi.pokedex.repository.DetailRepository
import timber.log.Timber

class DetailViewModel @ViewModelInject constructor(
  private val detailRepository: DetailRepository
) : LiveCoroutinesViewModel() {

  private var pokemonFetchingLiveData: MutableLiveData<String> = MutableLiveData()
  val pokemonInfoLiveData: LiveData<PokemonInfo>

  val isLoading: ObservableBoolean = ObservableBoolean(false)
  val toastLiveData: MutableLiveData<String> = MutableLiveData()

  init {
    Timber.d("init DetailViewModel")

    pokemonInfoLiveData = pokemonFetchingLiveData.switchMap {
      isLoading.set(true)
      launchOnViewModelScope {
        this.detailRepository.fetchPokemonInfo(
          name = it,
          onSuccess = { isLoading.set(false) },
          onError = { toastLiveData.postValue(it) })
      }
    }
  }

  fun fetchPokemonInfo(name: String) {
    pokemonFetchingLiveData.value = name
  }
}
