# Database Setup

Database untuk Aplikasi Jadwal Kuliah dengan Reminder

## Setup Database

### 1. Buat Database
Buka phpMyAdmin atau MySQL console, jalankan:
```sql
CREATE DATABASE jadwal_kuliah_db;
```

### 2. Import SQL File
- Buka phpMyAdmin
- Pilih database `jadwal_kuliah_db`
- Klik tab **Import**
- Klik **Choose File** dan pilih file `jadwal_kuliah_db.sql`
- Klik **Go**

Atau via command line:
```bash
mysql -u root -p jadwal_kuliah_db < jadwal_kuliah_db.sql
```

### 3. Struktur Tabel

**Tabel: jadwal**
- `id_jadwal` (INT, PRIMARY KEY, AUTO_INCREMENT)
- `nama_mk` (VARCHAR 255)
- `hari` (VARCHAR 20)
- `jam_mulai` (VARCHAR 10)
- `jam_selesai` (VARCHAR 10)
- `ruangan` (VARCHAR 100)
- `nm_dosen` (VARCHAR 255)
- `reminder` (BOOLEAN)
- `user_id` (INT)
- `created_at` (TIMESTAMP)
- `updated_at` (TIMESTAMP)

## Konfigurasi

Edit file `config.php` di folder API:
```php
$host = 'localhost';
$username = 'root';
$password = ''; // Isi jika ada password
$database = 'jadwal_kuliah_db';
```

## API Endpoint

Base URL: `http://localhost/api_jadwal/`

- **GET** `/api_jadwal.php` - Ambil semua jadwal
- **POST** `/api_jadwal.php` - Tambah jadwal baru
- **PUT** `/api_jadwal.php` - Update jadwal
- **DELETE** `/api_jadwal.php` - Hapus jadwal