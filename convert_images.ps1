# Script PowerShell para convertir imágenes a hexadecimal para MySQL
$imagePath = "temp_images"
$outputFile = "event_images_real.sql"

$images = @(
    @{file="adolescentesGrupal.jpg"; eventId=1; newName="taller_juegos_mesa_1.jpg"},
    @{file="alumnosOrdenador.jpg"; eventId=1; newName="taller_juegos_mesa_2.jpg"},
    @{file="ninaFicha.jpg"; eventId=2; newName="escuela_padres_1.jpg"},
    @{file="ninoCascos.jpg"; eventId=2; newName="escuela_padres_2.jpg"}
)

Write-Host "Convirtiendo imágenes a formato hexadecimal para MySQL..."

$sqlValues = @()

foreach ($img in $images) {
    $fullPath = Join-Path $imagePath $img.file
    Write-Host "Procesando: $($img.file)"
    
    if (Test-Path $fullPath) {
        $bytes = [System.IO.File]::ReadAllBytes($fullPath)
        $hex = [System.BitConverter]::ToString($bytes) -replace "-", ""
        
        # Limitar tamaño para que MySQL pueda manejarlo (max 16MB pero usaremos menos)
        if ($hex.Length -gt 500000) {
            Write-Host "  Imagen muy grande ($($hex.Length) chars), truncando..."
            $hex = $hex.Substring(0, 500000)
        }
        
        $sqlValue = "  ( '$($img.newName)', UNHEX('$hex'), $($img.eventId) )"
        $sqlValues += $sqlValue
    } else {
        Write-Host "ERROR: No se encontró $fullPath"
    }
}

$sqlFinal = $sqlValues -join ",`n"
$sqlFinal += ";"

# Crear el archivo SQL
$content = @"
-- INSERCIONES REALES para event_images con imágenes del proyecto
INSERT INTO event_images (image_name, image_data, event_id)
VALUES
$sqlFinal
"@

$content | Out-File -FilePath $outputFile -Encoding UTF8

Write-Host ""
Write-Host "SQL generado en: $outputFile"
Write-Host "Archivos procesados: $($sqlValues.Count)"
