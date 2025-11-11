// MainActivity.kt
package com.example.jadwalkuliahreminder

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.util.*

data class MataKuliah(
    val id: Int,
    val nama: String,
    val hari: String,
    val jamMulai: String,
    val jamSelesai: String,
    val ruangan: String,
    val dosen: String,
    val reminder: Boolean = true
)

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: JadwalAdapter
    private lateinit var bottomNavigation: BottomNavigationView
    private val jadwalList = mutableListOf<MataKuliah>()
    private var nextId = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        createNotificationChannel()

        // Setup RecyclerView
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = JadwalAdapter(jadwalList,
            onEdit = { mataKuliah -> editJadwal(mataKuliah) },
            onDelete = { mataKuliah -> deleteJadwal(mataKuliah) }
        )
        recyclerView.adapter = adapter

        // Setup Bottom Navigation
        bottomNavigation = findViewById(R.id.bottomNavigation)
        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_jadwal -> {
                    // Sudah di halaman jadwal
                    true
                }
                R.id.nav_add -> {
                    showAddDialog()
                    true
                }
                R.id.nav_settings -> {
                    showSettingsDialog()
                    true
                }
                else -> false
            }
        }

        // Set default selected item
        bottomNavigation.selectedItemId = R.id.nav_jadwal

        // Contoh data
        addSampleData()
    }

    private fun showSettingsDialog() {
        val options = arrayOf(
            "Tentang Aplikasi",
            "Hapus Semua Jadwal",
            "Export Jadwal"
        )

        AlertDialog.Builder(this)
            .setTitle("Pengaturan")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> showAboutDialog()
                    1 -> showDeleteAllDialog()
                    2 -> Toast.makeText(this, "Fitur export akan segera hadir", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Tutup", null)
            .show()
    }

    private fun showAboutDialog() {
        AlertDialog.Builder(this)
            .setTitle("Tentang Aplikasi")
            .setMessage("Aplikasi Jadwal Kuliah v1.0\n\nDibuat untuk membantu mahasiswa mengatur jadwal kuliah dengan fitur reminder otomatis.")
            .setPositiveButton("OK", null)
            .show()
    }

    private fun showDeleteAllDialog() {
        if (jadwalList.isEmpty()) {
            Toast.makeText(this, "Tidak ada jadwal untuk dihapus", Toast.LENGTH_SHORT).show()
            return
        }

        AlertDialog.Builder(this)
            .setTitle("Hapus Semua Jadwal")
            .setMessage("Yakin ingin menghapus semua jadwal?")
            .setPositiveButton("Hapus") { _, _ ->
                // Cancel semua reminder
                jadwalList.forEach { cancelReminder(it) }

                jadwalList.clear()
                adapter.notifyDataSetChanged()
                Toast.makeText(this, "Semua jadwal dihapus", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    private fun addSampleData() {
        jadwalList.add(MataKuliah(nextId++, "Pemrograman Mobile", "Senin", "08:00", "10:30", "Lab 301", "Dr. Budi", true))
        jadwalList.add(MataKuliah(nextId++, "Basis Data", "Selasa", "13:00", "15:30", "Ruang 201", "Prof. Siti", true))
        jadwalList.add(MataKuliah(nextId++, "Algoritma", "Rabu", "09:00", "11:30", "Ruang 102", "Dr. Ahmad", true))
        adapter.notifyDataSetChanged()

        // Set reminder untuk semua jadwal
        jadwalList.forEach { setReminder(it) }
    }

    private fun showAddDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_jadwal, null)
        val etNama = dialogView.findViewById<android.widget.EditText>(R.id.etNamaMK)
        val spinnerHari = dialogView.findViewById<android.widget.Spinner>(R.id.spinnerHari)
        val etJamMulai = dialogView.findViewById<android.widget.EditText>(R.id.etJamMulai)
        val etJamSelesai = dialogView.findViewById<android.widget.EditText>(R.id.etJamSelesai)
        val etRuangan = dialogView.findViewById<android.widget.EditText>(R.id.etRuangan)
        val etDosen = dialogView.findViewById<android.widget.EditText>(R.id.etDosen)
        val switchReminder = dialogView.findViewById<android.widget.Switch>(R.id.switchReminder)

        val hariArray = arrayOf("Senin", "Selasa", "Rabu", "Kamis", "Jumat", "Sabtu", "Minggu")
        val spinnerAdapter = android.widget.ArrayAdapter(this, android.R.layout.simple_spinner_item, hariArray)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerHari.adapter = spinnerAdapter

        etJamMulai.setOnClickListener { showTimePicker { time -> etJamMulai.setText(time) } }
        etJamSelesai.setOnClickListener { showTimePicker { time -> etJamSelesai.setText(time) } }

        AlertDialog.Builder(this)
            .setTitle("Tambah Jadwal")
            .setView(dialogView)
            .setPositiveButton("Simpan") { _, _ ->
                val mataKuliah = MataKuliah(
                    id = nextId++,
                    nama = etNama.text.toString(),
                    hari = spinnerHari.selectedItem.toString(),
                    jamMulai = etJamMulai.text.toString(),
                    jamSelesai = etJamSelesai.text.toString(),
                    ruangan = etRuangan.text.toString(),
                    dosen = etDosen.text.toString(),
                    reminder = switchReminder.isChecked
                )
                jadwalList.add(mataKuliah)
                adapter.notifyItemInserted(jadwalList.size - 1)

                if (mataKuliah.reminder) {
                    setReminder(mataKuliah)
                }
                Toast.makeText(this, "Jadwal ditambahkan", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    private fun showTimePicker(onTimeSelected: (String) -> Unit) {
        val picker = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_24H)
            .setHour(8)
            .setMinute(0)
            .setTitleText("Pilih Waktu")
            .build()

        picker.addOnPositiveButtonClickListener {
            val hour = String.format("%02d", picker.hour)
            val minute = String.format("%02d", picker.minute)
            onTimeSelected("$hour:$minute")
        }
        picker.show(supportFragmentManager, "timePicker")
    }

    private fun showEditDialog(mataKuliah: MataKuliah) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_jadwal, null)
        val etNama = dialogView.findViewById<android.widget.EditText>(R.id.etNamaMK)
        val spinnerHari = dialogView.findViewById<android.widget.Spinner>(R.id.spinnerHari)
        val etJamMulai = dialogView.findViewById<android.widget.EditText>(R.id.etJamMulai)
        val etJamSelesai = dialogView.findViewById<android.widget.EditText>(R.id.etJamSelesai)
        val etRuangan = dialogView.findViewById<android.widget.EditText>(R.id.etRuangan)
        val etDosen = dialogView.findViewById<android.widget.EditText>(R.id.etDosen)
        val switchReminder = dialogView.findViewById<android.widget.Switch>(R.id.switchReminder)

        // Isi data yang sudah ada
        etNama.setText(mataKuliah.nama)
        etJamMulai.setText(mataKuliah.jamMulai)
        etJamSelesai.setText(mataKuliah.jamSelesai)
        etRuangan.setText(mataKuliah.ruangan)
        etDosen.setText(mataKuliah.dosen)
        switchReminder.isChecked = mataKuliah.reminder

        // Setup spinner
        val hariArray = arrayOf("Senin", "Selasa", "Rabu", "Kamis", "Jumat", "Sabtu", "Minggu")
        val spinnerAdapter = android.widget.ArrayAdapter(this, android.R.layout.simple_spinner_item, hariArray)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerHari.adapter = spinnerAdapter

        // Set posisi spinner sesuai hari yang dipilih
        val hariPosition = hariArray.indexOf(mataKuliah.hari)
        if (hariPosition >= 0) {
            spinnerHari.setSelection(hariPosition)
        }

        etJamMulai.setOnClickListener { showTimePicker { time -> etJamMulai.setText(time) } }
        etJamSelesai.setOnClickListener { showTimePicker { time -> etJamSelesai.setText(time) } }

        AlertDialog.Builder(this)
            .setTitle("Edit Jadwal")
            .setView(dialogView)
            .setPositiveButton("Simpan") { _, _ ->
                // Hapus reminder lama
                cancelReminder(mataKuliah)

                // Update data
                val position = jadwalList.indexOf(mataKuliah)
                if (position != -1) {
                    val updatedMataKuliah = MataKuliah(
                        id = mataKuliah.id,
                        nama = etNama.text.toString(),
                        hari = spinnerHari.selectedItem.toString(),
                        jamMulai = etJamMulai.text.toString(),
                        jamSelesai = etJamSelesai.text.toString(),
                        ruangan = etRuangan.text.toString(),
                        dosen = etDosen.text.toString(),
                        reminder = switchReminder.isChecked
                    )

                    jadwalList[position] = updatedMataKuliah
                    adapter.notifyItemChanged(position)

                    // Set reminder baru jika aktif
                    if (updatedMataKuliah.reminder) {
                        setReminder(updatedMataKuliah)
                    }

                    Toast.makeText(this, "Jadwal berhasil diupdate", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    private fun editJadwal(mataKuliah: MataKuliah) {
        showEditDialog(mataKuliah)
    }

    private fun deleteJadwal(mataKuliah: MataKuliah) {
        AlertDialog.Builder(this)
            .setTitle("Hapus Jadwal")
            .setMessage("Yakin ingin menghapus ${mataKuliah.nama}?")
            .setPositiveButton("Hapus") { _, _ ->
                val position = jadwalList.indexOf(mataKuliah)
                jadwalList.remove(mataKuliah)
                adapter.notifyItemRemoved(position)
                cancelReminder(mataKuliah)
                Toast.makeText(this, "Jadwal dihapus", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    private fun setReminder(mataKuliah: MataKuliah) {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        // Reminder 1: 10 menit sebelum kuliah
        setAlarmForReminder(alarmManager, mataKuliah, -10, "sebelum")

        // Reminder 2: Saat kuliah dimulai
        setAlarmForReminder(alarmManager, mataKuliah, 0, "mulai")
    }

    private fun setAlarmForReminder(
        alarmManager: AlarmManager,
        mataKuliah: MataKuliah,
        minuteOffset: Int,
        type: String
    ) {
        val intent = Intent(this, ReminderReceiver::class.java).apply {
            putExtra("MATA_KULIAH", mataKuliah.nama)
            putExtra("RUANGAN", mataKuliah.ruangan)
            putExtra("JAM", mataKuliah.jamMulai)
            putExtra("REMINDER_TYPE", type)
        }

        val requestCode = mataKuliah.id * 10 + if (type == "sebelum") 1 else 2
        val pendingIntent = PendingIntent.getBroadcast(
            this,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val calendar = Calendar.getInstance().apply {
            set(Calendar.DAY_OF_WEEK, getDayOfWeek(mataKuliah.hari))

            val timeParts = mataKuliah.jamMulai.split(":")
            val hour = timeParts[0].toInt()
            val minute = timeParts[1].toInt()

            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute + minuteOffset)
            set(Calendar.SECOND, 0)

            // Jika waktu sudah lewat, set untuk minggu depan
            if (timeInMillis <= System.currentTimeMillis()) {
                add(Calendar.WEEK_OF_YEAR, 1)
            }
        }

        // Gunakan setExactAndAllowWhileIdle untuk Android 12+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (alarmManager.canScheduleExactAlarms()) {
                alarmManager.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    AlarmManager.INTERVAL_DAY * 7,
                    pendingIntent
                )
            } else {
                // Minta izin schedule exact alarm
                Toast.makeText(this, "Izinkan notifikasi tepat waktu di Settings", Toast.LENGTH_LONG).show()
            }
        } else {
            alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                AlarmManager.INTERVAL_DAY * 7,
                pendingIntent
            )
        }
    }

    private fun cancelReminder(mataKuliah: MataKuliah) {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        // Cancel reminder 10 menit sebelum
        val intent1 = Intent(this, ReminderReceiver::class.java)
        val pendingIntent1 = PendingIntent.getBroadcast(
            this,
            mataKuliah.id * 10 + 1,
            intent1,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent1)

        // Cancel reminder saat mulai
        val intent2 = Intent(this, ReminderReceiver::class.java)
        val pendingIntent2 = PendingIntent.getBroadcast(
            this,
            mataKuliah.id * 10 + 2,
            intent2,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent2)
    }

    private fun getDayOfWeek(hari: String): Int {
        return when (hari) {
            "Minggu" -> Calendar.SUNDAY
            "Senin" -> Calendar.MONDAY
            "Selasa" -> Calendar.TUESDAY
            "Rabu" -> Calendar.WEDNESDAY
            "Kamis" -> Calendar.THURSDAY
            "Jumat" -> Calendar.FRIDAY
            "Sabtu" -> Calendar.SATURDAY
            else -> Calendar.MONDAY
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "jadwal_kuliah_channel",
                "Jadwal Kuliah",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifikasi pengingat jadwal kuliah"
            }
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }
}