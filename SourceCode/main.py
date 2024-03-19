import random
import stages
import word_list
import logo

print(logo.logo)

choosen_word = random.choice(word_list.word_list)
word_length = len(choosen_word)
display = ["_"] * word_length
choosen_word_list = list(choosen_word)
lives = 6

guessed_letters = []  # Track guessed letters

end_of_game = False
while not end_of_game:
    guess = input("Enter the letter to guess the word: ").lower()

    # Checks if entered character is a letter and single character
    if not guess.isalpha() or len(guess) != 1:
        print("Please enter a valid single character.")
        continue

    # Check if the letter has already been guessed
    if guess in guessed_letters:
        print(f"You have already guessed the letter {guess}.")
        continue
    guessed_letters.append(guess)

    if guess not in choosen_word:
        print(f"This letter {guess} is not in the word, you lost a life. :( ")
        lives -= 1
        if lives == 0:
            end_of_game = True
            print(f"The word was {choosen_word}.")
            print("You Lose")
    else:
        for position in range(word_length):
            letter = choosen_word[position]
            if letter == guess:
                display[position] = letter

    print(" ".join(display))

    if "_" not in display:
        end_of_game = True
        print("You Win")
    print(stages.stages[lives])
