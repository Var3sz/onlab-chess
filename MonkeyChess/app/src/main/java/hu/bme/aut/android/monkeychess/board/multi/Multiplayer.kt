package hu.bme.aut.android.monkeychess.board.multi

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

class Multiplayer(val playerOne: String, val playerTwo: String, val gameId: String, var fen: String, val isNewGame: Boolean){
    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()

    var playerOneImageUrlLiveData = MutableLiveData<String>()
    var playerTwoImageUrlLiveData = MutableLiveData<String>()
    var currentUser = MutableLiveData<String>()

    init{
        getProfilePictures()
    }

    fun createNewGame(fen: String, callback: (String?, String?, String?) -> Unit){
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
                    userCollection.document(userID).collection("games").add(gameData).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            callback(gameID, playerOne, playerTwo)
                        } else {
                            callback(null, null, null)
                        }
                        callback(gameID, playerOne, playerTwo)
                    }
                }
            }
        }.addOnFailureListener { e->
            e.printStackTrace()
            callback(null ,null, null)
        }
    }

    fun sendMove(gameId: String, fen: String){
        val userCollection = db.collection("users")
        Log.d("Game ID: ", gameId)

        userCollection.get().addOnSuccessListener { users ->
            for (user in users) {
                val gamesCollection = userCollection.document(user.id).collection("games")
                val gameQuery = gamesCollection.whereEqualTo("Game ID", gameId)

                gameQuery.get().addOnSuccessListener { games ->
                    if (!games.isEmpty) {
                        val gameDoc = games.documents[0]
                        val gameRef = gamesCollection.document(gameDoc.id)

                        val updateData = hashMapOf<String, Any>(
                            "FEN" to fen
                        )

                        gameRef.update(updateData)
                            .addOnSuccessListener {
                            }
                            .addOnFailureListener { e ->
                                e.printStackTrace()
                            }
                    }
                }
            }
        }.addOnFailureListener { e ->
            e.printStackTrace()
        }
    }

    fun receiveMove(gameId: LiveData<String?>, callback: (String?) -> Unit) {
        GlobalScope.launch(Dispatchers.IO) {
            val userCollection = db.collection("users")

            userCollection.get().addOnSuccessListener { users ->
                for (user in users) {
                    val gamesCollection = userCollection.document(user.id).collection("games")
                    val gameQuery = gamesCollection.whereEqualTo("Game ID", gameId.value.toString())

                    gameQuery.addSnapshotListener { snapshots, error ->
                        if (error != null) {
                            error.printStackTrace()
                            callback(null)
                            return@addSnapshotListener
                        }

                        if (snapshots != null && !snapshots.isEmpty) {
                            val gameDoc = snapshots.documents[0]
                            val fen = gameDoc.getString("FEN")
                            callback(fen)
                        } else {
                            callback(null)
                        }
                    }
                }
            }.addOnFailureListener { e ->
                e.printStackTrace()
                callback(null)
            }
        }
    }


    private fun getProfilePictures() {
        db.collection("users").get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                for (document in task.result) {
                    if(auth.currentUser?.email == document.data.getValue("E-mail")){
                        val _currentUser = document.data.getValue("Username").toString()
                        currentUser.value = _currentUser
                    }
                    if (document.data.getValue("Username") == playerOne) {
                        val imageUrl = document.data.getValue("ImageURL").toString()
                        playerOneImageUrlLiveData.value = if (imageUrl.isNotEmpty()) imageUrl else "https://firebasestorage.googleapis.com/v0/b/monkeychess-b42f5.appspot.com/o/profile-pictures%2Fprofile-placeholder.png?alt=media&token=95aedef2-d07e-4b68-8045-8f677646fe51"
                    } else if (document.data.getValue("Username") == playerTwo) {
                        val imageUrl = document.data.getValue("ImageURL").toString()
                        playerTwoImageUrlLiveData.value = if (imageUrl.isNotEmpty()) imageUrl else "https://firebasestorage.googleapis.com/v0/b/monkeychess-b42f5.appspot.com/o/profile-pictures%2Fprofile-placeholder.png?alt=media&token=95aedef2-d07e-4b68-8045-8f677646fe51"
                    }
                }
            }
        }.addOnFailureListener { exception ->
            Log.d("Exception:", "Error getting documents", exception)
        }
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

class NullableLiveData<T>(defaultValue: T? = null) : MutableLiveData<T>() {
    init {
        value = defaultValue
    }
}