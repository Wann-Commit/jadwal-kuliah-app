# Aplikasi Jadwal Kuliah dengan Reminder

Aplikasi Android untuk mengatur jadwal mata kuliah dengan fitur reminder otomatis.

## Features
- ✅ CRUD Jadwal Kuliah
- ✅ Reminder 10 menit sebelum kuliah
- ✅ Reminder saat kuliah dimulai
- ✅ Sync dengan database MySQL
- ✅ Bottom Navigation Bar
- ✅ Material Design

## Tech Stack
- **Android:** Kotlin, Retrofit, RecyclerView, Material Components
- **Backend:** PHP, MySQL
- **Server:** XAMPP

## Setup

### 1. Clone Repository
```bash
git clone https://github.com/username/jadwal-kuliah-app.git
cd jadwal-kuliah-app
```

### 2. Setup Database
Lihat instruksi lengkap di folder `database/README.md`

**Quick setup:**
```bash
# Buat database
mysql -u root -p -e "CREATE DATABASE jadwal_kuliah_db"

# Import SQL
mysql -u root -p jadwal_kuliah_db < database/jadwal_kuliah_db.sql
```

### 3. Setup Backend API
Lihat instruksi lengkap di folder `backend/README.md`

**Quick setup:**
- Copy folder `backend/` ke `C:\xampp\htdocs\api_jadwal\`
- Start XAMPP (Apache & MySQL)
- Test: http://localhost/api_jadwal/api_jadwal.php

### 4. Setup Android App
- Buka project di Android Studio
- Sync Gradle
- Edit `ApiService.kt`:
    - Emulator: `http://10.0.2.2/api_jadwal/`
    - HP Fisik: `http://[IP_KOMPUTER]/api_jadwal/`
- Build & Run

## Screenshots
(Tambahkan screenshot aplikasi di sini)

## Developer
Nama Anda - [GitHub](https://github.com/username)

## License
MIT License