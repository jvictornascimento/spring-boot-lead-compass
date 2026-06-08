# Lead Finder Backend Context

Este documento registra o mapa funcional e arquitetural do backend do Lead Finder. Ele deve orientar a implementação incremental do projeto e manter alinhamento com o frontend criado no Lovable.

O backlog ágil otimizado para implementação com TDD está em `docs/agile-stories.md`.

## Stack Planejada

- Java 21
- Spring Boot 3.x como referência de arquitetura; o projeto atual está em Spring Boot 4.0.6
- Spring Web MVC
- Spring Data JPA
- Spring Security
- Spring Validation
- PostgreSQL
- Flyway
- Lombok
- MapStruct
- OpenFeign ou WebClient
- Spring Scheduler
- Docker
- Swagger/OpenAPI with `org.springdoc:springdoc-openapi-starter-webmvc-ui`
- RabbitMQ ou Kafka futuramente
- Redis futuramente

## Integrações Futuras

- Google Places API
- Google Maps API
- OpenAI API
- WhatsApp via links `wa.me`
- SMTP, Resend ou SendGrid

## Módulos Backend

Organizar o backend por domínio dentro do pacote base `com.jvictornascimento.leadCompass`:

- `auth`
- `dashboard`
- `searches`
- `leads`
- `diagnostics`
- `campaigns`
- `messages`
- `offers`
- `followups`
- `interactions`
- `exports`
- `settings`

## Entidades Principais

### Lead

Campos: `id`, `businessName`, `niche`, `city`, `state`, `address`, `phone`, `whatsapp`, `website`, `instagram`, `googleMapsUrl`, `googlePlaceId`, `rating`, `reviewCount`, `hasWebsite`, `hasHttps`, `isResponsive`, `isSiteSlow`, `opportunityScore`, `status`, `duplicate`, `lostReason`, `createdAt`, `updatedAt`.

Status: `NEW`, `QUALIFIED`, `MESSAGE_GENERATED`, `CONTACTED`, `RESPONDED`, `MEETING_SCHEDULED`, `PROPOSAL_SENT`, `CLOSED`, `LOST`, `DISCARDED`.

Lost reasons: `HAS_PROVIDER`, `NO_INTEREST`, `NO_RESPONSE`, `PRICE_TOO_HIGH`, `NOT_FIT`, `INVALID_PHONE`, `COMPANY_CLOSED`, `OTHER`.

### DigitalDiagnosis

Campos: `id`, `leadId`, `type`, `severity`, `description`.

Types: `NO_WEBSITE`, `SLOW_SITE`, `NO_HTTPS`, `NOT_RESPONSIVE`, `NO_INSTAGRAM`, `FEW_REVIEWS`, `LOW_RATING`, `NO_WHATSAPP_BUTTON`, `OLD_VISUAL_SITE`, `LOW_DIGITAL_PRESENCE`.

Severity: `POSITIVE`, `WARNING`, `CRITICAL`.

### Search

Campos: `id`, `niche`, `city`, `state`, `radiusKm`, `maxResults`, `onlyWithoutWebsite`, `includeBadWebsite`, `includeInstagram`, `status`, `totalResults`, `withoutWebsiteCount`, `createdAt`.

Status: `PENDING`, `RUNNING`, `COMPLETED`, `FAILED`.

### Campaign

Campos: `id`, `name`, `niche`, `city`, `service`, `channel`, `tone`, `template`, `status`, `createdAt`.

Channel: `WHATSAPP`, `EMAIL`, `BOTH`.

Tone: `DIRECT`, `CONSULTATIVE`, `INFORMAL`.

Status: `DRAFT`, `ACTIVE`, `PAUSED`, `FINISHED`.

### GeneratedMessage

Campos: `id`, `leadId`, `campaignId`, `content`, `status`, `createdAt`.

Status: `GENERATED`, `APPROVED`, `REJECTED`, `SENT`.

### Offer

Campos: `id`, `name`, `description`, `priceRange`, `estimatedTime`, `category`, `proposalTemplate`.

### Proposal

Campos: `id`, `leadId`, `offerId`, `problemFound`, `suggestedSolution`, `benefits`, `priceRange`, `estimatedTime`, `nextSteps`, `createdAt`.

### FollowUp

Campos: `id`, `leadId`, `scheduledAt`, `status`, `generatedMessage`, `createdAt`.

Status: `PENDING`, `COMPLETED`, `LATE`, `CANCELED`.

### Interaction

Campos: `id`, `leadId`, `type`, `description`, `createdAt`.

Types: `LEAD_CREATED`, `MESSAGE_GENERATED`, `MESSAGE_APPROVED`, `WHATSAPP_OPENED`, `FOLLOWUP_GENERATED`, `STATUS_CHANGED`, `NOTE_ADDED`, `PROPOSAL_GENERATED`.

## Endpoints MVP

Implementar primeiro estes endpoints para conectar bem com o frontend:

- `GET /api/dashboard/summary`
- `GET /api/leads`
- `GET /api/leads/{id}`
- `PATCH /api/leads/{id}/status`
- `POST /api/searches`
- `GET /api/searches`
- `POST /api/leads/{id}/generate-message`
- `POST /api/campaigns`
- `GET /api/campaigns`
- `GET /api/offers`
- `POST /api/offers`
- `GET /api/followups`

