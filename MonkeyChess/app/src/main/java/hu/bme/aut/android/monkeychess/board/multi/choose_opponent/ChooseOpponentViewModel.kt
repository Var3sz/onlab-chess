package hu.bme.aut.android.monkeychess.board.multi.choose_opponent

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ChooseOpponentViewModel:ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    private val _users = MutableLiveData<List<Pair<String, String>>>()
    val users: LiveData<List<Pair<String, String>>> get() = _users

    private val _currentUser = MutableLiveData<String>()
    val currentUser: LiveData<String> get() = _currentUser

    init{
        loadUsers()
    }

    private fun loadUsers(){
        val collectionRef = db.collection("users")
        collectionRef.addSnapshotListener { value, error ->
            if (error != null) {
                return@addSnapshotListener
            }

            val updatedUsers = mutableListOf<Pair<String, String>>()

            for (document in value!!) {
                val username = document.data.getValue("Username")
                val imageUrl = document.data.getValue("ImageURL")
                val email = document.getString("E-mail")

                if (auth.currentUser?.email != email && imageUrl.toString().isNotEmpty()) {
                    updatedUsers.add(Pair(username.toString(), imageUrl.toString()))
                }
                else if(auth.currentUser?.email != email && imageUrl.toString().isEmpty()){
                    val defaultImage = "https://firebasestorage.googleapis.com/v0/b/monkeychess-b42f5.appspot.com/o/profile-pictures%2Fprofile-placeholder.png?alt=media&token=95aedef2-d07e-4b68-8045-8f677646fe51"
                    updatedUsers.add(Pair(username.toString(), defaultImage))
                }
                else if(auth.currentUser?.email == email){
                    _currentUser.value = document.data.getValue("Username").toString()
                }
            }
            _users.value = updatedUsers
        }
    }
}