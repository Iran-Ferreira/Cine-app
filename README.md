# 🎬 CineApp - Aplicativo de Filmes

O **CineApp** é um aplicativo Android desenvolvido em **Kotlin** e **Jetpack Compose** para consulta de filmes obtidos via API pública e gerenciamento de filmes favoritos armazenados localmente.

---

## 📱 Funcionalidades do Aplicativo

1. **Tela Inicial (Menu Principal)**
   - Apresenta o nome do aplicativo (**CineApp**) e cartões de acesso simples para:
     - 🎬 **Lista de Filmes**
     - ❤️ **Filmes Favoritos**

2. **Lista de Filmes**
   - Consome uma API pública (TMDB / Fallback) via **Retrofit**.
   - Exibe em lista (`LazyColumn`) o **título**, **ano de lançamento**, **avaliação (nota)** e **imagem de capa**.
   - Permite clicar em um filme para visualizar seus detalhes completos.

3. **Detalhes do Filme**
   - Apresenta informações detalhadas do filme:
     - Título
     - Ano de Lançamento
     - Gênero(s)
     - Descrição (Sinopse)
     - Avaliação / Nota
   - Botão para **Adicionar aos Favoritos** ou **Remover dos Favoritos** com atualização imediata de estado.

4. **Filmes Favoritos**
   - Armazena os filmes selecionados no banco de dados local **Room Database**, mantendo os dados salvos mesmo após fechar o app.
   - Atualiza a interface do usuário **automaticamente** em tempo real quando ocorrem alterações nos favoritos.
   - Permite remover filmes da lista diretamente ou visualizar seus detalhes.

---

## 🗺️ Fluxo de Navegação

```
[ Tela Inicial ]
       │
       ├───> [ Lista de Filmes ] ───> [ Detalhes do Filme ]
       │                                     │
       └───> [ Filmes Favoritos ] ───────────┴───> (Adicionar / Remover dos Favoritos)
```

---

## 🛠️ Tecnologias Utilizadas

| Tecnologia | Descrição / Utilização |
| :--- | :--- |
| **Kotlin** | Linguagem de programação principal utilizada em todo o desenvolvimento. |
| **Jetpack Compose** | Toolkit moderno e declarativo para construção de interfaces gráficas nativas Android. |
| **Navigation Compose** | Biblioteca responsável por gerenciar a navegação entre telas (`NavHost`, `composable`, `rememberNavController`). |
| **Room Database** | Abstração sobre SQLite para persistência de dados local segura e reativa. |
| **Retrofit** | Cliente HTTP moderno para realizar chamadas de rede e consumo da API de filmes. |
| **Gson** | Biblioteca para serialização e desserialização de JSON nos objetos Kotlin DTO. |
| **Coroutines & Flow** | Manipulação de operações assíncronas de rede e banco de dados sem bloquear a Thread Principal. |
| **ViewModel** | Gerenciamento e retenção de estado da interface com sobrevivência a mudanças de configuração. |
| **StateFlow & collectAsState()** | Fluxo de dados reativo que notifica e atualiza a interface gráfica automaticamente. |
| **Coil Compose** | Carregamento assíncrono de imagens da Web com suporte a cache. |

---

## 🏛️ Estrutura do Projeto

```text
com.example.myapplication/
├── data/
│   ├── local/          # Configuração do Room Database (AppDatabase, MovieDao, MovieEntity)
│   ├── remote/         # Retrofit API Service e DTOs (MovieApiService, MovieDto)
│   └── repository/     # Repositório central de dados (MovieRepository)
├── domain/
│   └── model/          # Modelo de domínio da aplicação (Movie.kt)
├── ui/
│   ├── navigation/     # Roteamento e destinos com Navigation Compose (Screen, AppNavigation)
│   ├── screens/        # Telas da aplicação (HomeScreen, MovieListScreen, MovieDetailScreen, FavoritesScreen)
│   ├── theme/          # Configurações de Tema, Cores e Tipografia Material 3
│   └── viewmodel/      # ViewModels para gerenciamento de estado (MovieListViewModel, MovieDetailViewModel, FavoritesViewModel)
└── MainActivity.kt     # Ponto de entrada da aplicação
```

---

## 🚀 Como Rodar o Projeto

### Pré-requisitos
- **Android Studio** (versão Ladybug 2024.2.1 ou superior recomendada).
- **JDK 11** ou superior configurado.
- Dispositivo Android físico com **Android 7.0 (API 24)** ou superior, ou um **Emulador Android (AVD)**.

### Passo a Passo

1. **Clonar ou abrir o repositório no Android Studio:**
   - Abra o Android Studio.
   - Escolha **Open** e selecione a pasta raiz do projeto (`MyApplication`).

2. **Sincronizar as dependências do Gradle:**
   - Aguarde o Android Studio realizar a sincronização automática das dependências (**Gradle Sync**).
   - Caso a sincronização não ocorra automaticamente, acesse: `File -> Sync Project with Gradle Files`.

3. **Executar o Aplicativo:**
   - Selecione o dispositivo de destino (Emulador ou Dispositivo Físico conectado via USB).
   - Clique no botão **Run 'app'** (ícone do sinal de play verde `▶`) no canto superior direito do Android Studio, ou utilize o atalho `Shift + F10` (`Control + R` no macOS).

4. **Execução via Linha de Comando (Opcional):**
   No terminal, na pasta raiz do projeto, execute:
   ```bash
   ./gradlew installDebug
   ```

---

## 📄 Licença

Projeto desenvolvido para fins acadêmicos.
