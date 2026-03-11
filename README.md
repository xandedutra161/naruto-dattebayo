# DattebayoApp

Aplicativo Android nativo para explorar personagens do universo Naruto e gerenciar favoritos com persistencia local.

## Tecnologias principais

- Kotlin como linguagem principal do projeto.
- Jetpack Compose e Material 3 para a interface declarativa.
- Navigation Compose para fluxo entre lista, detalhe e favoritos.
- Koin para injecao de dependencia.
- Retrofit + Gson para consumo e serializacao da API.
- Room para armazenamento local dos favoritos.
- Coroutines + StateFlow para asincronismo e gerenciamento de estado.
- Coil para carregamento de imagens remotas.

## Abordagens de desenvolvimento

- Arquitetura em camadas, com separacao entre `data`, `domain` e `feature`.
- Use cases para concentrar regras de negocio e desacoplar a UI da fonte de dados.
- ViewModels com `StateFlow` para expor estados de loading, sucesso e erro.
- Persistencia local com Room para manter favoritos e reaproveitar dados consultados.
- Mapeamento explicito entre DTOs da API, entidades locais e modelos de dominio.

## Decisoes arquiteturais

- Koin organizado em modulos dedicados para rede, banco, repositorios, use cases e viewmodels.
- Repository como ponto unico de integracao entre API remota e cache local.
- Navegacao declarativa com Compose, incluindo rota parametrizada para detalhes.
- Tratamento explicito de retry e mensagens de erro nos fluxos principais da UI.
- Sincronizacao do estado de favoritos entre listagem, tela de detalhe e armazenamento local.

## Qualidade e testes

- Testes unitarios cobrindo repository, use cases, mapeamentos e ViewModels.
- `MockWebServer` para validar chamadas e parsing na integracao com a API.
- Testes instrumentados com Compose UI para componentes e navegacao principal.