## Endpoints Por Domínio

### Dashboard

- `GET /api/dashboard/summary`

Retorna totais de leads, leads sem site, sites ruins, qualificados, contactados, convertidos, taxa de resposta, taxa de conversão, campanhas ativas e follow-ups pendentes.

### Searches

- `POST /api/searches`
- `GET /api/searches`
- `GET /api/searches/{id}`

Ao criar uma busca: salvar `Search` como `PENDING`, mudar para `RUNNING`, buscar empresas na API externa ou mock inicial, criar leads, rodar diagnóstico, calcular score, deduplicar e finalizar como `COMPLETED` ou `FAILED`.

### Leads

- `GET /api/leads`
- `GET /api/leads/{id}`
- `PATCH /api/leads/{id}/status`
- `PATCH /api/leads/{id}/lost`
- `POST /api/leads/{id}/notes`
- `PATCH /api/leads/{id}/merge`

Filtros esperados em `GET /api/leads`: `city`, `niche`, `status`, `withoutWebsite`, `minScore`.

Na mesclagem, o lead principal mantém os dados, o duplicado é marcado como mesclado ou inativo, e as interações são transferidas.

### Diagnóstico Digital

- `POST /api/leads/{id}/diagnose`

Analisar se o site existe, tem HTTPS, responde, é lento, possui Instagram, possui WhatsApp, tem poucas reviews, nota baixa ou baixa presença digital.

### Score De Oportunidade

- `POST /api/leads/{id}/calculate-score`

Regra inicial:

- Sem site: `+40`
- Site lento: `+15`
- Sem HTTPS: `+10`
- Sem Instagram: `+10`
- Poucas avaliações: `+10`
- Nota baixa: `+5`
- Sem WhatsApp: `+5`
- Baixa presença digital: `+10`

Pontuação máxima: `100`.

### Campaigns

- `POST /api/campaigns`
- `GET /api/campaigns`
- `GET /api/campaigns/{id}`
- `POST /api/campaigns/{id}/generate-messages`

Ao gerar mensagens de campanha: buscar campanha e leads, aplicar template ou IA, criar `GeneratedMessage`, marcar lead como `MESSAGE_GENERATED` e registrar interação `MESSAGE_GENERATED`.

### Messages

- `POST /api/leads/{id}/generate-message`
- `PATCH /api/generated-messages/{id}/approve`
- `PATCH /api/generated-messages/{id}/reject`
- `PATCH /api/generated-messages/{id}/sent`
- `GET /api/generated-messages/{id}/whatsapp-link`

O link WhatsApp deve retornar uma URL no formato `https://wa.me/5519999999999?text=Mensagem%20codificada`.

### Offers

- `GET /api/offers`
- `POST /api/offers`

### Proposals

- `POST /api/leads/{id}/generate-proposal`

Retornar problema encontrado, solução sugerida, benefícios, faixa de preço, prazo estimado e próximos passos.

### Follow-Ups

- `GET /api/followups`
- `GET /api/followups?status=PENDING`
- `POST /api/leads/{id}/follow-up`
- `PATCH /api/followups/{id}/complete`

### Exports

- `GET /api/leads/export?format=csv`
- `GET /api/leads/export?format=xlsx`

Bibliotecas planejadas: OpenCSV para CSV e Apache POI para Excel.

### Settings

- `GET /api/settings`
- `PUT /api/settings`

Configurações: `companyName`, `commercialWhatsapp`, `messageSignature`, `dailyContactLimit`, `businessHourStart`, `businessHourEnd`, `blockOutsideBusinessHours`, `avoidRepeatedContact`.

## Serviços Internos

- `LeadSearchService`: buscar empresas, salvar leads e relacionar busca com leads.
- `GooglePlacesService`: consultar Google Places API e converter retorno externo para DTO interno.
- `LeadDeduplicationService`: comparar telefone, `googlePlaceId`, endereço e nome parecido; marcar duplicados.
- `DigitalDiagnosisService`: verificar site, HTTPS, lentidão, Instagram, WhatsApp e gerar diagnósticos.
- `OpportunityScoreService`: calcular score de `0` a `100`.
- `MessageGenerationService`: gerar mensagens por template e futuramente por IA.
- `ProposalService`: gerar mini proposta comercial.
- `FollowUpService`: criar follow-ups, marcar atrasados e sugerir mensagens.
- `InteractionService`: registrar histórico do lead.

## Ordem Ideal De Desenvolvimento

1. Criar projeto Spring Boot.
2. Configurar PostgreSQL.
3. Configurar Flyway.
4. Criar entidades e enums.
5. Criar repositories.
6. Criar DTOs.
7. Criar CRUD de leads.
8. Criar CRUD de searches.
9. Criar mocks de busca.
10. Criar diagnóstico digital.
11. Criar score.
12. Criar deduplicação.
13. Criar pipeline e status.
14. Criar campanhas.
15. Criar geração de mensagem.
16. Criar ofertas.
17. Criar propostas.
18. Criar follow-ups.
19. Criar exportação CSV/XLSX.
20. Integrar Google Places API.
21. Integrar IA.
