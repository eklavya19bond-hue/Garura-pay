package com.garurapay.app.admin

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.garurapay.app.R
import com.garurapay.app.data.PaymentSubmission
import java.util.Locale

class PaymentSubmissionAdapter(
    private val submissions: MutableList<PaymentSubmission>,
    private val onApprove: (PaymentSubmission, Double) -> Unit,
    private val onDeny: (PaymentSubmission) -> Unit
) : RecyclerView.Adapter<PaymentSubmissionAdapter.SubmissionViewHolder>() {

    class SubmissionViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val tvUserId: TextView = v.findViewById(R.id.tvSubmissionUserId)
        val tvOrderInfo: TextView = v.findViewById(R.id.tvSubmissionOrderInfo)
        val tvStatus: TextView = v.findViewById(R.id.tvSubmissionStatus)
        val ivScreenshot: ImageView = v.findViewById(R.id.ivSubmissionScreenshot)
        val etCreditAmount: EditText = v.findViewById(R.id.etCreditAmount)
        val btnApprove: Button = v.findViewById(R.id.btnApproveSubmission)
        val btnDeny: Button = v.findViewById(R.id.btnDenySubmission)
        val layoutActions: LinearLayout = v.findViewById(R.id.layoutAdminActions)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubmissionViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_admin_payment_submission, parent, false)
        return SubmissionViewHolder(v)
    }

    override fun onBindViewHolder(holder: SubmissionViewHolder, position: Int) {
        val item = submissions[position]

        holder.tvUserId.text = "User ID: ${item.userId}"
        holder.tvOrderInfo.text = String.format(Locale.US, "Order #%d • Base: ₹%.2f • Final: %.2f RP", item.orderId, item.orderAmount, item.finalAmount)
        holder.tvStatus.text = item.status

        try {
            holder.ivScreenshot.setImageURI(Uri.parse(item.screenshotUri))
        } catch (e: Exception) {
            holder.ivScreenshot.setImageResource(R.drawable.ic_screenshot_placeholder)
        }

        if (holder.etCreditAmount.text.isEmpty()) {
            holder.etCreditAmount.setText(String.format(Locale.US, "%.2f", item.finalAmount))
        }

        if (item.status == "PENDING") {
            holder.layoutActions.visibility = View.VISIBLE
            holder.tvStatus.setTextColor(0xFFD97706.toInt())

            holder.btnApprove.setOnClickListener {
                val inputStr = holder.etCreditAmount.text.toString().trim()
                val creditAmt = inputStr.toDoubleOrNull() ?: item.finalAmount
                onApprove(item, creditAmt)
            }

            holder.btnDeny.setOnClickListener {
                onDeny(item)
            }
        } else {
            holder.layoutActions.visibility = View.GONE
            if (item.status == "APPROVED") {
                holder.tvStatus.setTextColor(0xFF059669.toInt())
                holder.tvStatus.text = String.format(Locale.US, "APPROVED (+%.2f credited)", item.creditedAmount)
            } else {
                holder.tvStatus.setTextColor(0xFFDC2626.toInt())
                holder.tvStatus.text = "DENIED"
            }
        }
    }

    override fun getItemCount(): Int = submissions.size
}
