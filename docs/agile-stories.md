# Agile Stories And TDD Plan

Este backlog organiza a implementação do Lead Finder em incrementos pequenos, priorizando MVP, baixo acoplamento e ciclos TDD. Para cada story: escrever teste falhando, implementar o mínimo necessário, refatorar, e só então avançar.

## Definição De Pronto

- Código coberto por testes unitários ou integração, conforme risco.
- Endpoint documentado por contrato de request/response.
- Validações de entrada implementadas com mensagens claras.
- Migrações Flyway versionadas quando houver mudança de banco.
- `./mvnw test` executando com sucesso.

## Fluxo De Branch E Commits

- Criar uma branch para cada story antes de iniciar implementação.
- Usar nomes como `story/1-1-lead-model` ou `story/5-3-generate-message`.
- Fazer commits curtos dentro da branch, sempre em inglês simples e Conventional Commits.
- Preferir commits por passo de TDD, por exemplo `test: add lead status tests`, `feat: update lead status`, `refactor: clean lead service`.
- Manter a branch focada apenas na story atual.

## Epic 0: Base Técnica

### Story 0.1: Configurar Ambiente De Persistência

Como desenvolvedor, quero configurar PostgreSQL, profiles e Flyway para que o backend tenha uma base confiável de dados.

Critérios:

- `application.yaml` usa variáveis de ambiente para conexão.
- Existe profile de teste.
- Flyway roda automaticamente.
- Teste de contexto valida inicialização.

### Story 0.2: Criar Estrutura De Pacotes

Como desenvolvedor, quero organizar os pacotes por domínio para manter o projeto escalável.

Critérios:

- Pacotes criados para `leads`, `searches`, `dashboard`, `campaigns`, `messages`, `offers`, `followups`, `interactions`, `diagnostics`, `settings`.
- Cada domínio segue padrão `controller`, `service`, `repository`, `dto`, `mapper`, `model` quando aplicável.

## Epic 1: Leads MVP

### Story 1.1: Criar Modelo De Lead

Como usuário, quero armazenar leads com dados comerciais e digitais para acompanhar oportunidades.

Critérios:

- Entidade `Lead` criada com enums de status e motivo de perda.
- Repository criado.
- Migração Flyway criada.
- Teste repository cobre persistência básica.

### Story 1.2: Listar Leads Com Filtros

Como usuário, quero listar leads por cidade, nicho, status, ausência de site e score mínimo para priorizar contato.

Critérios:

- `GET /api/leads` aceita filtros opcionais.
- Retorno usa DTO, não entidade.
- Testes cobrem filtro isolado e combinação de filtros.

### Story 1.3: Buscar Lead Por ID

Como usuário, quero ver detalhes de um lead para analisar diagnósticos e histórico.

Critérios:

- `GET /api/leads/{id}` retorna lead com diagnósticos e interações.
- Retorna `404` quando inexistente.
- Testes cobrem sucesso e erro.

### Story 1.4: Alterar Status Do Lead

Como usuário, quero alterar o status de um lead para refletir o andamento comercial.

Critérios:

- `PATCH /api/leads/{id}/status` valida status.
- Cria interação `STATUS_CHANGED`.
- Testes cobrem transição válida e lead inexistente.

## Epic 2: Searches E Pipeline De Captura

### Story 2.1: Criar Modelo De Search

Como usuário, quero registrar buscas por nicho e localização para rastrear origem dos leads.

Critérios:

- Entidade `Search` criada com status.
- Migração e repository criados.
- Teste cobre criação com status inicial.

### Story 2.2: Criar Busca Com Mock Inicial

Como usuário, quero criar uma busca e receber leads simulados para validar o fluxo antes da API Google.

Critérios:

- `POST /api/searches` cria busca `PENDING`, processa como `RUNNING` e finaliza `COMPLETED`.
- Gera leads mockados relacionados ao termo buscado.
- Testes cobrem pipeline de sucesso e falha controlada.

### Story 2.3: Listar E Consultar Searches

Como usuário, quero listar buscas e consultar uma busca específica para acompanhar resultados.

Critérios:

- `GET /api/searches` retorna buscas ordenadas por criação.
- `GET /api/searches/{id}` retorna totais e status.
- Testes cobrem lista vazia, lista preenchida e `404`.

## Epic 3: Diagnóstico, Score E Deduplicação

### Story 3.1: Diagnosticar Presença Digital

Como usuário, quero diagnosticar problemas digitais de um lead para entender a oportunidade.

Critérios:

- `POST /api/leads/{id}/diagnose` gera diagnósticos conforme dados disponíveis.
- Diagnósticos anteriores podem ser substituídos ou recalculados de forma determinística.
- Testes cobrem sem site, sem HTTPS, poucas reviews e nota baixa.

### Story 3.2: Calcular Score De Oportunidade

Como usuário, quero um score de 0 a 100 para priorizar leads com maior potencial.

Critérios:

- `POST /api/leads/{id}/calculate-score` aplica a regra inicial.
- Score nunca passa de `100`.
- Testes cobrem cada componente da pontuação.

### Story 3.3: Detectar Duplicados

