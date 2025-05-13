package com.joseBlackJack.blackjack.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;



class GameTest {

    // With Reflection magic


    private Game game;
    private Deck mockDeck;
    private Player player;
    private Dealer dealer;

//    @BeforeEach
//    void setUp() {
//        // Creamos mocks y objetos reales
//        mockDeck = mock(Deck.class);
//        player = new Player();
//        dealer = new Dealer();
//
//        // Inyectamos dependencias usando reflexión (para no modificar Game)
//        game = new Game();
//        setPrivateField(game, "deck", mockDeck);
//        setPrivateField(game, "player", player);
//        setPrivateField(game, "dealer", dealer);
//    }
//
//    // Metdo helper para inyección de dependencias en campos privados
//    private void setPrivateField(Object target, String fieldName, Object value) {
//        try {
//            Field field = target.getClass().getDeclaredField(fieldName);
//            field.setAccessible(true);
//            field.set(target, value);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }

    @Test
    void startGame_ShouldInitializeGame_WhenInWaitingState() {
        // Arrange
        game.setStateForTesting(Game.GameState.WAITING_TO_START);

        // Act
        game.startGame();

        // Assert
        verify(mockDeck).initializeDeck();
        verify(mockDeck).shuffle();
        verify(mockDeck).dealCards(player, 2);
        verify(mockDeck).dealCards(dealer, 2);
        assertEquals(Game.GameState.PLAYER_TURN, game.getState());
    }

    @Test
    void startGame_InitializesGame_WhenInWaitingState() {
        // Arrange
        mockDeck = mock(Deck.class);
        game = new Game();
        game.setDeckForTesting(mockDeck); // Inyectamos mock
        game.setStateForTesting(Game.GameState.WAITING_TO_START);

        // Act
        game.startGame();

        // Assert
        verify(mockDeck).initializeDeck();
        verify(mockDeck).shuffle();
        verify(mockDeck).dealCards(game.getPlayer(), 2);
        verify(mockDeck).dealCards(game.getDealer(), 2);
        assertEquals(Game.GameState.PLAYER_TURN, game.getState());
    }
}
