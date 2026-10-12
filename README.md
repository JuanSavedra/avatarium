# Avatarium

Mod de personalização de personagem para o **Hytale**. Você importa o seu próprio corpo base, modelado no Blockbench, e monta um guarda-roupa para ele: cabelos, camisetas, calças, tênis e acessórios. No jogo, troca as peças num menu com setinhas ← →, estilo criação de personagem de RPG, e a escolha fica salva.

> Status: template inicial. O plugin carrega e registra o comando `/avatarium`.

## Requisitos

- **JDK 25** ([Adoptium](https://adoptium.net/) ou JetBrains Runtime)
- **IntelliJ IDEA** (recomendado)
- **Blockbench** com o plugin do Hytale, para os modelos

## Rodando

```powershell
.\gradlew.bat setupHytaleDev   # primeira vez
.\gradlew.bat runServer        # sobe o servidor local com o mod
.\gradlew.bat build            # gera o .jar em build/libs
```

Na primeira execução, o servidor pode pedir para autorizar a sua conta Hytale.

## Conceitos

- **Character Pack:** um corpo base e as partes modeladas para ele, que compartilham a mesma estrutura de ossos.
- **Slot:** uma categoria de peça (cabelo, camiseta, calça...), **obrigatória** ou **opcional**.
- **Loadout:** a combinação escolhida pelo jogador, salva e aplicada ao entrar.

Detalhes de arquitetura e regras do projeto estão no [CLAUDE.md](CLAUDE.md).

## Créditos

Baseado no [HytaleModding/plugin-template](https://github.com/HytaleModding/plugin-template), com o [Hytale Gradle Plugin](https://github.com/AzureDoom/Hytale-Gradle-Plugin) de AzureDoom.

## Licença

MIT
