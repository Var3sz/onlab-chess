package hu.bme.aut.android.monkeychess.board.multi.select_game

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore

class SelectGameViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    private val _games = MutableLiveData<List<Pair<String, String>>>()
    val games: LiveData<List<Pair<String, String>>> get() = _games

    init{
        loadGames()
    }

    private fun loadGames(){
        val usersCollectionRef  = db.collection("users")
        usersCollectionRef .addSnapshotListener { value, error ->
            if (error != null) {
                return@addSnapshotListener
            }

            val updateGames = mutableListOf<Pair<String, String>>()

            for (document in value!!) {
                val userId = document.id
                val userSubcollectionRef = usersCollectionRef.document(userId).collection("games")
                val email = document.getString("E-mail")

                if(auth.currentUser?.email == email){
                    userSubcollectionRef.addSnapshotListener { gamesSnapshot, exception ->
                        for (gameDocument in gamesSnapshot!!) {
                            val gameData = gameDocument.data

                            val player1 = gameData["Player One"]?.toString() ?: ""
                            val player2 = gameData["Player Two"]?.toString() ?: ""

                            updateGames.add(Pair(player1, player2))
                        }
                        _games.value = updateGames
                    }
                }
                _games.value = updateGames
            }
        }
    }
}