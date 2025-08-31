package com.example.underbigtreeapp.repository

import android.content.Context
import com.example.underbigtreeapp.data.local.AppDatabase
import com.example.underbigtreeapp.data.local.UserDao
import com.example.underbigtreeapp.data.local.toEntity
import com.example.underbigtreeapp.data.local.toUser
import com.example.underbigtreeapp.data.remote.FirebaseService
import com.example.underbigtreeapp.model.User
import com.example.underbigtreeapp.utils.UserPreferences
import kotlinx.coroutines.flow.Flow

class UserRepository private constructor(
    private val firebaseService: FirebaseService,
    private val prefs: UserPreferences,
    private val userDao: UserDao
) {

    companion object {
        @Volatile
        private var INSTANCE: UserRepository? = null

        fun getInstance(context: Context): UserRepository {
            return INSTANCE ?: synchronized(this) {
                val db = AppDatabase.getDatabase(context.applicationContext)
                val instance = UserRepository(
                    firebaseService = FirebaseService(),
                    prefs = UserPreferences(context.applicationContext),
                    userDao = db.userDao()
                )
                INSTANCE = instance
                instance
            }
        }
    }

    // --- Auth ---
    suspend fun registerUser(email: String, password: String, name: String, phone: String): User? {
        val uid = firebaseService.register(email, password, name, phone) ?: return null
        val newUser = User(uid = uid, email = email, displayName = name, phone = phone, points = 0)
        userDao.insertUser(newUser.toEntity())
        prefs.saveUserId(uid)
        return newUser
    }

    suspend fun loginUser(email: String, password: String): User? {
        val uid = firebaseService.login(email, password) ?: return null
        prefs.saveUserId(uid)

        // 1. Try local (offline support)
        val local = userDao.getUserById(uid)?.toUser()
        if (local != null) return local

        // 2. Remote fallback
        val remote = firebaseService.getUser(uid) ?: return null
        userDao.insertUser(remote.toEntity())
        return remote
    }

    // --- Profile ---
    suspend fun getUserById(uid: String): User? {
        // Load local first
        val local = userDao.getUserById(uid)?.toUser()
        if (local != null) {
            // Fetch remote in background and sync
            val remote = firebaseService.getUser(uid)
            if (remote != null) {
                userDao.insertUser(remote.toEntity())
                return remote
            }
            return local
        }

        // No local → fetch remote
        val remote = firebaseService.getUser(uid)
        return if (remote != null) {
            userDao.insertUser(remote.toEntity())
            remote
        } else {
            prefs.clear()
            null
        }
    }

    suspend fun updateUser(user: User) {
        userDao.insertUser(user.toEntity()) // save locally
        firebaseService.updateUser(user)    // sync remotely
    }

    suspend fun logout() {
        prefs.clear()
        firebaseService.logout()
        userDao.clearAll()
    }

    fun getSavedUserIdFlow(): Flow<String?> = prefs.getUserIdFlow()
    suspend fun getSavedUserId(): String? = prefs.getUserId()
}
