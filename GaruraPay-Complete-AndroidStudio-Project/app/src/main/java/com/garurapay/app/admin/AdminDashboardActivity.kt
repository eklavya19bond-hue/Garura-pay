package com.garurapay.app.admin

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.garurapay.app.R
import com.garurapay.app.data.AppDatabase
import com.garurapay.app.data.PaymentSubmission
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

class AdminDashboardActivity : AppCompatActivity() {

    private lateinit var rvSubmissions: RecyclerView
    private lateinit var tvEmptyState: TextView
    private lateinit var adapter: PaymentSubmissionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_dashboard)

        rvSubmissions = findViewById(R.id.rvAdminSubmissions)
        tvEmptyState = findViewById(R.id.tvAdminEmptyState)
        rvSubmissions.layoutManager = LinearLayoutManager(this)

        loadSubmissions()
    }

    private fun loadSubmissions() {
        lifecycleScope.launch {
            val db = AppDatabase.getInstance(this@AdminDashboardActivity)
            val submissions = withContext(Dispatchers.IO) {
                db.paymentSubmissionDao().getAllSubmissions()
            }

            if (submissions.isEmpty()) {
                tvEmptyState.visibility = View.VISIBLE
                rvSubmissions.visibility = View.GONE
            } else {
                tvEmptyState.visibility = View.GONE
                rvSubmissions.visibility = View.VISIBLE

                adapter = PaymentSubmissionAdapter(
                    submissions.toMutableList(),
                    onApprove = { submission, amountToCredit ->
                        handleApproveSubmission(submission, amountToCredit)
                    },
                    onDeny = { submission ->
                        handleDenySubmission(submission)
                    }
                )
                rvSubmissions.adapter = adapter
            }
        }
    }

    private fun handleApproveSubmission(submission: PaymentSubmission, amountToCredit: Double) {
        lifecycleScope.launch {
            val db = AppDatabase.getInstance(this@AdminDashboardActivity)
            withContext(Dispatchers.IO) {
                db.userDao().addBalance(submission.userId, amountToCredit)
                db.paymentSubmissionDao().updateStatus(submission.submissionId, "APPROVED", amountToCredit)
            }

            Toast.makeText(
                this@AdminDashboardActivity,
                String.format(Locale.US, "Approved! ₹%.2f credited to User ID: %s", amountToCredit, submission.userId),
                Toast.LENGTH_LONG
            ).show()

            loadSubmissions()
        }
    }

    private fun handleDenySubmission(submission: PaymentSubmission) {
        lifecycleScope.launch {
            val db = AppDatabase.getInstance(this@AdminDashboardActivity)
            withContext(Dispatchers.IO) {
                db.paymentSubmissionDao().updateStatus(submission.submissionId, "DENIED", 0.0)
            }
            Toast.makeText(this@AdminDashboardActivity, "Submission denied", Toast.LENGTH_SHORT).show()
            loadSubmissions()
        }
    }
}
