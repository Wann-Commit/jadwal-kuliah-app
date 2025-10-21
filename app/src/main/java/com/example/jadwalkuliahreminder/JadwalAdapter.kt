package com.example.jadwalkuliahreminder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

// JadwalAdapter.kt
class JadwalAdapter(
    private val jadwalList: List<MataKuliah>,
    private val onEdit: (MataKuliah) -> Unit,
    private val onDelete: (MataKuliah) -> Unit
) : RecyclerView.Adapter<JadwalAdapter.JadwalViewHolder>() {

    class JadwalViewHolder(view: android.view.View) : RecyclerView.ViewHolder(view) {
        val tvNama: android.widget.TextView = view.findViewById(R.id.tvNamaMK)
        val tvHari: android.widget.TextView = view.findViewById(R.id.tvHari)
        val tvJam: android.widget.TextView = view.findViewById(R.id.tvJam)
        val tvRuangan: android.widget.TextView = view.findViewById(R.id.tvRuangan)
        val tvDosen: android.widget.TextView = view.findViewById(R.id.tvDosen)
        val btnEdit: android.widget.ImageButton = view.findViewById(R.id.btnEdit)
        val btnDelete: android.widget.ImageButton = view.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: android.view.ViewGroup, viewType: Int): JadwalViewHolder {
        val view = android.view.LayoutInflater.from(parent.context)
            .inflate(R.layout.item_jadwal, parent, false)
        return JadwalViewHolder(view)
    }

    override fun onBindViewHolder(holder: JadwalViewHolder, position: Int) {
        val mataKuliah = jadwalList[position]
        holder.tvNama.text = mataKuliah.nama
        holder.tvHari.text = mataKuliah.hari
        holder.tvJam.text = "${mataKuliah.jamMulai} - ${mataKuliah.jamSelesai}"
        holder.tvRuangan.text = "Ruangan: ${mataKuliah.ruangan}"
        holder.tvDosen.text = "Dosen: ${mataKuliah.dosen}"

        holder.btnEdit.setOnClickListener { onEdit(mataKuliah) }
        holder.btnDelete.setOnClickListener { onDelete(mataKuliah) }
    }

    override fun getItemCount() = jadwalList.size
}
