package com.example.miniprojetokotlin2

import android.content.Intent
import androidx.core.content.FileProvider
import java.io.File
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.example.miniprojetokotlin.ui.theme.MiniProjetoKotlinTheme
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.fillMaxSize


class MainActivity : ComponentActivity() {

    // Configurações Retrofit e Room
    private lateinit var db: AddressDatabase
    private lateinit var api: ViaCepApi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializa o Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl("https://viacep.com.br/ws/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        api = retrofit.create(ViaCepApi::class.java)

        // Inicializa o Room Database
        db = Room.databaseBuilder(
            applicationContext,
            AddressDatabase::class.java, "address_db"
        ).build()

        setContent {
            MiniProjetoKotlinTheme {
                val snackbarHostState = remember { SnackbarHostState() }
                val coroutineScope = rememberCoroutineScope()

                Scaffold(
                    snackbarHost = { SnackbarHost(snackbarHostState) },
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    AddressInputScreen(
                        modifier = Modifier.padding(innerPadding),
                        onShowSnackbar = { message ->
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar(message)
                            }
                        },
                        onFetchAndStoreAddress = { cep ->  // Passa a função aqui
                            fetchAndStoreAddress(cep)
                        }
                    )
                }
            }
        }
    }

    // Função para consultar e salvar o endereço
    private fun fetchAndStoreAddress(cep: String) {
        if (!cep.matches(Regex("\\d{8}"))) {
            Toast.makeText(
                this@MainActivity,
                "CEP inválido! Deve conter 8 números.",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        lifecycleScope.launch {
            try {
                val response = api.getAddress(cep)
                if (response.isSuccessful && response.body() != null) {
                    val address = response.body()!!

                    // Salva no banco de dados Room
                    val entity = AddressEntity(
                        cep = address.cep,
                        logradouro = address.logradouro ?: "N/A",
                        bairro = address.bairro ?: "N/A",
                        localidade = address.localidade ?: "N/A",
                        uf = address.uf ?: "N/A"
                    )
                    db.addressDao().insert(entity)

                    // Inicia a nova Activity com os dados obtidos
                    val intent =
                        Intent(this@MainActivity, AddressResultActivity::class.java).apply {
                            putExtra("CEP", address.cep)
                            putExtra("LOGRADOURO", address.logradouro ?: "N/A")
                            putExtra("BAIRRO", address.bairro ?: "N/A")
                            putExtra("LOCALIDADE", address.localidade ?: "N/A")
                            putExtra("UF", address.uf ?: "N/A")
                        }
                    startActivity(intent)

                } else {
                    Toast.makeText(
                        this@MainActivity,
                        "CEP não encontrado ou inválido.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@MainActivity, "Erro: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

        // Função para exportar os dados para CSV
    private fun exportDataAsCSV() {
        lifecycleScope.launch {
            val addresses = db.addressDao().getAllAddresses()
            if (addresses.isEmpty()) {
                Toast.makeText(this@MainActivity, "Nenhum endereço salvo para exportar.", Toast.LENGTH_SHORT).show()
                return@launch
            }

            val csvHeader = "CEP,Logradouro,Bairro,Localidade,UF\n"
            val csvData = StringBuilder(csvHeader)

            addresses.forEach { address ->
                csvData.append("${address.cep},${address.logradouro},${address.bairro},${address.localidade},${address.uf}\n")
            }

            try {
                val fileName = "enderecos_exportados.csv"
                val file = File(applicationContext.getExternalFilesDir(null), fileName)

                file.writeText(csvData.toString())  // Escreve os dados no arquivo

                Toast.makeText(this@MainActivity, "Exportado para $fileName", Toast.LENGTH_LONG).show()

                // Opcional: Compartilhar arquivo após exportação
                shareFile(file)
            } catch (e: Exception) {
                Toast.makeText(this@MainActivity, "Erro ao exportar: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    // Função para compartilhar o arquivo
    fun shareFile(file: File) {
        val uri = FileProvider.getUriForFile(
            this,
            "com.example.meuprojeto.provider",  // Insira seu applicationId aqui
            file
        )

        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/csv"  // Ou "text/plain" dependendo do tipo de arquivo
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        startActivity(Intent.createChooser(shareIntent, "Compartilhar arquivo"))
    }
}


@Composable
fun AddressInputScreen(
    modifier: Modifier = Modifier,
    onShowSnackbar: (String) -> Unit = {},
    onFetchAndStoreAddress: (String) -> Unit = {} // Função de callback
) {
    var cep by remember { mutableStateOf(TextFieldValue("")) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(
            value = cep,
            onValueChange = { cep = it },
            label = { Text("Digite o CEP") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (cep.text.isNotEmpty()) {
                    onShowSnackbar("Consultando CEP: ${cep.text}") // Mostra a snackbar
                    onFetchAndStoreAddress(cep.text)  // Chama a função para buscar e salvar o endereço
                } else {
                    onShowSnackbar("Por favor, insira um CEP")
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Consultar Endereço")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddressInputScreenPreview() {
    MiniProjetoKotlinTheme {
        AddressInputScreen()
    }
}


@Composable
fun AddressResultScreen(
    address: AddressEntity?,
    modifier: Modifier = Modifier
) {
    if (address != null) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("CEP: ${address.cep}", style = MaterialTheme.typography.headlineSmall) // Substituir h6 por headlineSmall
            Text("Logradouro: ${address.logradouro}", style = MaterialTheme.typography.bodyLarge) // body1 pode ser bodyLarge
            Text("Bairro: ${address.bairro}", style = MaterialTheme.typography.bodyLarge)
            Text("Localidade: ${address.localidade}", style = MaterialTheme.typography.bodyLarge)
            Text("UF: ${address.uf}", style = MaterialTheme.typography.bodyLarge)
        }
    } else {
        Text("Nenhum endereço encontrado", style = MaterialTheme.typography.headlineSmall)
    }
}