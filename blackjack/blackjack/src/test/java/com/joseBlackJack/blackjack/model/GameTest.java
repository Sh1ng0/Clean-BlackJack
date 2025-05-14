package com.joseBlackJack.blackjack.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class GameTest {


    private Game game;
    private Deck mockDeck;
    private Player mockPlayer;
    private Dealer mockDealer;

    @BeforeEach
    void setUp() {

        game = new Game();


        mockDeck = mock(Deck.class);
        mockDealer = mock(Dealer.class);
        mockPlayer = mock(Player.class);


        game.setPlayerForTesting(mockPlayer);
        game.setDealerForTesting(mockDealer);
        game.setDeckForTesting(mockDeck); // Inyectar mock
        game.setStateForTesting(Game.GameState.WAITING_TO_START);
    }


    // START GAME

    @Test
    void startGame_ShouldInitializeGame_WhenInWaitingState() {


        doNothing().when(mockDeck).dealCards(any(AbstractPlayer.class), anyInt());


        game.startGame();


        verify(mockDeck).initializeDeck();
        verify(mockDeck).shuffle();
        verify(mockDeck).dealCards(any(Player.class), eq(2)); // Usamos any() y eq()
        verify(mockDeck).dealCards(any(Dealer.class), eq(2));
        assertEquals(Game.GameState.PLAYER_TURN, game.getState());
    }


    @Test
    void startGame_InitializesGame_WhenInWaitingState() {

        mockDeck = mock(Deck.class);
        game = new Game();
        game.setDeckForTesting(mockDeck);


        doNothing().when(mockDeck).dealCards(any(AbstractPlayer.class), anyInt());


        game.startGame();


        verify(mockDeck).initializeDeck();
        verify(mockDeck).shuffle();
        verify(mockDeck).dealCards(game.getPlayer(), 2);
        verify(mockDeck).dealCards(game.getDealer(), 2);
        assertEquals(Game.GameState.PLAYER_TURN, game.getState());
    }


    // PLAY DEALER HAND

    @Test
    void playDealerHand_ShouldStopAfterBust() {

        when(mockDealer.calculateHandValue())
                .thenReturn(16)
                .thenReturn(22); // Bust

        when(mockDeck.dealCard())
                .thenReturn(new Card(Card.Suit.SPADES, Card.Rank.KING));


        game.playDealerHand();


        verify(mockDeck, times(1)).dealCard(); // Solo 1 carta antes del bust
    }


    @Test
    void playDealerHand_ShouldDrawCardsUntil17OrHigher() {

        when(mockDealer.calculateHandValue())
                .thenReturn(16)
                .thenReturn(17);

        Card mockCard = new Card(Card.Suit.HEARTS, Card.Rank.ACE);
        when(mockDeck.dealCard()).thenReturn(mockCard);


        game.playDealerHand();


        verify(mockDealer, times(2)).calculateHandValue();
        verify(mockDeck, times(1)).dealCard();
        verify(mockDealer, times(1)).receiveCard(mockCard);
    }

    @Test
    void playDealerHand_ShouldNotDrawWhenHandIs17OrHigher() {

        when(mockDealer.calculateHandValue()).thenReturn(17);


        game.playDealerHand();


        verify(mockDealer, atLeastOnce()).calculateHandValue();
        verify(mockDeck, never()).dealCard();
    }


    // DETERMINE WINNER

    @Test
    void determineWinner_PlayerBust_DealerWins() {

        when(mockPlayer.isBust()).thenReturn(true);


        Game.GameResult result = game.determineWinner();


        assertEquals(Game.GameResult.DEALER_WINS, result);
    }

    @Test
    void determineWinner_DealerBust_PlayerWins() {

        when(mockDealer.isBust()).thenReturn(true);


        Game.GameResult result = game.determineWinner();


        assertEquals(Game.GameResult.PLAYER_WINS, result);
    }

    @Test
    void determineWinner_PlayerHigherValue_PlayerWins() {

        when(mockPlayer.calculateHandValue()).thenReturn(20);
        when(mockDealer.calculateHandValue()).thenReturn(18);


        Game.GameResult result = game.determineWinner();


        assertEquals(Game.GameResult.PLAYER_WINS, result);
    }

    @Test
    void determineWinner_DealerHigherValue_DealerWins() {

        when(mockPlayer.calculateHandValue()).thenReturn(18);
        when(mockDealer.calculateHandValue()).thenReturn(20);


        Game.GameResult result = game.determineWinner();


        assertEquals(Game.GameResult.DEALER_WINS, result);
    }

    @Test
    void determineWinner_EqualValue_Push() {

        when(mockPlayer.calculateHandValue()).thenReturn(18);
        when(mockDealer.calculateHandValue()).thenReturn(18);


        Game.GameResult result = game.determineWinner();


        assertEquals(Game.GameResult.PUSH, result);
    }


    // IS GAME OVER


    @Test
    void isGameOver_ShouldReturnTrue_WhenGameStateIsOver(){

       game.setStateForTesting(Game.GameState.GAME_OVER);

       boolean result = game.isGameOver();

       assertTrue(result);

    }

    @Test
    void isGameOver_ShouldReturnFalse_WhenGameStateIsNotOver(){

        game.setStateForTesting(Game.GameState.PLAYER_TURN);

        boolean result = game.isGameOver();

        assertFalse(result);
    }


    // HIT

    @Test
    void playerHit_ShouldAddCard_WhenPlayerTurn() {
        // Arrange
        game.setStateForTesting(Game.GameState.PLAYER_TURN);
        Card mockCard = new Card(Card.Suit.HEARTS, Card.Rank.QUEEN);
        when(mockDeck.dealCard()).thenReturn(mockCard);
        when(mockPlayer.isBust()).thenReturn(false);

        // Act
        game.playerHit();

        // Assert
        verify(mockPlayer).receiveCard(mockCard);
        assertEquals(Game.GameState.PLAYER_TURN, game.getState()); // Estado no cambia
    }

    @Test
    void playerHit_ShouldEndGame_WhenPlayerBusts() {
        // Arrange
        game.setStateForTesting(Game.GameState.PLAYER_TURN);
        Card mockCard = new Card(Card.Suit.SPADES, Card.Rank.KING);
        when(mockDeck.dealCard()).thenReturn(mockCard);
        when(mockPlayer.isBust()).thenReturn(true);

        // Act
        game.playerHit();

        // Assert
        verify(mockPlayer).receiveCard(mockCard);
        assertEquals(Game.GameResult.DEALER_WINS, game.getResult());
        assertEquals(Game.GameState.GAME_OVER, game.getState());
    }

    @Test
    void playerHit_ShouldDoNothing_WhenNotPlayerTurn() {
        // Arrange
        game.setStateForTesting(Game.GameState.DEALER_TURN); // Estado incorrecto

        // Act
        game.playerHit();

        // Assert
        verifyNoInteractions(mockDeck);
        verifyNoInteractions(mockPlayer);
        assertEquals(Game.GameState.DEALER_TURN, game.getState());
    }






}
