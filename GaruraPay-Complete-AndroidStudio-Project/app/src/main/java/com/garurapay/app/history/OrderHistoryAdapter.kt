package com.garurapay.app.history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.garurapay.app.R
import com.garurapay.app.data.PaymentSubmission
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class OrderHistoryAdapter(
    private val orders: List<PaymentSubmission>
) : RecyclerView.Adapter<OrderHistoryAdapter.HistoryViewHolder>() {

    class HistoryViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val tvPackageName: TextView = v.findViewById(R.id.tvHistoryPackageName)
        val tvStatus: TextView = v.findViewById(R.id.tvHistoryStatus)
        val tvAmount: TextView = v.findViewById(R.id.tvHistoryAmount)
        val tvReward: TextView = v.findViewById(R.id.tvHistoryReward)
        val tvFinal: TextView = v.findViewById(R.id.tvHistoryFinal)
        val tvDate: TextView = v.findViewById(R.id.tvHistoryDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_order_history, parent, false)
        return HistoryViewHolder(v)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val item = orders[position]

        holder.tvPackageName.text = "RP Package #${item.orderId}"
        holder.tvAmount.text = String.format(Locale.US, "₹%.2f", item.orderAmount)
        holder.tvReward.text = String.format(Locale.US, "+%.2f RP", item.rewardAmount)
        holder.tvFinal.text = String.format(Locale.US, "%.2f RP", item.finalAmount)

        val dateFormatted = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.US).format(Date(item.timestamp))
        holder.tvDate.text = dateFormatted

        when (item.status) {
            "APPROVED" -> {
                holder.tvStatus.text = "APPROVED"
                holder.tvStatus.setTextColor(0xFF059669.toInt())
            }
            "DENIED" -> {
                holder.tvStatus.text = "DENIED"
                holder.tvStatus.setTextColor(0xFFDC2626.toInt())
            }
            else -> {
                holder.tvStatus.text = "PENDING"
                holder.tvStatus.setTextColor(0xFFD97706.toInt())
            }
        }
    }

    override fun getItemCount(): Int = orders.size
}
