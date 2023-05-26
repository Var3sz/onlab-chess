package hu.bme.aut.android.monkeychess.board.single

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SinglePlayer {
    val auth = FirebaseAuth.getInstance()
    val userDB = FirebaseFirestore.getInstance()

    var currentUserLiveData = MutableLiveData<String>()

    var imageUrlLiveData = MutableLiveData<String>()

    init {
        getCurrentUser()
    }

    private fun getCurrentUser() {
        if (auth.currentUser != null) {
            userDB.collection("users").get().addOnCompleteListener {
                if (it.isSuccessful) {
                    for (document in it.result) {
                        if (document.data.getValue("E-mail") == auth.currentUser?.email) {
                            setCurrentUser(document.data.getValue("Username").toString())
                            if(document.data.getValue("ImageURL").toString().isNotEmpty()){
                                setImageUrl(document.data.getValue("ImageURL").toString())
                            }else{
                                setImageUrl("https://firebasestorage.googleapis.com/v0/b/monkeychess-b42f5.appspot.com/o/profile-pictures%2Fprofile-placeholder.png?alt=media&token=95aedef2-d07e-4b68-8045-8f677646fe51")
                            }
                        }
                    }
                }
            }.addOnFailureListener { exception ->
                Log.d("Exception:", "Error getting documents", exception)
            }
        } else {
            setCurrentUser("")
        }
    }

    private fun setCurrentUser(user: String?){
        currentUserLiveData.value = user
    }

    private fun setImageUrl(url: String?){
        imageUrlLiveData.value = url
    }
}