Como usuário, quero detectar leads duplicados para evitar contatos repetidos.

Critérios:

- Serviço compara telefone, `googlePlaceId`, endereço e nome similar.
- Lead duplicado é marcado sem remover dados.
- Testes cobrem match por telefone, Google Place ID e ausência de duplicidade.

## Epic 4: Dashboard MVP

### Story 4.1: Resumo Do Dashboard

Como usuário, quero visualizar métricas principais para acompanhar o funil comercial.

Critérios:

- `GET /api/dashboard/summary` retorna totais, taxas, campanhas ativas e follow-ups pendentes.
- Taxas lidam corretamente com divisão por zero.
- Testes cobrem cenário vazio e cenário com dados.

## Epic 5: Ofertas, Campanhas E Mensagens

### Story 5.1: CRUD Mínimo De Ofertas

Como usuário, quero cadastrar ofertas para reutilizar em mensagens e propostas.

Critérios:

- `GET /api/offers` lista ofertas.
- `POST /api/offers` cria oferta validada.
- Testes cobrem criação válida e campos obrigatórios.

### Story 5.2: Criar Campanhas

Como usuário, quero criar campanhas por nicho, cidade, serviço, canal e tom.

Critérios:

- `POST /api/campaigns` cria campanha em `DRAFT`.
- `GET /api/campaigns` lista campanhas.
- Testes cobrem validação de canal, tom e template.

### Story 5.3: Gerar Mensagem Individual

Como usuário, quero gerar uma mensagem personalizada para um lead.

Critérios:

- `POST /api/leads/{id}/generate-message` usa oferta, tom e canal.
- Cria `GeneratedMessage`.
- Marca lead como `MESSAGE_GENERATED`.
- Registra interação.

### Story 5.4: Aprovar, Rejeitar E Enviar Mensagem

Como usuário, quero controlar o ciclo de aprovação da mensagem antes do envio.

Critérios:

- Endpoints `approve`, `reject` e `sent` alteram status.
- `sent` registra interação e pode marcar lead como `CONTACTED`.
- Testes cobrem transições válidas e inválidas.

### Story 5.5: Gerar Link WhatsApp

Como usuário, quero abrir o WhatsApp com texto codificado para agilizar contato.

Critérios:

- `GET /api/generated-messages/{id}/whatsapp-link` retorna URL `wa.me`.
- Texto é codificado por URL encoding.
- Testes cobrem telefone ausente e mensagem inexistente.

## Epic 6: Propostas E Follow-Ups

### Story 6.1: Gerar Mini Proposta

Como usuário, quero gerar uma proposta curta baseada no lead e na oferta.

Critérios:

- `POST /api/leads/{id}/generate-proposal` cria proposta.
- Retorno inclui problema, solução, benefícios, preço, prazo e próximos passos.
- Testes cobrem lead sem diagnóstico e oferta inexistente.

### Story 6.2: Criar Follow-Up

Como usuário, quero agendar follow-ups para leads contactados.

Critérios:

- `POST /api/leads/{id}/follow-up` agenda por `daysAfterContact`.
- `GET /api/followups` lista e filtra por status.
- `PATCH /api/followups/{id}/complete` conclui follow-up.

## Epic 7: Exportação E Configurações

### Story 7.1: Exportar Leads

Como usuário, quero exportar leads em CSV e XLSX para trabalhar fora da ferramenta.

Critérios:

- `GET /api/leads/export?format=csv` retorna CSV.
- `GET /api/leads/export?format=xlsx` retorna Excel.
- Testes validam headers e conteúdo básico.

### Story 7.2: Gerenciar Settings

Como usuário, quero configurar dados comerciais e limites de contato.

Critérios:

- `GET /api/settings` retorna configuração atual.
- `PUT /api/settings` atualiza configuração validada.
- Testes cobrem horário comercial e limite diário inválido.

## Epic 8: Integrações Externas

### Story 8.1: Integrar Google Places

Como usuário, quero buscar empresas reais no Google Places para popular leads.

Critérios:

- Client isolado em `GooglePlacesService`.
- Chave vem de variável de ambiente.
- Testes usam mock do client externo.

### Story 8.2: Integrar IA Para Mensagens

Como usuário, quero usar IA para gerar mensagens mais personalizadas.

Critérios:

- Serviço de IA fica atrás de interface.
- Fallback por template continua disponível.
- Testes cobrem fallback quando integração falha.

## Ordem Otimizada De Implementação

1. Epic 0: base técnica.
2. Epic 1: leads MVP.
3. Epic 2: searches com mocks.
4. Epic 3: diagnóstico, score e deduplicação.
5. Epic 4: dashboard.
6. Epic 5: ofertas, campanhas e mensagens.
7. Epic 6: propostas e follow-ups.
8. Epic 7: exportação e settings.
9. Epic 8: integrações reais.

## Estratégia TDD

- Começar por testes de serviço para regras puras.
- Usar testes de controller para contratos HTTP e validações.
- Usar testes de repository somente para queries e persistência relevante.
- Mockar APIs externas desde o início.
- Evitar implementar infraestrutura futura antes de existir uma story que precise dela.
