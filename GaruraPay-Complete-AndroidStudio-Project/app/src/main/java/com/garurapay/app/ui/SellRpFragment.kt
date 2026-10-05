package com.garurapay.app.ui

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.garurapay.app.R
import com.garurapay.app.data.SessionManager

class SellRpFragment : Fragment() {

    private lateinit var etRpAmount: EditText
    private lateinit var etUserUpiId: EditText
    private lateinit var btnSubmitSell: Button
    private lateinit var sessionManager: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_sell_rp, container, false)
        sessionManager = SessionManager(requireContext())

        etRpAmount = view.findViewById(R.id.etRpAmount)
        etUserUpiId = view.findViewById(R.id.etUserUpiId)
        btnSubmitSell = view.findViewById(R.id.btnSubmitSell)

        btnSubmitSell.setOnClickListener {
            val rp = etRpAmount.text.toString().trim()
            val upiId = etUserUpiId.text.toString().trim()

            if (rp.isEmpty()) {
                Toast.makeText(requireContext(), "Please enter RP amount", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (upiId.isEmpty()) {
                Toast.makeText(requireContext(), "Please enter your UPI ID", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!upiId.contains("@")) {
                Toast.makeText(requireContext(), "Invalid UPI ID. Must contain '@' (e.g. user@okhdfcbank)", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Display popup stating exactly "Complete 3 orders for payout"
            AlertDialog.Builder(requireContext())
                .setTitle("Payout Requirement")
                .setMessage("Complete 3 orders for payout")
                .setPositiveButton("OK") { d, _ -> d.dismiss() }
                .show()
        }

        return view
    }
}
