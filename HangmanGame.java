package com.example.hangmanjavafx;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class HangmanGame {

    // URL for loading words online
    //Lecture04-Files, Input-Output Stream page 84
    private static final String WordFromTheURL =
            "https://raw.githubusercontent.com/dwyl/english-words/master/words_alpha.txt";

    // Maximum wrong guesses allowed
    private static final int MaxLives = 6;

    // Word list
    private static List<String> words = new ArrayList<>();

    private final String word;
    private final char[] progress;
    private final List<Character> usedLetters = new ArrayList<>();
    private final List<String> triedWords = new ArrayList<>();
    private int wrongGuesses = 0;

    // What can happen when you guess a letter
    public enum GuessResult { HIT, MISS, REPEATED, INVALID }

    // What can happen when you guess a word
    public enum WordResult { CORRECT, WRONG_NEW, WRONG_REPEAT, INVALID }

    // Try to load words from the internet
    // If it fails, it uses a built-in list of words that is a little bit down from here
    static {
        try {
            // Create a URL object from the specified string
            URL url = new URL(WordFromTheURL);

            // Open a BufferedReader to read from the URL
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()))) {

                String line;

                // Read each line from the file
                while ((line = reader.readLine()) != null) {
                    line = line.trim();
                    if (!line.isEmpty()) words.add(line.toUpperCase());
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading words from URL. Using fallback list.");

            // built-in list of words' list
            words = Arrays.asList("JAVA", "TRAIN", "BIRD", "HANGMAN", "CAR", "SCENE", "GRAPHICS", "AMONG US",
                    "MMMMMMM", "X", "I AM A BIRD", "DOOM", "WENN ES MEINES GOTTES WILLE", "OVERLORD",
                    "APPLES", "ZOMBIES", "MIKU", "AK 47", "EXODIA", "POKEMON", "ASSAULT RIFLE",
                    "TYRANT", "BLUE EYES WHITE DRAGON", "ARTILLERY BATTERY", "MEGA KNIGHT",
                    "ESPANOL", "JESTER", "PUERTO RICO", "Caballo", "VACA", "CASA", "JUAN",
                    "PEDRO", "SUPER MAN", "FLASH FLOOD", "IDK", "HANG MAN", "X-KIRRA",
                    "PILATES", "MAHORAGA", "DANCE", "MOAB", "METRO MAN");
        }
    }

    // Pick a random word for the game and set up blanks for each letter (like a shadow of the word)
    //Lecture13-Introduction to Java page 218 as example
    public HangmanGame() {

        Random rand = new Random();

        this.word = words.get(rand.nextInt(words.size()));

        this.progress = new char[word.length()];

        for (int i = 0; i < word.length(); i++) {

            // Underscore for letters
            progress[i] = Character.isLetter(word.charAt(i)) ? '_' : word.charAt(i);
        }
    }

    // Single-letter guess
    public GuessResult guessLetter(char temporaryCharThing) {

        temporaryCharThing = Character.toUpperCase(temporaryCharThing);

        // Validate input
        if (!Character.isLetter(temporaryCharThing)){
            return GuessResult.INVALID;
        }

        if (usedLetters.contains(temporaryCharThing)) {
            return GuessResult.REPEATED;
        }

        usedLetters.add(temporaryCharThing);
        boolean found = false;

        // Reveal letter positions in progress
        for (int i = 0; i < word.length(); i++)
            if (word.charAt(i) == temporaryCharThing) {
                progress[i] = temporaryCharThing;
                found = true;
            }

        if (!found) {
            // Increment wrong guesses if a letter not found or roung ( o mal, no voy a buscar en google como escribe esa palabra ya son  las 12;00p.m)
            wrongGuesses++;
        }

        return found ? GuessResult.HIT : GuessResult.MISS;
    }

    // Full-word guess
    public WordResult guessWord(String guess) {
        guess = guess.toUpperCase();

        // Validate input
        //Lecture09-Regular Expressions page 8
        if (!guess.matches("[A-Z ]+")) {
            return WordResult.INVALID;
        }

        if (triedWords.contains(guess)) {
            return WordResult.WRONG_REPEAT;
        }

        triedWords.add(guess);
        if (guess.equals(word)){
            return WordResult.CORRECT;
        }

        //copy paste
        // Increment wrong guesses if a letter not found or roung ( o mal, no voy a buscar en google como escribe esa palabra ya son  las 12;00p.m)
        wrongGuesses++;
        return WordResult.WRONG_NEW;
    }

    // Check if player has won :)
    public boolean isWon() {
        for (int i = 0; i < progress.length; i++)
            if (progress[i] == '_' && Character.isLetter(word.charAt(i))) return false;
        return true;
    }

    // Check if player has lost :(
    public boolean isLost() {
        return wrongGuesses >= MaxLives;
    }

    // Remaining lives
    public int getLives() { return MaxLives - wrongGuesses; }

    // Get actual word
    public String getWord() { return word; }

    // Returns masked word with spaces between letters
    public String maskedWithSpaces() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < progress.length; i++) {
            builder.append(progress[i]);
            if (i < progress.length - 1) builder.append(' ');
        }
        return builder.toString();
    }

    // Returns used letters as string for display
    public String getUsedLettersString() {
        List<Character> misses = new ArrayList<>();
        for (char temporaryCharThing : usedLetters)
            if (word.indexOf(temporaryCharThing) < 0) misses.add(temporaryCharThing);

        // This sorts it alphabetically for fancy points
        Collections.sort(misses);

        StringBuilder builder = new StringBuilder ();
        for (int i = 0; i < misses.size(); i++) {
            builder.append(misses.get(i));
            if (i < misses.size() - 1) builder.append(' ');
        }
        return builder.toString();
    }

    // Show all the words the player has tried so far
    public String getTriedWordsString() {
        if (triedWords.isEmpty()) return "";
        return String.join(", ", triedWords);
    }
}
