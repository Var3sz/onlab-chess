package hu.bme.aut.android.monkeychess.mainMenu

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class MainMenuViewModel : ViewModel(){
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var userDB: FirebaseFirestore = FirebaseFirestore.getInstance()
    var usernameLiveData = MutableLiveData<String>()

    init{
        usernameLiveData = MutableLiveData<String>()
        showUsername()
    }

    private fun showUsername(){
        if(auth.currentUser != null) {
            userDB.collection("users").get().addOnCompleteListener {
                if (it.isSuccessful) {
                    for (document in it.result) {
                        if (document.data.getValue("E-mail") == auth.currentUser?.email) {
                            setUsername(document.data.getValue("Username").toString())
                            Log.d("User inside: ", "${auth.currentUser?.email}")
                            Log.d("Users: ", "${document.data.getValue("Username")}")
                        }

                    }
                }
            }.addOnFailureListener { exception ->
                Log.d("Exception:", "Error getting documents", exception)
            }
        }
    }

    private fun setUsername(string: String){
        val user: String = string
        usernameLiveData.value = user
        Log.d("Value:", user)
    }



    fun logoutUser(context: Context, navController: NavController){
        if(auth.currentUser!=null){
            auth.signOut()
            navController.navigate("welcome_screen")
            Toast.makeText(context, "Successful logout!", Toast.LENGTH_LONG).show()
        }
    }


}
