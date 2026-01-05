# Backend API - PHP

REST API untuk Aplikasi Jadwal Kuliah

## Setup

### 1. Install XAMPP
Download dan install XAMPP dari [https://www.apachefriends.org/](https://www.apachefriends.org/)

### 2. Copy File ke htdocs
Copy semua file dari folder `backend/` ke:
```
C:\xampp\htdocs\api_jadwal\
```

### 3. Import Database
Lihat instruksi di folder `database/`

### 4. Konfigurasi
Edit `config.php` jika perlu:
- Ganti password MySQL jika ada
- Ganti nama database jika berbeda

### 5. Test API
- Start Apache & MySQL di XAMPP
- Buka browser: `http://localhost/api_jadwal/api_jadwal.php`
- Harus muncul: `{"success":true,"data":[]}`

## API Documentation

### Get All Jadwal
**GET** `/api_jadwal.php`

**Response:**
```json
{
  "success": true,
  "data": [
    {
      "id_jadwal": 1,
      "nama_mk": "Pemrograman Mobile",
      "hari": "Senin",
      "jam_mulai": "08:00",
      "jam_selesai": "10:30",
      "ruangan": "Lab 301",
      "nm_dosen": "Dr. Budi",
      "reminder": true
    }
  ]
}
```

### Add Jadwal
**POST** `/api_jadwal.php`

**Body:**
```json
{
  "nama_mk": "Pemrograman Mobile",
  "hari": "Senin",
  "jam_mulai": "08:00",
  "jam_selesai": "10:30",
  "ruangan": "Lab 301",
  "nm_dosen": "Dr. Budi",
  "reminder": true
}
```

### Update Jadwal
**PUT** `/api_jadwal.php`

**Body:**
```json
{
  "id_jadwal": 1,
  "nama_mk": "Updated",
  ...
}
```

### Delete Jadwal
**DELETE** `/api_jadwal.php`

**Body:**
```json
{
  "id_jadwal": 1
}
```

## Tech Stack
- PHP 7.4+
- MySQL 5.7+
- Apache 2.4+