package com.example.miniprojetokotlin2

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileWriter
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.Text

class AddressResultActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Recuperando os dados passados pela Intent
        val cep = intent.getStringExtra("CEP") ?: "N/A"
        val logradouro = intent.getStringExtra("LOGRADOURO") ?: "N/A"
        val bairro = intent.getStringExtra("BAIRRO") ?: "N/A"
        val localidade = intent.getStringExtra("LOCALIDADE") ?: "N/A"
        val uf = intent.getStringExtra("UF") ?: "N/A"

        setContent {
            AddressResultScreen(
                cep = cep,
                logradouro = logradouro,
                bairro = bairro,
                localidade = localidade,
                uf = uf
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddressResultScreen(
    cep: String,
    logradouro: String,
    bairro: String,
    localidade: String,
    uf: String
) {
    val context = LocalContext.current  // Obter o contexto atual

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalhes do Endereço") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Text(text = "CEP: $cep", style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Logradouro: $logradouro", style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Bairro: $bairro", style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Localidade: $localidade", style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "UF: $uf", style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(16.dp))

            // Botão de exportação
            Button(onClick = {
                exportToCSV(context, cep, logradouro, bairro, localidade, uf)
            }) {
                Text("Exportar para CSV")
            }
        }
    }
}

// Função para exportar dados em formato CSV e compartilhar
fun exportToCSV(context: Context, cep: String, logradouro: String, bairro: String, localidade: String, uf: String) {
    val csvFile = File(context.filesDir, "endereco.csv") // Criação do arquivo CSV no diretório interno
    FileWriter(csvFile).use { writer ->
        writer.append("CEP,Logradouro,Bairro,Localidade,UF\n")
        writer.append("$cep,$logradouro,$bairro,$localidade,$uf\n")
    }

    // Obter URI do arquivo usando FileProvider
    val uri: Uri = FileProvider.getUriForFile(context, "${context.packageName}.provider", csvFile)

    // Intent para compartilhar o arquivo CSV
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/csv"
        putExtra(Intent.EXTRA_STREAM, uri)
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION) // Permissão para leitura do arquivo compartilhado
    }

    // Abrir menu de compartilhamento
    context.startActivity(Intent.createChooser(intent, "Compartilhar Endereço"))
}

@Preview(showBackground = true)
@Composable
fun AddressResultScreenPreview() {
    AddressResultScreen(
        cep = "12345678",
        logradouro = "Rua Exemplo",
        bairro = "Centro",
        localidade = "Cidade Exemplo",
        uf = "SP"
    )
}
