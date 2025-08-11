# Script simplificado - solo una imagen por evento
$imagePath = "temp_images"

$images = @(
    @{file="adolescentesGrupal.jpg"; eventId=1; newName="taller_juegos_mesa_1.jpg"},
    @{file="ninaFicha.jpg"; eventId=2; newName="escuela_padres_1.jpg"}
)

$sqlValues = @()

foreach ($img in $images) {
    $fullPath = Join-Path $imagePath $img.file
    
    if (Test-Path $fullPath) {
        $bytes = [System.IO.File]::ReadAllBytes($fullPath)
        $hex = [System.BitConverter]::ToString($bytes) -replace "-", ""
        
        # Usar solo los primeros 1000 caracteres para el ejemplo
        $hex = $hex.Substring(0, [Math]::Min($hex.Length, 1000))
        
        Write-Host "Imagen: $($img.newName) - Hex length: $($hex.Length)"
        
        $sqlValue = "  ( '$($img.newName)', UNHEX('$hex'), $($img.eventId) )"
        $sqlValues += $sqlValue
    }
}

$sqlFinal = $sqlValues -join ",`n"

Write-Host ""
Write-Host "-- SQL para reemplazar en data.sql:"
Write-Host "  ( 'taller_juegos_mesa_1.jpg', UNHEX('$($sqlValues[0].Split("'")[3])'), 1 ),"
Write-Host "  ( 'escuela_padres_1.jpg', UNHEX('$($sqlValues[1].Split("'")[3])'), 2 )"
