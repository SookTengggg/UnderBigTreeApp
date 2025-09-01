package com.example.underbigtreeapp.model

data class User(
    val uid: String,
    val email: String,
    val displayName: String,
    val phone: String,
    val points: Int = 0
)

fun Map<String, Any?>.toUser(uid: String): User {
    val email = this["email"] as? String ?: ""
    val displayName = this["displayName"] as? String ?: "Default Name"
    val phone = this["phone"] as? String ?: ""
    val points = when (val p = this["points"]) {
        is Number -> p.toInt()
        else -> 0
    }
    return User(
        uid = uid,
        email = email,
        displayName = displayName,
        phone = phone,
        points = points
    )
}

fun User.toFirestoreMap(): Map<String, Any> = mapOf(
    "email" to email,
    "displayName" to displayName,
    "phone" to phone,
    "points" to points
)