# 📘 REGRA DE NEGÓCIO

---

# 1️⃣ Separação Estrutural do Domínio

## 🔹 Pedido de Produto

Representado por:

* `pedido`
* `item_pedido`
* `estoque`
* `historico_estoque`

### Características:

* Contém itens físicos (`item_pedido`)
* Cada item está vinculado ao `estoque`
* Movimenta estoque automaticamente
* Não exige `servico`
* Pode ser finalizado diretamente

---

## 🔹 Pedido de Serviço

Representado por:

* `pedido`
* `servico`
* `etapa`
* `agendamento`
* `agendamento_funcionario`
* `agendamento_produto`

### Características:

* Pode ou não ter produto vinculado
* Possui fluxo de etapas (`etapa`)
* Pode gerar movimentação de estoque via `agendamento_produto`
* Depende de agendamento
* Envolve funcionários

---

# 2️⃣ Regra Central de Separação

Um `pedido` pode:

| Tipo              | Condição                          |
| ----------------- | --------------------------------- |
| Pedido de Produto | Possui registros em `item_pedido` |
| Pedido de Serviço | Possui registros em `servico`     |

⚠️ Um pedido pode ter ambos, mas a lógica operacional deve respeitar o tipo dominante.

---

# 3️⃣ Fluxo de Serviço (Baseado na tabela ETAPA)

A tabela `etapa` controla o estado do SERVIÇO.

Fluxo obrigatório:

```
PENDENTE
→ AGUARDANDO ORÇAMENTO
→ ANÁLISE DO ORÇAMENTO
→ ORÇAMENTO APROVADO
→ SERVIÇO AGENDADO
→ SERVIÇO EM EXECUÇÃO
→ CONCLUÍDO
```

---

## 🔒 Regras de Transição

1. Não pode pular etapa
2. Não pode concluir sem estar em execução
3. Não pode executar sem agendamento válido
4. Não pode agendar sem orçamento aprovado (se exigido)

---

# 4️⃣ Regras de Agendamento

Tabela envolvida:

* `agendamento`
* `agendamento_funcionario`
* `agendamento_produto`

---

## 4.1 Tipos de Agendamento

Campo:

```
tipo ENUM('ORCAMENTO','SERVICO')
```

---

### 🔹 ORCAMENTO

* Não exige funcionário obrigatório
* Não movimenta estoque
* Apenas coleta informações

---

### 🔹 SERVICO

Regras obrigatórias:

1. Deve possuir pelo menos 1 funcionário (`agendamento_funcionario`)
2. Deve possuir horário válido:

    * `inicio_agendamento`
    * `fim_agendamento`
3. Não pode haver conflito de agenda
4. Pode reservar produtos (`agendamento_produto`)

---

# 5️⃣ Regra de Conflito de Agenda

Ao agendar SERVIÇO:

O sistema deve verificar:

```
Para cada funcionário selecionado:
    Não pode existir outro agendamento
    onde:
        data_agendamento seja igual
    e
        intervalo (inicio_agendamento, fim_agendamento) conflite
```

Regra matemática:

Existe conflito se:

```
novo_inicio < agendamento_existente.fim
AND
novo_fim > agendamento_existente.inicio
```

---

# 6️⃣ Regra de Estoque

Tabelas envolvidas:

* `estoque`
* `historico_estoque`
* `agendamento_produto`
* `item_pedido`

---

## 6.1 Pedido de Produto

Ao criar `item_pedido`:

1. Validar se `quantidade_disponivel >= quantidade_solicitada`
2. Atualizar:

    * quantidade_total
    * quantidade_disponivel
3. Inserir registro em `historico_estoque`

    * tipo_movimentacao = 'SAIDA'
    * origem = 'PEDIDO'

---

## 6.2 Serviço

Durante agendamento:

* `quantidade_reservada` aumenta
* estoque.reservado aumenta
* estoque.quantidade_disponivel diminui

Ao concluir serviço:

* `quantidade_utilizada` efetiva saída
* registrar em `historico_estoque`

    * tipo_movimentacao = 'SAIDA'
    * origem = 'SERVICO'

---

# 7️⃣ Regra de Consistência Entre Pedido e Serviço

## 🔹 Pedido com Serviço

Se existir `servico.pedido_id`:

* O pedido só pode ser FINALIZADO se:

    * Todos os serviços estiverem em etapa CONCLUÍDO

---

## 🔹 Pedido de Produto

Pode ser FINALIZADO quando:

* Todos `item_pedido` foram processados
* Não houver pendência financeira

---

# 8️⃣ Regra de Funcionário

Tabela: `funcionario`

Condições para ser alocado:

* `ativo = true`
* Não estar em conflito
* (Futuramente pode validar escala)

---

# 9️⃣ Regras de Integridade Críticas

1. ❌ Não pode existir `agendamento` sem `servico`
2. ❌ Não pode existir `agendamento_funcionario` sem `agendamento`
3. ❌ Não pode existir `agendamento_produto` sem estoque suficiente
4. ❌ Não pode alterar etapa para "SERVIÇO EM EXECUÇÃO" sem:

    * agendamento válido
    * funcionário vinculado

---

# 1️⃣1️⃣ Modelo Mental Final do Sistema

```
CLIENTE
   ↓
PEDIDO
   ├── ITEM_PEDIDO → ESTOQUE → HISTORICO_ESTOQUE
   └── SERVICO
         ↓
       ETAPA
         ↓
     AGENDAMENTO
         ├── FUNCIONARIO
         └── PRODUTO (reserva estoque)
```
