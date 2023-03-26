package hu.bme.aut.android.monkeychess.profile.modify.email

import android.content.Context
import android.util.Patterns
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class EmailChangeViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val userDB = FirebaseFirestore.getInstance()
    private var password: MutableLiveData<String> = MutableLiveData("")
    private var email: MutableLiveData<String> = MutableLiveData("")
    private var newEmail: MutableLiveData<String> = MutableLiveData("")

    private fun changeEmailAddress(context: Context, navController: NavController){
        val user = auth.currentUser!!
        val credential = EmailAuthProvider.getCredential(email.value.toString(), password.value.toString())

        user.reauthenticate(credential).addOnCompleteListener {
            if(it.isSuccessful){
                userDB.collection("users").get().addOnCompleteListener { task ->
                    if(task.isSuccessful){
                        for(document in task.result){
                            if(document.data.getValue("E-mail") == auth.currentUser?.email){
                                userDB.collection("users").document(document.id).update("E-mail", newEmail.value.toString())
                            }
                        }
                    }
                }.addOnFailureListener{
                    Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                }


                user.updateEmail(newEmail.value.toString()).addOnCompleteListener { task->
                    if(task.isSuccessful){
                        val verifiable = auth.currentUser
                        verifiable?.sendEmailVerification()
                        auth.signOut()
                        navController.navigate("login_screen")
                        Toast.makeText(context, "E-mail address changed, login to continue!", Toast.LENGTH_LONG).show()
                    }
                    else{
                        Toast.makeText(context, "Unable to change e-mail address!", Toast.LENGTH_LONG).show()
                    }
                }.addOnFailureListener{
                    Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                }
            }
        }.addOnFailureListener{
           Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
        }
    }

    fun isChangeEmailInputValid(context: Context, navController: NavController){
        if(password.value.toString().isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email.value.toString()).matches() && Patterns.EMAIL_ADDRESS.matcher(newEmail.value.toString()).matches()){
            changeEmailAddress(context, navController)
        }
        else{
            Toast.makeText(context, "Please provide your credentials!", Toast.LENGTH_LONG).show()
        }
    }

    fun setPassword(_pass: String){
        password.value = _pass
    }

    fun setEmail(_email: String){
        email.value = _email
    }

    fun setNewEmail(_newEmail: String){
        newEmail.value = _newEmail
    }
}