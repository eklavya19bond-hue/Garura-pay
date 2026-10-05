package com.garurapay.app.data

import android.content.Context
import androidx.room.*

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Query("SELECT * FROM users WHERE userId = :userId LIMIT 1")
    suspend fun getUserById(userId: String): User?

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): User?

    @Query("UPDATE users SET passwordHash = :newPassword WHERE userId = :userId AND email = :email")
    suspend fun updatePassword(userId: String, email: String, newPassword: String): Int

    @Query("UPDATE users SET balance = balance + :amount WHERE userId = :userId")
    suspend fun addBalance(userId: String, amount: Double)

    @Query("SELECT balance FROM users WHERE userId = :userId")
    suspend fun getBalance(userId: String): Double?
}

@Dao
interface PaymentSubmissionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSubmission(submission: PaymentSubmission)

    @Query("SELECT * FROM payment_submissions ORDER BY timestamp DESC")
    suspend fun getAllSubmissions(): List<PaymentSubmission>

    @Query("UPDATE payment_submissions SET status = :status, creditedAmount = :credited WHERE submissionId = :id")
    suspend fun updateStatus(id: String, status: String, credited: Double)
}

@Database(entities = [User::class, PaymentSubmission::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun paymentSubmissionDao(): PaymentSubmissionDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "garura_pay_database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
