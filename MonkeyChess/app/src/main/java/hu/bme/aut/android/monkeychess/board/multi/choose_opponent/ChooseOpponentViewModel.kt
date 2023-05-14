package hu.bme.aut.android.monkeychess.board.multi.choose_opponent

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class ChooseOpponentViewModel:ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    private var users = mutableListOf<Pair<String, String>>()
    private var userList = MutableLiveData<List<Pair<String, String>>>()


    init{
        loadUsers()
    }

    private fun loadUsers(){
        val collectionRef = db.collection("users")
        collectionRef.get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot) {
                    val username = document.data.getValue("Username")
                    val imageUrl = document.data.getValue("ImageURL")
                    val email = document.getString("E-mail")

                    if(auth.currentUser?.email == email){
                        continue
                    }else{
                    if(imageUrl.toString().isNotEmpty()){
                        users.add(Pair(username.toString(), imageUrl.toString()))
                    }
                    else{
                        val defaultUrl = "https://firebasestorage.googleapis.com/v0/b/monkeychess-b42f5.appspot.com/o/profile-pictures%2Fprofile-placeholder.png?alt=media&token=95aedef2-d07e-4b68-8045-8f677646fe51"
                        users.add(Pair(username.toString(), defaultUrl))
                    }

                    }
                    addUsers(users)
                }
            }
            .addOnFailureListener { exception ->
                Log.d("TAG", "Error getting documents: ", exception)
            }
    }

    private fun addUsers(users: List<Pair<String, String>>){
        userList.value = users
    }

    fun getUsers(): MutableLiveData<List<Pair<String, String>>>{
        return userList
    }
}