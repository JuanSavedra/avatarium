# CLAUDE.md — Avatarium

Contexto e regras para trabalhar neste projeto. Leia antes de qualquer alteração.

## O que é o mod

**Avatarium** é um mod de **personalização de personagem** para o **Hytale**. O jogador joga com um **personagem próprio, modelado por ele**, e o veste num menu de estilo RPG.

1. O jogador cria um **corpo base** no Blockbench: um corpo "sem roupa", com roupa íntima, que serve de manequim.
2. Ele modela as **partes** desse corpo: cabelos, camisetas, calças, tênis, acessórios de mão etc.
3. Corpo e partes formam um **Character Pack**. Cada pack define seus **slots** e as **regras** de cada slot.
4. No jogo, um **menu de personalização com setinhas ← →** (estilo criação de personagem de RPG) troca as opções de cada slot.
5. A escolha é **salva por jogador** e aplicada sempre que ele entra.

### Regras dos slots
- Cada slot é **obrigatório** ou **opcional**, conforme o Character Pack. Exemplo: camiseta e calça obrigatórias, cabelo opcional (o personagem pode ser careca).
- Slot obrigatório **nunca fica vazio**: o pack define uma **opção padrão** para ele.
- Slot opcional tem a opção **"nenhum"** no ciclo das setinhas.
- O pack define o **visual padrão** do personagem, aplicado na primeira vez e ao resetar.

### Restrição de modelagem
Todas as partes de um pack precisam usar a **mesma hierarquia e os mesmos nomes de ossos** do corpo base, senão não encaixam. O mod deve **validar** isso ao carregar o pack e reportar erros claros (ex.: "camiseta_02 referencia o osso `Chest2`, que não existe no corpo base").

## Escopo e limites conhecidos

- **O modding do Hytale é feito no servidor.** O jogador não consegue mandar arquivos do PC dele para o jogo; os modelos precisam estar num asset pack instalado no servidor.
  - **v1 (alvo atual):** single player e servidores onde o dono instala os Character Packs em `mods/`.
  - **Futuro:** upload de modelos pelo jogador em multiplayer (site ou comando → servidor gera o asset → recarrega ao vivo). Exige moderação e validação de segurança, e fica fora do escopo até a v1 estar pronta.
