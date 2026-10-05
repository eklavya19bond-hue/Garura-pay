package com.garurapay.app.history

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.garurapay.app.R
import com.garurapay.app.data.AppDatabase
import com.garurapay.app.data.SessionManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class OrderHistoryActivity : AppCompatActivity() {

    private lateinit var rvHistory: RecyclerView
    private lateinit var tvEmptyHistory: TextView
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_history)

        sessionManager = SessionManager(this)
        rvHistory = findViewById(R.id.rvOrderHistory)
        tvEmptyHistory = findViewById(R.id.tvEmptyHistory)
        rvHistory.layoutManager = LinearLayoutManager(this)

        loadOrderHistory()
    }

    private fun loadOrderHistory() {
        val currentUserId = sessionManager.getUserId()
        lifecycleScope.launch {
            val db = AppDatabase.getInstance(this@OrderHistoryActivity)
            val submissions = withContext(Dispatchers.IO) {
                db.paymentSubmissionDao().getAllSubmissions().filter { it.userId == currentUserId }
            }

            if (submissions.isEmpty()) {
                tvEmptyHistory.visibility = View.VISIBLE
                rvHistory.visibility = View.GONE
            } else {
                tvEmptyHistory.visibility = View.GONE
                rvHistory.visibility = View.VISIBLE
                rvHistory.adapter = OrderHistoryAdapter(submissions)
            }
        }
    }
}
