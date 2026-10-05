package com.garurapay.app.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "payment_submissions")
data class PaymentSubmission(
    @PrimaryKey
    val submissionId: String,
    val userId: String, // 9-digit User ID of submitter
    val orderId: Int,
    val orderAmount: Double,
    val rewardAmount: Double,
    val finalAmount: Double,
    val screenshotUri: String,
    var status: String = "PENDING", // PENDING, APPROVED, DENIED
    val timestamp: Long = System.currentTimeMillis(),
    var creditedAmount: Double = 0.00
)
