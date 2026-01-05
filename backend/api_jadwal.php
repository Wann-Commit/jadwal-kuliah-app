<?php
include 'config.php';

$method = $_SERVER['REQUEST_METHOD'];

switch($method) {
    case 'GET':
        getJadwal();
        break;
    case 'POST':
        addJadwal();
        break;
    case 'PUT':
        updateJadwal();
        break;
    case 'DELETE':
        deleteJadwal();
        break;
    default:
        echo json_encode(['success' => false, 'message' => 'Method not allowed']);
}

// Get semua jadwal
function getJadwal() {
    global $conn;
    $user_id = isset($_GET['user_id']) ? $_GET['user_id'] : 1;
    
    $sql = "SELECT * FROM jadwal WHERE user_id = $user_id ORDER BY 
            FIELD(hari, 'Senin', 'Selasa', 'Rabu', 'Kamis', 'Jumat', 'Sabtu', 'Minggu'), 
            jam_mulai";
    $result = mysqli_query($conn, $sql);
    
    $jadwal = array();
    while($row = mysqli_fetch_assoc($result)) {
        $jadwal[] = $row;
    }
    
    echo json_encode(['success' => true, 'data' => $jadwal]);
}

// Tambah jadwal
function addJadwal() {
    global $conn;
    $data = json_decode(file_get_contents('php://input'), true);
    
    $nama_mk = mysqli_real_escape_string($conn, $data['nama_mk']);
    $hari = mysqli_real_escape_string($conn, $data['hari']);
    $jam_mulai = mysqli_real_escape_string($conn, $data['jam_mulai']);
    $jam_selesai = mysqli_real_escape_string($conn, $data['jam_selesai']);
    $ruangan = mysqli_real_escape_string($conn, $data['ruangan']);
    $nm_dosen = mysqli_real_escape_string($conn, $data['nm_dosen']);
    $reminder = isset($data['reminder']) ? ($data['reminder'] ? 1 : 0) : 1;
    $user_id = isset($data['user_id']) ? $data['user_id'] : 1;
    
    $sql = "INSERT INTO jadwal (nama_mk, hari, jam_mulai, jam_selesai, ruangan, nm_dosen, reminder, user_id) 
            VALUES ('$nama_mk', '$hari', '$jam_mulai', '$jam_selesai', '$ruangan', '$nm_dosen', $reminder, $user_id)";
    
    if(mysqli_query($conn, $sql)) {
        $id_jadwal = mysqli_insert_id($conn);
        echo json_encode(['success' => true, 'message' => 'Jadwal berhasil ditambahkan', 'id_jadwal' => $id_jadwal]);
    } else {
        echo json_encode(['success' => false, 'message' => 'Gagal menambahkan jadwal: ' . mysqli_error($conn)]);
    }
}

// Update jadwal
function updateJadwal() {
    global $conn;
    $data = json_decode(file_get_contents('php://input'), true);
    
    $id_jadwal = $data['id_jadwal'];
    $nama_mk = mysqli_real_escape_string($conn, $data['nama_mk']);
    $hari = mysqli_real_escape_string($conn, $data['hari']);
    $jam_mulai = mysqli_real_escape_string($conn, $data['jam_mulai']);
    $jam_selesai = mysqli_real_escape_string($conn, $data['jam_selesai']);
    $ruangan = mysqli_real_escape_string($conn, $data['ruangan']);
    $nm_dosen = mysqli_real_escape_string($conn, $data['nm_dosen']);
    $reminder = isset($data['reminder']) ? ($data['reminder'] ? 1 : 0) : 1;
    
    $sql = "UPDATE jadwal SET 
            nama_mk='$nama_mk', 
            hari='$hari', 
            jam_mulai='$jam_mulai', 
            jam_selesai='$jam_selesai', 
            ruangan='$ruangan', 
            nm_dosen='$nm_dosen', 
            reminder=$reminder 
            WHERE id_jadwal=$id_jadwal";
    
    if(mysqli_query($conn, $sql)) {
        echo json_encode(['success' => true, 'message' => 'Jadwal berhasil diupdate']);
    } else {
        echo json_encode(['success' => false, 'message' => 'Gagal mengupdate jadwal: ' . mysqli_error($conn)]);
    }
}

// Delete jadwal
function deleteJadwal() {
    global $conn;
    $data = json_decode(file_get_contents('php://input'), true);
    $id_jadwal = $data['id_jadwal'];
    
    $sql = "DELETE FROM jadwal WHERE id_jadwal=$id_jadwal";
    
    if(mysqli_query($conn, $sql)) {
        echo json_encode(['success' => true, 'message' => 'Jadwal berhasil dihapus']);
    } else {
        echo json_encode(['success' => false, 'message' => 'Gagal menghapus jadwal: ' . mysqli_error($conn)]);
    }
}
?>