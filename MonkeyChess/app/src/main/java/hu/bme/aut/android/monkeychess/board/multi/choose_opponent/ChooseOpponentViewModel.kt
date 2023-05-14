package hu.bme.aut.android.monkeychess.board.multi.choose_opponent

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ChooseOpponentViewModel:ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    private var users = mutableListOf<String>()
    private var userList = MutableLiveData<List<String>>()

    init{
        loadUsers()
    }

    private fun loadUsers(){
        val collectionRef = db.collection("users")
        collectionRef.get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot) {
                    val username = document.getString("Username")
                    val email = document.getString("E-mail")

                    if(auth.currentUser?.email == email){
                        continue
                    }else{
                        username?.let { users.add(it) }
                    }

                    addUsers(users)
                }
            }
            .addOnFailureListener { exception ->
                Log.d("TAG", "Error getting documents: ", exception)
            }
    }

    private fun addUsers(users: List<String>){
        userList.value = users;
    }

    fun getUsers(): MutableLiveData<List<String>>{
        return userList
    }
}