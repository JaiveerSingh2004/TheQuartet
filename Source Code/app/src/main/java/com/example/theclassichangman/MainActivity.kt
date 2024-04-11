package com.example.theclassichangman


import androidx.compose.ui.platform.LocalContext
import android.app.AlertDialog
import android.content.res.Resources
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.theclassichangman.ui.theme.TheClassicHangmanTheme
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    private var bgmediaPlayer: MediaPlayer? = null
    private var soundEffectPlayer: MediaPlayer? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val categoriesAndWords = readCategoriesAndWordsFromResources(resources)
            val (randomCategory, randomWord) = chooseRandomWord(categoriesAndWords)

            TheClassicHangmanTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    Main(
                        category = randomCategory,
                        randomWord = randomWord,
                        playCorrectGuessSound = ::playCorrectGuessSound,
                        playWinSound = ::playWinSound,
                        playLoseSound = ::playLoseSound,
                        playWrongLetterSound = ::playWrongLetterSound,
                        soundEffectPlayer = soundEffectPlayer,
                        categoriesAndWords = categoriesAndWords // Add this line

                    )
                }
            }
        }

        bgmediaPlayer = MediaPlayer.create(this, R.raw.bg_music)
        bgmediaPlayer?.isLooping = true // Loop the music
        bgmediaPlayer?.start()

        soundEffectPlayer = MediaPlayer()

    }

    override fun onDestroy() {
        super.onDestroy()
        // Release MediaPlayer when the activity is destroyed
        bgmediaPlayer?.release()
        bgmediaPlayer = null

        soundEffectPlayer?.release()
        soundEffectPlayer = null


    }


    private fun getSoundFileUri(fileName: String): Uri {
        val resId = resources.getIdentifier(fileName, "raw", packageName)
        return Uri.parse("android.resource://$packageName/$resId")
    }

    private fun playSound(uri: Uri?) {
        // Play the specified sound using MediaPlayer
        if (uri != null) {
            soundEffectPlayer?.apply {
                if(isPlaying){
                    stop()
                }
                reset()
                setDataSource(this@MainActivity, uri)
                prepare()
                start()
            }
        }
    }

    fun playCorrectGuessSound() {
        val uri = getSoundFileUri("right_letter")
        playSound(uri)
    }

    fun playWinSound() {
        val uri = getSoundFileUri("win")
        playSound(uri)
    }

    fun playLoseSound() {
        val uri = getSoundFileUri("loose")
        playSound(uri)
    }

    fun playWrongLetterSound() {
        val uri = getSoundFileUri("loose_life")
        playSound(uri)
    }
    }

    private fun readCategoriesAndWordsFromResources(resources: Resources): List<Pair<String, List<String>>> {
        val categoriesAndWords = mutableListOf<Pair<String, List<String>>>()
        val reader = BufferedReader(InputStreamReader(resources.openRawResource(R.raw.words)))
        var currentCategory = ""
        val words = mutableListOf<String>()

        reader.useLines { lines ->
            lines.forEach { line ->
                if (line.startsWith("[")) {
                    if (words.isNotEmpty()) {
                        categoriesAndWords.add(Pair(currentCategory, words.toList()))
                        words.clear()
                    }
                    currentCategory = line.substring(1, line.length - 1)
                } else {
                    words.add(line)
                }
            }
        }

        if (words.isNotEmpty()) {
            categoriesAndWords.add(Pair(currentCategory, words.toList()))
        }

        return categoriesAndWords
    }

    private fun chooseRandomWord(categoriesAndWords: List<Pair<String, List<String>>>): Pair<String, String> {
        val randomPair = categoriesAndWords.random()
        var randomCategory = randomPair.first
        var randomWord = randomPair.second.random()
        return Pair(randomCategory, randomWord)
    }


