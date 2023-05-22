package hu.bme.aut.android.monkeychess.board.multi

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class Multiplayer(val playerOne: String, val playerTwo: String, var fen: String, val isNewGame: Boolean){
    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()

    fun createNewGame(fen: String){
        val userCollection = db.collection("users")
        userCollection.get().addOnSuccessListener { users ->
            val gameID = generateRandomId(8)
            for (user in users){
                if(user.data.getValue("Username") == playerOne){
                    val userID = user.id
                    val gameData = hashMapOf(
                        "Game ID" to gameID,
                        "Player One" to playerOne,
                        "Player Two" to playerTwo,
                        "FEN" to fen
                    )
                    userCollection.document(userID).collection("games").add(gameData)
                }
                else if(user.data.getValue("Username") == playerTwo){
                    val userID = user.id
                    val gameData = hashMapOf(
                        "Game ID" to gameID,
                        "Player One" to playerOne,
                        "Player Two" to playerTwo,
                        "FEN" to fen
                    )
                    userCollection.document(userID).collection("games").add(gameData)
                }
            }
        }
    }

    fun sendMove(fen: String){

    }

    fun receiveMove(){

    }


    fun logPlayers(){
        Log.d("Multiplayer playeOne: ", playerOne)
        Log.d("Multiplayer playeTwo: ", playerTwo)
    }

    fun generateRandomId(length: Int): String {
        val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
        val random = Random(System.currentTimeMillis())

        val id = StringBuilder(length)
        repeat(length) {
            val randomIndex = random.nextInt(chars.length)
            val randomChar = chars[randomIndex]
            id.append(randomChar)
        }

        return id.toString()
    }

}