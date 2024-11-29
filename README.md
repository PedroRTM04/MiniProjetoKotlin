📘 Aplicativo Consulta CEP - Kotlin
Este é um aplicativo Android desenvolvido em Kotlin que permite ao usuário consultar informações de endereço fornecendo um CEP (Código de Endereçamento Postal) brasileiro. O aplicativo utiliza a API pública ViaCEP para obter os dados e exibe os resultados na interface.

🚀 Funcionalidades
Consulta de CEP: O usuário insere um CEP e obtém informações detalhadas sobre o endereço.
Exibição de Dados: Mostra informações como logradouro, bairro, localidade e UF.
Armazenamento Local: (Opcional) Os resultados das consultas podem ser armazenados em um banco de dados SQLite local usando Room.
Exportação de Dados: (Opcional) Permite exportar os dados armazenados em formatos como texto ou CSV.
🛠️ Tecnologias Utilizadas
Linguagem: Kotlin
API de Consulta: ViaCEP
Banco de Dados: Room 
Interface Gráfica: XML para layouts
Gerenciamento de Dependências: Gradle com build.gradle.kts
📲 Como Usar o Aplicativo
Abra o Aplicativo: Inicie o app no seu dispositivo Android.
Digite o CEP: Insira um CEP válido no campo de texto.
Consultar: Clique no botão "Buscar" para consultar os detalhes do endereço.
Visualização: O endereço completo será exibido na tela, incluindo:
CEP: 04067-031
Logradouro: Alameda dos Uapês
Bairro: Planalto Paulista
Localidade: São Paulo
UF: SP

📦 Instalação e Configuração
Pré-requisitos
Android Studio instalado
Kotlin configurado no projeto
Conexão com a internet para consumir a API
Clone o Projeto
bash
Copiar código
git clone https://github.com/SeuRepositorio/ConsultaViaCEP.git
cd ConsultaViaCEP
Dependências (build.gradle.kts)
Certifique-se de incluir as seguintes dependências:

kotlin
Copiar código
dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.6.21")
    implementation("androidx.core:core-ktx:1.7.0")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
}

Desenvolvido por Pedro Ricardo, João Henrique e Luis Eduardo Negromonte usando Kotlin. 🚀










