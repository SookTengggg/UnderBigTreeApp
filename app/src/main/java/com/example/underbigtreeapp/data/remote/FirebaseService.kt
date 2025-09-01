package com.example.underbigtreeapp.data.remote

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.example.underbigtreeapp.model.User
import com.example.underbigtreeapp.model.toFirestoreMap
import com.example.underbigtreeapp.model.toUser
import kotlinx.coroutines.tasks.await

class FirebaseService {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    suspend fun register(email: String, password: String, name: String, phone: String): String? {
        val result = auth.createUserWithEmailAndPassword(email, password).await()
        val uid = result.user?.uid ?: return null

        val user = User(uid = uid, email = email, displayName = name, phone = phone, points = 0)
        db.collection("users").document(uid).set(user.toFirestoreMap()).await()
        return uid
    }

    suspend fun login(email: String, password: String): String? {
        val result = auth.signInWithEmailAndPassword(email, password).await()
        return result.user?.uid
    }

    suspend fun getUser(uid: String): User? {
        val doc = db.collection("users").document(uid).get().await()
        if (!doc.exists()) return null
        val data = doc.data ?: return null
        return data.toUser(uid)
    }

    suspend fun updateUser(user: User) {
        db.collection("users").document(user.uid).update(user.toFirestoreMap()).await()
    }

    suspend fun updatePoints(uid: String, newPoints: Int) {
        db.collection("users").document(uid).update("points", newPoints).await()
    }

    suspend fun updateDisplayName(uid: String, newName: String) {
        db.collection("users").document(uid).update("displayName", newName).await()
    }

    fun logout() {
        auth.signOut()
    }
}
