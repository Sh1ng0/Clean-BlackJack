package com.joseBlackJack.blackjack.model;


import lombok.Getter;


public class Game {

    public enum GameState {
        WAITING_TO_START,
        PLAYER_TURN,
        DEALER_TURN,
        GAME_OVER,

    }

    public enum GameResult {
        PLAYER_WINS,
        DEALER_WINS,
        PUSH
    }

    @Getter private Player player;
    @Getter private Dealer dealer;
    private Deck deck;
    @Getter private GameState state;
    @Getter private GameResult result;


    public Game() {
        this.player = new Player();
        this.dealer = new Dealer();
        this.deck = new Deck();
        this.state = GameState.WAITING_TO_START;
        this.result = null;

    }


    // LOGIC

    public void startGame() {
        if (state == GameState.WAITING_TO_START) {
            deck.initializeDeck();
            deck.shuffle();
            // Repartir 2 cartas al jugador y al dealer
            deck.dealCards(player, 2);
            deck.dealCards(dealer, 2);
            state = GameState.PLAYER_TURN;
        }
    }


    private void playDealerHand() {
        while (dealer.calculateHandValue() < 17) {  // Regla: dealer pide hasta 16
            dealer.receiveCard(deck.dealCard());
        }
    }

    public GameResult determineWinner() {
        if (player.isBust()) {
            return GameResult.DEALER_WINS;
        } else if (dealer.isBust()) {
            return GameResult.PLAYER_WINS;
        } else if (player.calculateHandValue() > dealer.calculateHandValue()) {
            return GameResult.PLAYER_WINS;
        } else if (player.calculateHandValue() < dealer.calculateHandValue()) {
            return GameResult.DEALER_WINS;
        } else {
            return GameResult.PUSH;
        }
    }

    public boolean isGameOver() {
        return state == GameState.GAME_OVER;
    }


    // ACTIONS

    public void playerHit() {
        if (state == GameState.PLAYER_TURN) {
            player.receiveCard(deck.dealCard());
            if (player.isBust()) {
                result = GameResult.DEALER_WINS;
                state = GameState.GAME_OVER;
            }
        }
    }

    public void playerStand() {
        if (state == GameState.PLAYER_TURN) {
            state = GameState.DEALER_TURN;
            playDealerHand();
            result = determineWinner();
            state = GameState.GAME_OVER;
        }
    }

    public void playerDouble() {
        if (state == GameState.PLAYER_TURN && player.getHand().size() == 2) {
            player.receiveCard(deck.dealCard());
            if (player.isBust()) {
                result = GameResult.DEALER_WINS;
            } else {
                state = GameState.DEALER_TURN;
                playDealerHand();
                result = determineWinner();
            }
            state = GameState.GAME_OVER;
        }
    }
}





