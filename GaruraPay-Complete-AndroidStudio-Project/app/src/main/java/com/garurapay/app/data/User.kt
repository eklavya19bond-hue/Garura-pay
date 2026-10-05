package com.garurapay.app.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey
    val userId: String, // Unique 9-digit ID (e.g. "839201948")
    val email: String,
    var passwordHash: String,
    val fullName: String,
    var balance: Double = 0.00
)
