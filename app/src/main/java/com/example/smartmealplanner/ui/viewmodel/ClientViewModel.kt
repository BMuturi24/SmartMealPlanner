package com.example.smartmealplanner.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartmealplanner.data.model.ClientProfileEntity
import com.example.smartmealplanner.data.model.User
import com.example.smartmealplanner.data.repository.MealRepository
import kotlinx.coroutines.launch

class ClientViewModel(private val repo: MealRepository) : ViewModel() {

    // LiveData to hold the list of clients and their profiles
    private val _clients = MutableLiveData<List<User>>(emptyList())
    val clients: LiveData<List<User>> = _clients

    private val _profiles = MutableLiveData<List<ClientProfileEntity>>(emptyList())
    val profiles: LiveData<List<ClientProfileEntity>> = _profiles

    private val _message = MutableLiveData<String?>()
    val message: LiveData<String?> = _message

    fun refresh() {
        viewModelScope.launch {
            _clients.value = repo.allClients()
            _profiles.value = repo.allProfiles()
        }
    }

    fun addClient(username: String, email: String, password: String) {
        viewModelScope.launch {
            try {
                repo.adminAddClient(username, email, password)
                _message.value = "Client '$username' added"
                refresh()
            } catch (e: Exception) {
                _message.value = "Could not add: ${e.message}"
            }
        }
    }

    fun deleteClient(username: String) {
        viewModelScope.launch {
            val target = repo.allClients().firstOrNull { it.username == username }
            if (target == null) {
                _message.value = "No client named '$username'"
                return@launch
            }
            repo.adminDeleteClient(target.id)
            _message.value = "Deleted '$username'"
            refresh()
        }
    }

    fun consumeMessage() { _message.value = null }
}