@Composable
fun Main(
    category: String,
    randomWord: String,
    playCorrectGuessSound: () -> Unit,
    playWinSound: () -> Unit,
    playLoseSound: () -> Unit,
    playWrongLetterSound: () -> Unit,
    soundEffectPlayer: MediaPlayer?,
    categoriesAndWords: List<Pair<String, List<String>>> // Add this line
) {


    fun buildInitialGuessedWord(word: String): String {
        return word.map { '_' }.joinToString(" ")
    }

    var initialCategory = category
    var initialRandomWord = randomWord

    var category by remember { mutableStateOf(initialCategory) }
    var randomWord by remember { mutableStateOf(initialRandomWord) }

    var guessedWord by remember { mutableStateOf(buildInitialGuessedWord(randomWord)) }
    var lives by remember { mutableStateOf(6) } // Start with 6 lives
    var hangmanImage by remember { mutableStateOf(R.drawable.hangman01) }
    val disabledButtons = remember { mutableStateOf(mutableSetOf<Char>()) }
    val context = LocalContext.current


    fun handleButtonClick(letter: Char) {
        if (!randomWord.contains(letter, ignoreCase = true)) {
            // Incorrect guess
            lives--
            playWrongLetterSound()
            when (lives) {
                5 -> hangmanImage = R.drawable.hangman02
                4 -> hangmanImage = R.drawable.hangman03
                3 -> hangmanImage = R.drawable.hangman04
                2 -> hangmanImage = R.drawable.hangman05
                1 -> hangmanImage = R.drawable.hangman06
                0 -> hangmanImage = R.drawable.hangman07 // Game over image
            }
            Log.d("Hangman", "Lives remaining: $lives")
            Log.d("Hangman", "Updated hangmanImage: $hangmanImage")
        } else {
            // Correct guess
            playCorrectGuessSound()
            Log.d("Hangman", "Lives remaining: $lives")
            val newGuessedWord = StringBuilder(guessedWord)
            randomWord.forEachIndexed { index, c ->
                if (c.equals(letter, ignoreCase = true)) {
                    newGuessedWord[index * 2] = letter
                }
            }
            guessedWord = newGuessedWord.toString()
        }

        disabledButtons.value.add(letter)


        fun resetGame() {
            // Add your game reset logic here
            // For example, you might want to reset the guessed word, lives, hangman image, and disabled buttons
            soundEffectPlayer?.let {
                if (it.isPlaying) {
                    it.stop()
                }
            }
            val (newRandomCategory, newRandomWord) = chooseRandomWord(categoriesAndWords)

            // Reset the game state
            category = newRandomCategory
            randomWord = newRandomWord
            guessedWord = buildInitialGuessedWord(randomWord)
            lives = 6
            hangmanImage = R.drawable.hangman01
            disabledButtons.value.clear()
        }

        fun showDialog(title: String, message: String) {
            AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Play Again!") { dialog, _ ->
                    resetGame()
                    dialog.dismiss()
                }
                .show()
        }



        if (!guessedWord.contains('_')) {
            // Player wins
            playWinSound()
            showDialog("Congratulations!", "You have guessed the word correctly: $randomWord")
        } else if (lives == 0) {
            // Player loses
            playLoseSound()
            showDialog("Game Over", "You have run out of lives. The word was: $randomWord")
        }
    }


    Box(modifier = Modifier.fillMaxSize()) {

        Image(
            painter = painterResource(id = R.drawable.bg_image),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.matchParentSize()
        )


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(32.dp))

            Image(
                painter = painterResource(id = hangmanImage),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .size(250.dp)
                    .aspectRatio(1f)
            )
        }

        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(16.dp)
        ) {

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Category: $category",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Magenta,
                textAlign = TextAlign.Center
            )

            Text(
                text = guessedWord,
                style = MaterialTheme.typography.displayMedium,
                color = Color.Black,
                textAlign = TextAlign.Center
            )
//            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Lives: $lives",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Red,
                textAlign = TextAlign.Center
            )

        }

        Box(
            contentAlignment = Alignment.BottomCenter,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
        )
         {
            Column {
                val letters = ('A'..'Z').toList()
                val chunkedLetters = letters.chunked(7)

                chunkedLetters.forEach { chunk ->
                    Row(
//                        modifier = Modifier.padding(vertical = 6.dp, horizontal = 16.dp),
                        modifier = Modifier.padding(1.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        chunk.forEach { letter ->
                            Button(
                                onClick = { handleButtonClick(letter) },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.Transparent,
                                    contentColor = Color.Black
                                ),
                                enabled = !disabledButtons.value.contains(letter),
                                modifier = Modifier
                                    .weight(1f)
                                    .aspectRatio(1f)
                            ) {
                                Text(
                                    text = letter.toString(),
                                    fontSize = 25.sp,
                                    fontFamily = FontFamily.Cursive,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }


        }
    }
}


@Preview(showBackground = true, widthDp = 412, heightDp = 915)
@Composable
fun PreviewJaiveerMain() {
    TheClassicHangmanTheme {
        Main(
            category = "Test Category",
            randomWord = "TestWord",
            playCorrectGuessSound = {},
            playWinSound = {},
            playLoseSound = {},
            playWrongLetterSound = {},
            soundEffectPlayer = null,
            categoriesAndWords = emptyList() // Add this line

        )
    }
}

//@Preview(showBackground = true, widthDp = 360, heightDp = 800)
//@Composable
//fun PreviewIshpreetMain() {
//    TheClassicHangmanTheme {
//        Main(
//            category = "Test Category",
//            randomWord = "TestWord",
//            playCorrectGuessSound = {},
//            playWinSound = {},
//            playLoseSound = {},
//            playWrongLetterSound = {},
//            soundEffectPlayer = null

//        )
//    }
//}



