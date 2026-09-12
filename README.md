---

## 📝 Meus aprendizados durante o desafio

Fiz um fork deste projeto para praticar testes unitários com JUnit 5 e Mockito, como parte do bootcamp da DIO.

### O que pratiquei
- Escrever testes unitários com JUnit 5
- Usar Mockito para simular dependências (`@Mock` e `@InjectMocks`)
- Validar exceções com `assertThrows`
- Verificar chamadas com `verify(objeto, times(n))`
- Aplicar o ciclo do TDD: Red → Green → Refactor

### Testes adicionais que criei
Além dos testes do projeto original, criei mais 6 testes em `MeusTestesBeerServiceTest.java`:
- Validar exceção ao buscar cerveja com nome inexistente
- Validar listagem de cervejas cadastradas
- Verificar se o repositório é chamado ao buscar por nome
- Validar exceção ao cadastrar cerveja já existente
- Validar incremento de estoque com sucesso
- Validar exceção ao incrementar estoque acima do máximo

### Resultado

Tests run: 27, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS


### Dificuldades que enfrentei
- Compatibilidade entre Spring Boot 2.3.0, Lombok e Java 25 (resolvi usando Java 11)
- Entender quando usar `@Mock` e quando usar `@Spy`
- Configurar corretamente os matchers (`any`, `times`, etc.)