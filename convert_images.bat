@echo off
echo Convirtiendo imagenes a formato hexadecimal para MySQL...

set "IMAGE_PATH=d:\GITHUB\ÁGORA\Proyecto Personal\agora_frontend\public\images\img"
set "OUTPUT_FILE=event_images_real.sql"

echo -- INSERCIONES REALES para event_images con imagenes del proyecto > %OUTPUT_FILE%
echo INSERT INTO event_images (image_name, image_data, event_id) >> %OUTPUT_FILE%
echo VALUES >> %OUTPUT_FILE%

echo Procesando imagenes...

powershell -Command "& {
    $imagePath = 'd:\GITHUB\ÁGORA\Proyecto Personal\agora_frontend\public\images\img'
    $outputFile = 'event_images_real.sql'
    
    $images = @(
        @{file='adolescentesGrupal.jpg'; eventId=1; newName='taller_juegos_mesa_1.jpg'},
        @{file='alumnosOrdenador.jpg'; eventId=1; newName='taller_juegos_mesa_2.jpg'},
        @{file='niñaFicha.jpg'; eventId=2; newName='escuela_padres_1.jpg'},
        @{file='niñoCascos.jpg'; eventId=2; newName='escuela_padres_2.jpg'}
    )
    
    $sqlValues = @()
    
    foreach ($img in $images) {
        $fullPath = Join-Path $imagePath $img.file
        Write-Host 'Procesando:' $img.file
        
        if (Test-Path $fullPath) {
            $bytes = [System.IO.File]::ReadAllBytes($fullPath)
            $hex = [System.BitConverter]::ToString($bytes) -replace '-', ''
            
            # Limitar tamaño para que MySQL pueda manejarlo
            if ($hex.Length -gt 200000) {
                Write-Host '  Imagen muy grande, truncando para demo...'
                $hex = $hex.Substring(0, 200000)
            }
            
            $sqlValue = '  ( ''' + $img.newName + ''', UNHEX(''' + $hex + '''), ' + $img.eventId + ' )'
            $sqlValues += $sqlValue
        } else {
            Write-Host 'ERROR: No se encontro' $fullPath
        }
    }
    
    $sqlFinal = $sqlValues -join ',`n'
    $sqlFinal += ';'
    
    # Sobrescribir el archivo con el contenido completo
    '-- INSERCIONES REALES para event_images con imagenes del proyecto' | Out-File -FilePath $outputFile -Encoding UTF8
    'INSERT INTO event_images (image_name, image_data, event_id)' | Add-Content -Path $outputFile -Encoding UTF8
    'VALUES' | Add-Content -Path $outputFile -Encoding UTF8
    $sqlFinal | Add-Content -Path $outputFile -Encoding UTF8
    
    Write-Host ''
    Write-Host '✅ SQL generado en:' $outputFile
}"

echo.
echo Conversion completada. Revisa el archivo event_images_real.sql
