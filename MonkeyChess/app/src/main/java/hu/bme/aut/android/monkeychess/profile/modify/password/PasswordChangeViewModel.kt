package hu.bme.aut.android.monkeychess.profile.modify.password

import android.content.Context
import android.util.Patterns
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class PasswordChangeViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private var password: MutableLiveData<String> = MutableLiveData("")
    private var email: MutableLiveData<String> = MutableLiveData("")
    private var newPassword: MutableLiveData<String> = MutableLiveData("")
    private var confirmNewPassword: MutableLiveData<String> = MutableLiveData("")

    private fun changePassword(context: Context, navController: NavController){
        val user = auth.currentUser!!
        val credential = EmailAuthProvider.getCredential(email.value.toString(), password.value.toString())

        user.reauthenticate(credential).addOnCompleteListener {
            if(it.isSuccessful){
                user.updatePassword(newPassword.value.toString()).addOnCompleteListener { task->
                    if(task.isSuccessful){
                        auth.signOut()
                        navController.navigate("login_screen")
                        Toast.makeText(context, "Password changed, login to continue!", Toast.LENGTH_LONG).show()
                    }
                    else{
                        Toast.makeText(context, "Unable to change password!", Toast.LENGTH_LONG).show()
                    }
                }.addOnFailureListener{
                    Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                }
            }
        }.addOnFailureListener{
            Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
        }
    }

    fun isChangePasswordInputValid(context: Context, navController: NavController){
        if(password.value.toString().isNotEmpty() && newPassword.value.toString().length >=6 && Patterns.EMAIL_ADDRESS.matcher(email.value.toString()).matches() && newPassword.value.toString() == confirmNewPassword.value.toString()){
            changePassword(context, navController)
        }
        else{
            if(newPassword.value.toString().length > 6){
                Toast.makeText(context, "New password must be at least 6 characters long!", Toast.LENGTH_LONG).show()
            }else if(newPassword.value.toString() != confirmNewPassword.value.toString()){
                Toast.makeText(context, "New password must match!", Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(context, "Please provide your credentials!", Toast.LENGTH_LONG).show()
            }
        }
    }

    fun setPassword(_pass: String){
        password.value = _pass
    }

    fun setEmail(_email: String){
        email.value = _email
    }

    fun setNewPassword(_newPassword: String){
        newPassword.value = _newPassword
    }

    fun setConfirmNewPassword(_confirmNewPassword: String){
        confirmNewPassword.value = _confirmNewPassword
    }
}