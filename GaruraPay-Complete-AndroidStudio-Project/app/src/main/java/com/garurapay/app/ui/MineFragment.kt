package com.garurapay.app.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.garurapay.app.R
import com.garurapay.app.admin.AdminLoginActivity
import com.garurapay.app.auth.LoginActivity
import com.garurapay.app.data.AppDatabase
import com.garurapay.app.data.SessionManager
import com.garurapay.app.history.OrderHistoryActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

class MineFragment : Fragment() {

    private lateinit var tvCurrentBalance: TextView
    private lateinit var tvUserId: TextView
    private lateinit var tvUserName: TextView
    private lateinit var sessionManager: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_mine, container, false)
        sessionManager = SessionManager(requireContext())

        tvCurrentBalance = view.findViewById(R.id.tvCurrentBalance)
        tvUserId = view.findViewById(R.id.tvUserTier)
        tvUserName = view.findViewById(R.id.tvUserName)

        val layoutDeposit: View = view.findViewById(R.id.layoutDeposit)
        val layoutWithdraw: View = view.findViewById(R.id.layoutWithdraw)
        val layoutOrderHistory: View = view.findViewById(R.id.layoutOrderHistory)
        val btnAdminAccess: View = view.findViewById(R.id.layoutAdminPortal)
        val btnLogout: View = view.findViewById(R.id.layoutLogout)

        // Dynamic User Name from registration session data
        val userName = sessionManager.getUserName()
        tvUserName.text = userName

        val userId = sessionManager.getUserId()
        tvUserId.text = "User ID: $userId"

        loadBalanceFromDatabase(userId)

        layoutDeposit.setOnClickListener {
            Toast.makeText(requireContext(), "Deposit Gateway - Go to Buy RP tab", Toast.LENGTH_SHORT).show()
        }

        // Updated Withdrawal Flow: Prompts user to manually enter UPI ID in popup every time
        layoutWithdraw.setOnClickListener {
            val builder = android.app.AlertDialog.Builder(requireContext())
            builder.setTitle("Withdraw RP Payout")
            builder.setMessage("Please enter your UPI ID to receive payout:")

            val inputUpi = android.widget.EditText(requireContext()).apply {
                hint = "Enter UPI ID (e.g. user@okhdfcbank)"
                setTextColor(android.graphics.Color.BLACK)
                setHintTextColor(android.graphics.Color.GRAY)
                inputType = android.text.InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
            }
            val container = android.widget.FrameLayout(requireContext()).apply {
                val padding = (20 * resources.displayMetrics.density).toInt()
                setPadding(padding, 8, padding, 8)
                addView(inputUpi)
            }
            builder.setView(container)

            builder.setPositiveButton("Submit") { _, _ ->
                val upi = inputUpi.text.toString().trim()
                if (upi.isEmpty()) {
                    Toast.makeText(requireContext(), "Please enter a valid UPI ID", Toast.LENGTH_SHORT).show()
                } else if (!upi.contains("@")) {
                    Toast.makeText(requireContext(), "UPI ID must contain '@'", Toast.LENGTH_SHORT).show()
                } else {
                    android.app.AlertDialog.Builder(requireContext())
                        .setTitle("Payout Requirement")
                        .setMessage("Complete 3 orders for payout")
                        .setPositiveButton("OK") { d, _ -> d.dismiss() }
                        .show()
                }
            }
            builder.setNegativeButton("Cancel") { d, _ -> d.dismiss() }
            builder.show()
        }

        // Functional Order & Settlement History
        layoutOrderHistory.setOnClickListener {
            startActivity(Intent(requireContext(), OrderHistoryActivity::class.java))
        }

        btnAdminAccess.setOnClickListener {
            startActivity(Intent(requireContext(), AdminLoginActivity::class.java))
        }

        btnLogout.setOnClickListener {
            sessionManager.logout()
            val intent = Intent(requireContext(), LoginActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
        }

        return view
    }

    override fun onResume() {
        super.onResume()
        tvUserName.text = sessionManager.getUserName()
        loadBalanceFromDatabase(sessionManager.getUserId())
    }

    private fun loadBalanceFromDatabase(userId: String) {
        lifecycleScope.launch {
            val db = AppDatabase.getInstance(requireContext())
            val balance = withContext(Dispatchers.IO) {
                db.userDao().getBalance(userId) ?: sessionManager.getUserBalance()
            }
            sessionManager.updateBalance(balance)
            tvCurrentBalance.text = String.format(Locale.US, "%.2f", balance)
        }
    }
}
