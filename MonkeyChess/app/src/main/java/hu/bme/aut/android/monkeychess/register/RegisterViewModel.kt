package hu.bme.aut.android.monkeychess.register

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class RegisterViewModel: ViewModel() {
    private var fullname: MutableLiveData<String> = MutableLiveData("")
    private var email: MutableLiveData<String> = MutableLiveData("")
    private var username: MutableLiveData<String> = MutableLiveData("")
    private var password: MutableLiveData<String> = MutableLiveData("")
    private var confirmPassword: MutableLiveData<String> = MutableLiveData("")

    private lateinit var auth: FirebaseAuth
    private lateinit var userDB: FirebaseFirestore

    private fun registerUser(context: Context, navController: NavController){
        auth = FirebaseAuth.getInstance()

        userDB = FirebaseFirestore.getInstance()

        auth.createUserWithEmailAndPassword(email.value.toString(), password.value.toString()).addOnCompleteListener {
            if(it.isSuccessful){


                val user = hashMapOf(
                    "Name" to fullname.value.toString(),
                    "E-mail" to email.value.toString(),
                    "Username" to username.value.toString(),
                    "Sign-up date" to dateFormatter()
                )

                userDB.collection("users")
                    .add(user)
                    .addOnSuccessListener{ documentReference->
                        Log.d("FireStore db:", "DocumentSnapshot added with ID: ${documentReference.id}")
                    }
                    .addOnFailureListener{ e->
                        Log.d("FireStore db: ", "Error adding document", e)
                    }
                navController.navigate("login_screen")
                Toast.makeText(context, "Successful Registration", Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(context, it.exception?.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    // TODO: Megerősítő e-mail a szűréshez
    fun isRegistrationInputValid(context: Context, navController: NavController){
        if(fullname.value.toString().isNotEmpty() && username.value.toString().length >= 5 && password.value.toString().length >= 6 && (email.value.toString().isNotEmpty() || Patterns.EMAIL_ADDRESS.matcher(email.value.toString()).matches()) && confirmPassword.value.toString() == password.value.toString()){
            registerUser(context, navController)
        }
        else{
            if(fullname.value.toString().isEmpty()){
                Toast.makeText(context, "Fullname must not be empty!", Toast.LENGTH_LONG).show()
            }  else if(email.value.toString().isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email.value.toString()).matches()){
                Toast.makeText(context, "Please enter a valid e-mail address!", Toast.LENGTH_LONG).show()
            }else if(username.value.toString().length < 5){
                Toast.makeText(context,  "Username must be at least 5 characters long!", Toast.LENGTH_LONG).show()
            } else if(password.value.toString().length < 6){
                Toast.makeText(context, "Password must be at least 6 characters long!", Toast.LENGTH_LONG).show()
            }
            else{
                Toast.makeText(context, "Passwords must match!", Toast.LENGTH_LONG).show()
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun dateFormatter(): String {
        val sdf = SimpleDateFormat("yyyy.MM.dd")
        return sdf.format(Date())
    }

    fun setFullname(_fullname: String){
        fullname.value = _fullname
    }

    fun setEmail(_email: String) {
        email.value = _email
    }

    fun setUsername(_username: String){
        username.value = _username
    }

    fun setPassword(_password: String) {
        password.value = _password
    }

    fun setConfirmpassword(_confirmpassword: String){
        confirmPassword.value = _confirmpassword
    }

    fun getFullname(): String?{
        return fullname.value
    }

    fun getEmail(): String?{
        return email.value
    }

    fun getUsername(): String?{
        return username.value
    }

    fun getPassword(): String?{
        return password.value
    }



}