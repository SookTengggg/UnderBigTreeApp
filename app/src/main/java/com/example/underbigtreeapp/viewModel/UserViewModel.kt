package com.example.underbigtreeapp.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.underbigtreeapp.model.User
import com.example.underbigtreeapp.repository.UserRepository
import kotlinx.coroutines.launch

class UserViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = UserRepository.getInstance(application)

    private val _user = MutableLiveData<User?>(null)
    val user: LiveData<User?> = _user

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<String?>(null)
    val error: LiveData<String?> = _error

    fun signup(email: String, password: String, name: String, phone: String) {
        _loading.value = true
        _error.value = null
        viewModelScope.launch {
            try {
                val result = repo.registerUser(email, password, name, phone)
                if (result == null) _error.value = "Signup failed"
                _user.value = result
            } catch (e: Exception) {
                _error.value = e.message ?: "Signup error"
            } finally {
                _loading.value = false
            }
        }
    }

    fun login(email: String, password: String) {
        _loading.value = true
        _error.value = null
        viewModelScope.launch {
            try {
                val result = repo.loginUser(email, password)
                if (result == null) _error.value = "Invalid email or password"
                _user.value = result
            } catch (e: Exception) {
                _error.value = e.message ?: "Login error"
            } finally {
                _loading.value = false
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            repo.logout()
            _user.value = null
        }
    }

    fun loadUser(uid: String) {
        _loading.value = true
        _error.value = null
        viewModelScope.launch {
            try {
                val user = repo.getUserById(uid)
                _user.value = user
            } catch (e: Exception) {
                _error.value = e.message ?: "Load user error"
            } finally {
                _loading.value = false
            }
        }
    }

    suspend fun loadSavedUserIfAny() {
        val saved = repo.getSavedUserId()
        if (saved != null) {
            viewModelScope.launch {
                _user.value = repo.getUserById(saved)
            }
        }
    }

    fun updateProfile(name: String? = null, phone: String? = null) {
        val current = _user.value ?: return
        viewModelScope.launch {
            try {
                val updated = current.copy(
                    displayName = name ?: current.displayName,
                    phone = phone ?: current.phone
                )
                repo.updateUser(updated)
                _user.value = updated
            } catch (e: Exception) {
                _error.value = e.message ?: "Update profile error"
            }
        }
    }

    fun updatePhone(newPhone: String) {
        val current = _user.value ?: return
        viewModelScope.launch {
            try {
                val updated = current.copy(phone = newPhone)
                repo.updateUser(updated)
                _user.value = updated
            } catch (e: Exception) {
                _error.value = e.message ?: "Update phone error"
            }
        }
    }

    fun updateDisplayName(newName: String) {
        val current = _user.value ?: return
        viewModelScope.launch {
            try {
                val updated = current.copy(displayName = newName)
                repo.updateUser(updated)
                _user.value = updated
            } catch (e: Exception) {
                _error.value = e.message ?: "Update profile error"
            }
        }
    }

    fun updatePoints(newPoints: Int) {
        val current = _user.value ?: return
        viewModelScope.launch {
            try {
                val updated = current.copy(points = newPoints)
                repo.updateUser(updated)
                _user.value = updated
            } catch (e: Exception) {
                _error.value = e.message ?: "Update points error"
            }
        }
    }

    fun getUserId(): String? = _user.value?.uid
    suspend fun getSavedUserId(): String? = repo.getSavedUserId()
}
