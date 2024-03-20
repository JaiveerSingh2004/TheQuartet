import random
import stages
import word_list
import logo

print(logo.logo)

# Function to choose difficulty
def choose_difficulty():
    while True:
        difficulty = input("Choose difficulty (easy, normal, hard): ").lower()
        if difficulty in ['easy', 'normal', 'hard']:
            return difficulty
        else:
            print("Invalid difficulty! Please choose from 'easy', 'normal', or 'hard'.")

# Function to initialize lives based on difficulty
def initialize_lives(difficulty):
    if difficulty == 'easy':
        return 8
    elif difficulty == 'normal':
        return 6
    elif difficulty == 'hard':
        return 4

# Function to initialize stages based on difficulty
def initialize_stages(difficulty):
    if difficulty == 'easy':
        return stages.easy_stages
    elif difficulty == 'normal':
        return stages.normal_stages
    elif difficulty == 'hard':
        return stages.hard_stages

difficulty = choose_difficulty()
lives = initialize_lives(difficulty)
stage_images = initialize_stages(difficulty)

choosen_word = random.choice(word_list.word_list)
word_length = len(choosen_word)
display = ["_ " for _ in range(word_length)]
choosen_word_list = [*choosen_word]

end_of_game = False
guessed_letters = []
while not end_of_game:

    guess = input("Enter a letter to guess the word: ").lower()

    if not guess.isalpha() or len(guess) != 1:
        print("Please enter a valid single character.")
        continue

    if guess in display:
        print(f"You have already guessed the letter {guess}.")
        continue

    for position in range(word_length):
        letter = choosen_word[position]
        if letter == guess:
            display[position] = letter

    if guess not in choosen_word:
        print(f"The letter {guess} is not in the word. You lost a life. :(")
        lives -= 1
        if lives == 0:
            end_of_game = True
            print(f"The word was {choosen_word}.")
            print("You lose!")
            break
    
    print(" ".join(display))

    if "_" not in display:
        end_of_game = True
        print("You win!")
        break

    stage_image_path = stage_images[min(lives, len(stage_images) - 1)]
    print(stage_image_path)
