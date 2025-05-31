# setup-secrets.ps1

Write-Host "Setting up Kubernetes secrets for Watchflix..."

# Function to encode a string to base64
function Encode-Secret {
    param ([string]$value)
    return [Convert]::ToBase64String([System.Text.Encoding]::UTF8.GetBytes($value))
}

# Prompt user for secrets
$SECRET_KEY_ATLAS     = Read-Host "SECRET_KEY_ATLAS"
$VIRUSTOTAL_API_KEY   = Read-Host "VIRUSTOTAL_API_KEY"
$AZUREBLOB_API_KEY    = Read-Host "AZUREBLOB_API_KEY"
$SECRET_KEY_AES       = Read-Host "SECRET_KEY_AES"
$SECRET_KEY_NEON      = Read-Host "SECRET_KEY_NEON"
$SECRET_KEY_SUPABASE  = Read-Host "SECRET_KEY_SUPABASE"
$JWT_SECRET           = Read-Host "JWT_SECRET"

# Create the YAML file with base64-encoded secrets
$yamlContent = @"
apiVersion: v1
kind: Secret
metadata:
  name: watchflix-secrets
  namespace: watchflix
type: Opaque
data:
  SECRET_KEY_ATLAS: $(Encode-Secret $SECRET_KEY_ATLAS)
  VIRUSTOTAL_API_KEY: $(Encode-Secret $VIRUSTOTAL_API_KEY)
  AZUREBLOB_API_KEY: $(Encode-Secret $AZUREBLOB_API_KEY)
  SECRET_KEY_AES: $(Encode-Secret $SECRET_KEY_AES)
  SECRET_KEY_NEON: $(Encode-Secret $SECRET_KEY_NEON)
  SECRET_KEY_SUPABASE: $(Encode-Secret $SECRET_KEY_SUPABASE)
  JWT_SECRET: $(Encode-Secret $JWT_SECRET)
"@

# Write the YAML to a file
$yamlContent | Set-Content -Encoding UTF8 -Path "secretsnew.yaml"

Write-Host "`nSecrets file created as secrets-filled.yaml"
Write-Host "Apply it with: kubectl apply -f secrets-filled.yaml"
