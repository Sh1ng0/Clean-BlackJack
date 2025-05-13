package com.joseBlackJack.blackjack.model;

import java.util.ArrayList;
import java.util.List;

public class Game {

    public enum GameState {
        WAITING_TO_START,
        PLAYER_TURN,
        DEALER_TURN,
        GAME_OVER
    }

    private List<Player> players;
    private Dealer dealer;
    private Deck deck;
    private GameState state;
    private int currentPlayerIndex;

    public Game() {
        this.players = new ArrayList<>();
        this.dealer = new Dealer();
        this.deck = new Deck();
        this.state = GameState.WAITING_TO_START;
        this.currentPlayerIndex = 0;
    }

    // Métodos para inicializar juego, repartir cartas, avanzar turno, etc.

    // Ejemplo: iniciar partida
    public void startGame() {
        deck.initializeDeck();
        deck.shuffle();
        // Repartir 2 cartas a cada jugador y al dealer
        for (Player p : players) {
            deck.dealCards(p, 2);
        }
        deck.dealCards(dealer, 2);

        state = GameState.PLAYER_TURN;
        currentPlayerIndex = 0;  // empieza el primer jugador
    }

    // Avanzar turno a siguiente jugador o dealer
    public void nextTurn() {
        if (state != GameState.PLAYER_TURN) return;

        currentPlayerIndex++;
        if (currentPlayerIndex >= players.size()) {
            state = GameState.DEALER_TURN;
            // Aquí lógicas para dealer jugar
        }
    }

    // Otros métodos para acciones (pide carta, planta...), calcular ganador, etc.

    // Añadir jugador para escalar
    public void addPlayer(Player player) {
        players.add(player);
    }
}
