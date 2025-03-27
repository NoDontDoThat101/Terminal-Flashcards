# Terminal Flashcards
A program to open a terminal window and interact with saved flash cards.

When the program is run, it will first look for settings.json in its own directory. If settings.json doesn't exist, it makes it. If a setting is invalid or empty, it asks the user to replace it.
Then it'll look in the flashcard_path for flashcards and begin processing.

Since this is not intended to be used outside of a few dozen people, I'm not bothering to document this. The code documents itself for the most part, and isn't complicated to decypher.