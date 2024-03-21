import random  # Import random module to generate random numbers 
import stages # Import stages inbuilt module to display stages
import logo # Import logo inbuilt module to display logo
import word_list # Import word_list inbuilt module to display word_list

print(logo.logo) # Print the logo

chosen_category = random.choice(list(word_list.word_list.keys())) # Choose a random category from the word_list
chosen_word = random.choice(word_list.word_list[chosen_category]) # Choose a random word from the chosen category
word_length = len(chosen_word) # Get the length of the chosen word
display = ["_"] * word_length # Create a list of underscores to display the word


print(word_list.category_ASCII[chosen_category]) #  Print the ASCII art for the selected category

print(f"The word consists of {word_length} characters.") # Print the length of the chosen word

guessed_letters = []  # Track guessed letters
lives = len(stages.stages) - 1  # Set lives based on the number of stages



end_of_game = False # Set the end of game to False
while not end_of_game: # While the end of game is False
    print(" ".join(display)) # Print the word to be guessed
    print("\n") 
    print(f"No. of lives left: {lives}") # Print the number of lives left
    guess = input("Enter a letter to guess the word: ").lower() # Get the user input and convert it to lowercase

    # Checks if entered character is a letter and single character
    if not guess.isalpha() or len(guess) != 1:
        print("Please enter a valid single character.")
        continue

    # Check if the letter has already been guessed
    if guess in guessed_letters:
        print(f"You have already guessed the letter {guess}.")
        continue
    guessed_letters.append(guess) # Add the guessed letter to the list of guessed letters

    # Check if the guessed letter is not in the chosen word
    if guess not in chosen_word:
        print(f"This letter {guess} is not in the word, you lost a life. :( ") 
        lives -= 1 
        if lives == 0:
            end_of_game = True
            print(f"The word was {chosen_word}.")
    # If the guessed letter is in the chosen word          
    else:
        for position in range(word_length):
            letter = chosen_word[position] # Get the letter at the current position
            if letter == guess: # If the letter at the current position is the guessed letter
                display[position] = letter # Replace the underscore at the current position with the guessed letter

    if "_" not in display: # If there are no underscores left in the display
        end_of_game = True  # Set the end of game to True
        print("\n")
        print(f"The word was {chosen_word.upper()}.") 
        print(stages.you_win) 

    print(stages.stages[lives])  # Adjusting lives for indexing
