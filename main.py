# Step 4
import random
import stages
import word_list
import logo

print(logo.logo)

choosen_word = random.choice(word_list.word_list)
word_length = len(choosen_word)
display = [ ]
choosen_word_list = [*choosen_word]
lives = 6



for x in range(word_length):
    display.append("_")

end_of_game = False
while not end_of_game:

    guess = input("Enter the letter to guess the word: ").lower()

    # Checks if entered charachters is number or more than one characheter
    if not guess.isalpha() or len(guess) != 1:
        print("Please enter a valid single character.")
        continue

    if guess in display:
        print(f"You have already guessed this letter {guess}.")
    for position in range(word_length):
        letter = choosen_word[position]
        if letter == guess:
            display[position] = letter
    if guess not in choosen_word:
        print(f"This letter {guess} is not in the word, you lost a life. :( ")
        lives-=1
        if lives == 0:
            end_of_game = True
            print(f"The word was {choosen_word}.")
            print("You Lose")
    print("".join(display))

    if "_" not in display:
        end_of_game = True
        print("You Win")
    print(stages.stages[lives])
