package com.example.hangmangamejavafx;

import com.example.hangmanjavafx.HangmanGame;
import com.example.hangmanjavafx.HangmanGame.GuessResult;
import com.example.hangmanjavafx.HangmanGame.WordResult;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class GameController implements Initializable {

    @FXML private Text asciiBoard;
    @FXML private Text maskedWord;
    @FXML private Text statusText;
    @FXML private Text usedLettersText;
    @FXML private Text triedWordsText;
    @FXML private TextField input;
    @FXML private Button guessBtn;
    @FXML private Button playBtn;

    private HangmanGame game;

    private final List<String> stages = Arrays.asList(
            """
            +---+
            |   |
                |
                |
                |
                |
            =========""",
            """
            +---+
            |   |
            O   |
                |
                |
                |
            =========""",
            """
            +---+
            |   |
            O   |
            |   |
                |
                |
            =========""",
            """
            +---+
            |   |
            O   |
           /|   |
                |
                |
            =========""",
            """
            +---+
            |   |
            O   |
           /|\\  |
                |
                |
            =========""",
            """
            +---+
            |   |
            O   |
           /|\\  |
           /    |
                |
            =========""",
            """
            +---+
            |   |
            O   |
           /|\\  |
           / \\  |
                |
            ========="""
    );

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setIdleUI();
        asciiBoard.setText(stages.get(0));
        maskedWord.setText("");
        statusText.setText("Press Play to start");
        usedLettersText.setText("Used letters: ");
        triedWordsText.setText("");

        input.setOnAction(e -> onGuess());
        guessBtn.setOnAction(e -> onGuess());
        playBtn.setOnAction(e -> startNewGame());
    }

    private void startNewGame() {
        game = new HangmanGame();
        statusText.setText("Good luck!");
        input.clear();
        usedLettersText.setText("Used letters: ");
        triedWordsText.setText("");
        guessBtn.setDisable(false);
        input.setDisable(false);
        playBtn.setDisable(true);
        playBtn.setText("Play again");
        render();
    }

    private void onGuess() {
        if (game == null) return;

        String txt = input.getText().trim();
        if (txt.isEmpty()) return;

        if (txt.length() == 1) {
            char c = txt.charAt(0);
            GuessResult r = game.guessLetter(c);
            switch (r) {
                case HIT -> statusText.setText("Good guess!");
                case MISS -> statusText.setText("Wrong letter!");
                case REPEATED -> statusText.setText("You already tried that letter.");
                case INVALID -> statusText.setText("Please enter a letter A–Z.");
            }
        } else {
            WordResult r = game.guessWord(txt);
            switch (r) {
                case CORRECT -> statusText.setText("You won!");
                case WRONG_NEW -> statusText.setText("Wrong word!");
                case WRONG_REPEAT -> statusText.setText("You already tried that word.");
                case INVALID -> statusText.setText("Only letters A–Z allowed.");
            }
        }

        input.clear();
        render();
    }

    private void render() {
        maskedWord.setText(game.maskedWithSpaces());

        int used = 6 - game.getLives();
        int idx = Math.min(Math.max(used, 0), stages.size() - 1);
        asciiBoard.setText(stages.get(idx));

        usedLettersText.setText("Used letters: " + game.getUsedLettersString());
        String tried = game.getTriedWordsString();
        triedWordsText.setText(tried.isBlank() ? "" : "Tried words: " + tried);

        if (game.isWon()) {
            statusText.setText("You won!");
            endGameUI();
        } else if (game.isLost()) {
            statusText.setText("You lost. Word: " + game.getWord());
            endGameUI();
        }
    }

    private void endGameUI() {
        guessBtn.setDisable(true);
        input.setDisable(true);
        playBtn.setDisable(false);
    }

    private void setIdleUI() {
        guessBtn.setDisable(true);
        input.setDisable(true);
        playBtn.setDisable(false);
        playBtn.setText("Play");
    }
}
