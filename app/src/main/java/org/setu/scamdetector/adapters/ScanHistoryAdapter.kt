package org.setu.scamdetector.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.setu.scamdetector.databinding.CardScanResultBinding
import org.setu.scamdetector.models.ScanResultModel

interface ScamListener {
    fun onScamClick(scam: ScanResultModel)
}

class ScanHistoryAdapter(private var scams: List<ScanResultModel>,
                         private val listener: ScamListener) :
        RecyclerView.Adapter<ScanHistoryAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardScanResultBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)

        return MainHolder(binding)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val scam = scams[holder.adapterPosition]
        holder.bind(scam, listener)
    }

    override fun getItemCount(): Int = scams.size

    class MainHolder(private val binding : CardScanResultBinding) :
            RecyclerView.ViewHolder(binding.root) {

        fun bind(scam: ScanResultModel, listener: ScamListener) {
            binding.scamTitle.text = scam.title
            binding.description.text = scam.description
            binding.root.setOnClickListener { listener.onScamClick(scam) }
        }
    }
}
