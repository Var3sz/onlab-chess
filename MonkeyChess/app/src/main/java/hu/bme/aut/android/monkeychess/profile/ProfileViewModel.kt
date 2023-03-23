package hu.bme.aut.android.monkeychess.profile

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProfileViewModel : ViewModel() {
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var userDB: FirebaseFirestore = FirebaseFirestore.getInstance()
    private var usernameLiveData = MutableLiveData<String>()
    private var fullnameLiveData = MutableLiveData<String>()
    private var emailLiveData = MutableLiveData<String>()
    private var accCreatedAtLiveData = MutableLiveData<String>()

    init{
        showProfileData()
    }

    private fun showProfileData(){
        if(auth.currentUser != null) {
            userDB.collection("users").get().addOnCompleteListener {
                if (it.isSuccessful) {
                    for (document in it.result) {
                        if (document.data.getValue("E-mail") == auth.currentUser?.email) {
                            setUsername(document.data.getValue("Username").toString())
                            setFullname(document.data.getValue("Name").toString())
                            setEmail(document.data.getValue("E-mail").toString())
                            setAccCreatedAt(document.data.getValue("Sign-up date").toString())
                            Log.d("User inside: ", "${auth.currentUser?.email}")
                            Log.d("Users: ", "${document.data.getValue("Username")}")
                        }
                    }
                }
            }.addOnFailureListener { exception ->
                Log.d("Exception:", "Error getting documents", exception)
            }
        }
        else{
            setUsername("")
            setFullname("")
            setEmail("")
            setAccCreatedAt("")
        }
    }


    private fun setUsername(string: String){
        usernameLiveData.value = string
    }

    private fun setFullname(string: String){
        fullnameLiveData.value = string
    }

    private fun setEmail(string: String){
        emailLiveData.value = string
    }

    private fun setAccCreatedAt(string: String){
        accCreatedAtLiveData.value = string
    }

    fun getUsername(): MutableLiveData<String>{
        return usernameLiveData
    }
    fun getFullname(): MutableLiveData<String>{
        return fullnameLiveData
    }
    fun getEmail(): MutableLiveData<String>{
        return emailLiveData
    }
    fun getAccCreatedAt(): MutableLiveData<String>{
        return accCreatedAtLiveData
    }
}