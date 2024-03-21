# Hangman Game Documentation

## Overview

Hangman Game is a Python-based interactive word guessing game. It challenges players to guess a word by suggesting letters within a certain number of incorrect guesses.
The game provides hints about the category of the word and includes visual representations of the hangman for incorrect guesses.

## Demo Gameplay Video

[Gameplay Video](https://drive.google.com/file/d/1yqnBVOYjGJYEfvXl6SLxXLb2xBIQccG8/view?usp=sharing)

## Functionality

The Hangman game consists of the following main functionalities:

1. **Word Selection:** The game randomly selects a word from a predefined list based on different categories.

2. **Guessing Mechanism:** Players can guess letters to try and uncover the hidden word. Incorrect guesses result in a decrease in the player's remaining lives.

3. **Game End:** The game ends when the player correctly guesses the word or runs out of lives.

## Modules

The Hangman game is composed of the following modules:

1. **main.py:** This is the main Python script that initializes the game, handles user input, and manages the game flow.

2. **stages.py:** This module contains ASCII art representations of the hangman for different stages of the game.

3. **logo.py:** This module contains ASCII art for the game logo.

4. **word_list.py:** This module contains predefined word lists categorized by different themes.

## Dependencies

The Hangman game requires the following dependencies:

- Python 3.x: The game is written in Python and requires Python 3.x to run.

## Setup Instructions

To run the Hangman game, follow these steps:

1. Clone the repository to your local machine:

```
git clone <repository_url>
```

2. Navigate to the directory where the game files are located:

```
cd hangman-game
```

3. Make sure you have Python installed. You can check your Python version using the following command:

```
python --version
```

4. Run the main.py script to start the game:

```
python main.py
```

5. Follow the prompts to play the game.

## Additional Mechanisms

In addition to the main functionalities, the Hangman game includes the following mechanisms:

- **Category Display:** The game displays the category of the word being guessed, providing players with a hint to help them guess the word.
  
- **Hangman Visualization:** As players make incorrect guesses, the game displays a visual representation of the hangman being "built," with additional parts added for each incorrect guess.
  
- **Win/Loss Notification:** Upon winning or losing the game, players receive a notification with the outcome and the correct word.

## Additional Notes

- The game offers a fun and engaging way to improve vocabulary and spelling skills.
  
- Players can enjoy the game solo or compete with friends to see who can guess the word with the fewest incorrect guesses.
  
- The game's ASCII art adds a nostalgic touch reminiscent of classic hangman games.

## Play Online

You can play the Hangman Game online using the following link:

[Play Hangman Game on Replit](https://replit.com/@TheTrio03/TheHangmanGame?v=1)
