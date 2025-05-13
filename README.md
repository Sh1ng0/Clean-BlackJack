Practice project for security and testing

# Blackjack Microservices Project (Concise Blueprint)

## **Core Stack**
- **Language**: Java 17
- **Framework**: Spring Boot 3
- **Gateway**: APISIX (JWT injection)
- **DB**: PostgreSQL (Workbench)
- **Testing**: JUnit 5, Mockito

## **Microservices Architecture**
┌─────────────┐ ┌─────────────┐ ┌─────────────┐
│ auth-svc │ │ game-core │ │ bet-svc │
│ (JWT/auth) │ ←→ │ (game logic)│ ←→ │ (bets SQL) │
└─────────────┘ └─────────────┘ └─────────────┘
↑ ↑ ↑
└───────────────────┴───────────┬───────┘
↓
┌───────────────┐
│ APISIX │
│ (API Gateway) │
└───────────────┘
↑
│
┌─────────────┐
│ Client │
└─────────────┘

## **Key Decisions**
1. **game-core**:
   - Pure stateless logic (except active games in memory)
   - Exposes REST endpoints for game actions
   - Publishes events to Kafka (game results)

2. **bet-svc**:
   - Handles all money transactions
   - Connects to PostgreSQL (transactions history)

3. **auth-svc**:
   - JWT generation/validation
   - User credentials storage

## **First Milestone**
- [ ] Finish `game-core` MVP (1v1 gameplay)
- [ ] Basic APISIX routing
- [ ] Dockerize all services

## **Testing guidelines**
- [ ] No reflection in the form of the "Field" class
- [ ] We use package private testing (Classes will be modified and comentated accordingly)

