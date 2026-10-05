package com.garurapay.app.data

import java.io.Serializable

data class Order(
    val id: Int,
    val orderAmount: Double,
    val rewardAmount: Double,
    val finalAmount: Double
) : Serializable