- O mod [Wardrobe](https://www.curseforge.com/hytale/mods/wardrobe) resolve um problema parecido e serve de **referência** para estudar como trocar o modelo do jogador e anexar partes. Ele é **AGPLv3**: **não copie código dele**, para o Avatarium manter a própria licença (MIT).

## Stack

- **Java 25** (JDK 25; JetBrains Runtime recomendado para hot reload), **Gradle** via wrapper (`gradlew.bat`).
- Gradle plugin **`com.azuredoom.hytale-tools`**: gera o manifest, prepara o ambiente e roda o servidor local.
- API do servidor: `com.hypixel.hytale:Server`, do Maven da Hytale.
- Modelos e animações: **Blockbench** com o plugin oficial do Hytale (`.blockymodel`).
- Projeto baseado no [HytaleModding/plugin-template](https://github.com/HytaleModding/plugin-template).

Comandos (Windows):

```powershell
.\gradlew.bat setupHytaleDev     # prepara o ambiente de dev (primeira vez)
.\gradlew.bat runServer          # sobe o servidor local com o mod
.\gradlew.bat build              # gera o .jar em build/libs
.\gradlew.bat updatePluginManifest  # regenera manifest.json a partir do gradle.properties
```

A identidade do mod (nome, id, versão, autor, classe principal, faixa de versão do servidor) fica no **`gradle.properties`**, que é a fonte da verdade. Não edite o `manifest.json` à mão: altere o `gradle.properties` e rode `updatePluginManifest`.

**A verificar:** `manifestServerVersion` veio do template como `>=0.5.3 <0.6.0`. Confirme a versão atual do servidor depois do `setupHytaleDev` e ajuste, senão o servidor pode recusar o mod.

## Arquitetura e organização

O objetivo é **não precisar refatorar o tempo todo**. Pense na estrutura antes de escrever, e prefira estender a remendar.

### Pacotes (em `src/main/java/dev/juansavedra/avatarium/`)

| Pacote | Responsabilidade |
|---|---|
| (raiz) `AvatariumPlugin` | Entry point. **Só registra** serviços, comandos e eventos. Nada de lógica nele |
| `pack/` | Modelo de dados do Character Pack: corpo base, slots, regras, partes, visual padrão. Carregamento e **validação** |
| `loadout/` | A escolha atual do jogador (`Loadout`), as regras de troca (ciclo das setinhas, obrigatório/opcional) e a persistência |
| `appearance/` | Aplica o loadout no jogador do jogo: trocar o modelo base, anexar e remover partes |
| `ui/` | Menu de personalização (setinhas, preview, salvar, resetar) |
| `commands/` | Comandos (`/avatarium` etc.) |
| `config/` | Configuração do servidor (codecs de config) |
| `events/` | Handlers de eventos do jogo (jogador entrou, saiu etc.) que delegam para os serviços |

Só crie um pacote quando houver código para ele.

### Regras de código
- **`pack/` e `loadout/` não dependem da API do Hytale** sempre que possível: são Java puro, testáveis com JUnit sem subir servidor. A ponte com o jogo fica em `appearance/`, `ui/` e `events/`.
- **Um tipo público por arquivo.** Classes `PascalCase`, métodos e campos `camelCase`, constantes `UPPER_SNAKE_CASE`, pacotes em minúsculas.
- Identificadores, comentários e Javadoc em **inglês**. Documentação (README, CLAUDE.md) em **português**.
- Dependências entram pelo construtor. **Evite estado estático global** (o template usa `static` para config; aqui não fazemos isso).
- Use `@Nonnull` e `@Nullable` (`javax.annotation`) nas assinaturas públicas, como a API do Hytale.
- Prefira `record` para dados imutáveis (definições de slot, partes, loadout salvo).
- Erros de pack inválido viram **mensagens claras** para o criador do pack, nunca stack traces soltos.
- Sem números ou strings mágicas espalhadas: ids de slot, caminhos de asset e limites ficam em constantes ou no próprio pack.
- Não deixe código morto, TODOs vagos ou classes "Utils" genéricas.
- Se a API do Hytale não estiver documentada, consulte as fontes decompiladas que o `setupHytaleDev` gera (com Javadocs injetados) antes de supor assinaturas. **Não invente métodos da API.**

## Regras de trabalho para o Claude

1. **Nunca faça commit** (nem `git add`, `git commit` ou `git push`). Ao terminar uma alteração, **sugira** uma mensagem de commit no final da resposta, no formato Conventional Commits (`feat:`, `fix:`, `refactor:`, `chore:`, `docs:`...), com a descrição **em português (pt-BR)**. Ex.: `feat: adiciona ciclo de opções por slot`.
2. **Arquivos de texto em CRLF**, **exceto `gradlew`**, que é script de shell e precisa de LF (com CRLF ele quebra no Linux e no CI). Depois de criar ou editar arquivos, converta. Exemplo em PowerShell:
   ```powershell
   Get-ChildItem -Recurse -File -Force -Include *.java,*.kts,*.properties,*.json,*.md,*.yml,*.bat,.gitignore,.gitattributes |
     Where-Object { $_.FullName -notmatch '\\(build|\.gradle|run)\\' } |
     ForEach-Object { $t = [IO.File]::ReadAllText($_.FullName); $n = $t -replace "`r?`n", "`r`n"; if ($n -ne $t) { [IO.File]::WriteAllText($_.FullName, $n) } }
   ```
3. **Respeite a arquitetura acima.** Se uma tarefa não se encaixar nela, proponha a mudança de estrutura antes e atualize este arquivo, em vez de improvisar.
4. Rode `.\gradlew.bat build` depois das alterações e reporte erros e avisos novos. Se o JDK 25 não estiver disponível, diga isso em vez de afirmar que compila.
5. Mantenha este CLAUDE.md atualizado quando uma decisão de projeto mudar (formato do Character Pack, persistência, UI, escopo).
