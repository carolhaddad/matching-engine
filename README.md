# Matching Engine

Este projeto estrutura uma matching engine simples para negociação de um único ativo, suportando diferentes tipos de ordens, execução de trades e manutenção de prioridade preço-tempo.

---

## Funcionalidades

- Suporte a ordens Limit, Market e Peg
- Operações de Modify e Cancel
- Algoritmo de matching com prioridade preço-tempo
- Execução parcial de ordens
- Order Book com acesso ao melhor bid/ask
- Interface de linha de comando (CLI)
- Testes automatizados com JUnit 5

---

## Comandos Aceitos

limit buy `price` `qty`  
limit sell `price` `qty`   
limit bid buy `price` `qty`  
limit ask sell `price` `qty`  
market buy `qty`  
market sell `qty`  
peg bid buy `qty`  
peg ask sell `qty`  
cancel order `id`  
modify order `id`  
print book  
exit  

### Observações
- Comandos são case-insensitive (aceita letras maiúsculas e minúsculas)
- Espaços extras são ignorados
- Entradas inválidas são rejeitadas com mensagem de erro

---

## Regras de Validação

- price: double positivo (internamente tratado como long em centavos)
- qty: inteiro positivo
- id: long válido
- Comandos fora do padrão são rejeitados

---

## Arquitetura do Projeto

domain -> Entidades centrais (Order)  
book -> Estruturas de dados do Order Book  
manager -> Gerenciamento de IDs e Peg Orders   
dto -> Data Transfer Objects para encapsulamento  
services -> Algoritmo de matching e criação de ordens  
facade -> Fachada para acesso ao sistema  
util -> Funções utilitárias de conversão  
app -> Interface de entrada/saída (CLI)  
test -> Testes unitários e de integração (JUnit)  

---

## Exemplo de Uso

limit sell 20 200  
Order created: sell 200 @ 20 id 1  

market buy 150  
Trade executed: price=20, qty=150  

limit sell 10 100  
Order created: sell 100 @ 10 id 2  

cancel order 2  
Order 2 canceled  

---

## Decisões de Design

- Limit e Peg orders podem gerar trade, remanescente entra no livro
- Apenas ordens externas geram trade (modify e peg price update **não** geram trade)
- Preço do trade é o preço da ordem presente no livro
- Peg orders não perdem prioridade temporal ao atualizar preço  
- Modify orders perdem prioridade temporal
- limit bid buy <price> <qty>:
  - Caso o preço informado seja inferior ao melhor bid atual, ele é ajustado para o melhor bid.
- limit ask sell <price> <qty>:
  - Caso o preço informado seja superior ao melhor ask atual,ele é ajustado para o melhor ask.
- Preços representados como long (centavos) internamente para evitar erros de ponto flutuante
- Modify não permite alterar o preço de ordens Peg
- Order Book implementado com TreeMap<Long, PriorityQueue<Order>> para otimização
- Market orders geram IDs, mas não entram no livro
- Uso do Facade Pattern para orquestrar o sistema
- Peg bid buy acompanha o melhor bid
- Peg ask sell acompanha o melhor ask
- Se não houver referência no livro, o preço inicial é 0

---

## Estruturas de Dados

- **TreeMap** para níveis de preço
- **PriorityQueue** para time-priority
- **HashMap** para obtenção de ordens por ID

---

## Análise de Complexidade

- Inserção de ordens Limit/Peg: O(log N)
- Execução de matching: O(log N)
- Cancelamento e Modificação:
  - Obtençao de ordens por ID: O(1)
  - Remoção do heap: O(N)

---

## Testes

Testes unitários, de integração e de erro com JUnit 5, cobrindo:

- Algoritmo de matching
- Prioridade temporal e de preço
- Adicão e remoção ao livro corretamente
- Cancelamento
- Modificação de ordens
- Peg orders
- Execução parcial
- Limit sem trade
- Market com quantidade maior que o livro
- Atualização de Peg
- Casos de erro e validações

---

## Tecnologias Utilizadas

- Java 17
- Maven
- JUnit 5

---

## Como rodar
mvn clean compile  
mvn exec:java

---

## Como testar
mvn test

---

## Autoria

**Carolina Haddad**  
04/02/2026 – 11/02/2026