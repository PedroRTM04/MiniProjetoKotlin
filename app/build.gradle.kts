plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android") version "2.0.0"
    id("kotlin-kapt") // Para usar o KAPT com RooM
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.0"
}

android {
    namespace = "com.example.miniprojetokotlin2" // Atualize o namespace com o nome correto do seu projeto
    compileSdk = 34 // Atualize conforme necessário

    defaultConfig {
        applicationId = "com.example.MiniProjetoKotlin2" // Verifique se o applicationId está correto
        minSdk = 21
        targetSdk = 34 // Atualize conforme necessário
        versionCode = 1
        versionName = "1.0"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }

    buildFeatures {
        compose = true // Habilita o Compose
    }

    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}" // Excluir arquivos desnecessários
        }
    }
}

dependencies {

    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    // Outras dependências do Compose
    testImplementation ("junit:junit:4.13.2")
    androidTestImplementation ("androidx.test.ext:junit:1.1.5")
    androidTestImplementation ("androidx.test.espresso:espresso-core:3.5.1")

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.10.0") // Retrofit
    implementation("com.squareup.retrofit2:converter-gson:2.10.0") // Conversão JSON

    // Room
    implementation("androidx.room:room-runtime:2.6.1") // Room Runtime
    implementation("androidx.room:room-ktx:2.6.1") // Extensões do Room para Kotlin
    kapt("androidx.room:room-compiler:2.6.1") // Compilador Room

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.2") // Coroutines para Android

    // Jetpack Compose
    implementation("androidx.compose.ui:ui:1.6.0") // Compose UI
    implementation("androidx.compose.material3:material3:1.2.0-alpha11") // Material 3
    implementation("androidx.compose.ui:ui-tooling-preview:1.6.0") // Ferramentas de preview

    // Navegação e ViewModel
    implementation("androidx.navigation:navigation-compose:2.7.5") // Navegação no Compose
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0") // ViewModel Compose

    // Dependências de suporte
    implementation("androidx.core:core-ktx:1.12.0") // Extensões Kotlin para Android
    implementation("androidx.appcompat:appcompat:1.7.0") // Compatibilidade com AppCompat
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0") // Lifecycle com Kotlin
}

kapt {
    correctErrorTypes = true // Corrigir tipos de erro do KAPT
}