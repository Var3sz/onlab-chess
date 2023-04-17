package hu.bme.aut.android.monkeychess.profile

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class ProfileViewModel : ViewModel() {
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var userDB: FirebaseFirestore = FirebaseFirestore.getInstance()
    private var usernameLiveData = MutableLiveData<String>()
    private var fullnameLiveData = MutableLiveData<String>()
    private var emailLiveData = MutableLiveData<String>()
    private var accCreatedAtLiveData = MutableLiveData<String>()

    /** For Firebase storage **/
    private val storageRef = Firebase.storage.reference
    private var imageUrlLiveData = MutableLiveData<String>()

    /** For delete profile reauthentication**/
    private var password: MutableLiveData<String> = MutableLiveData("")
    private var email: MutableLiveData<String> = MutableLiveData("")

    init {
        showProfileData()
    }

    private fun showProfileData() {
        if (auth.currentUser != null) {
            userDB.collection("users").get().addOnCompleteListener {
                if (it.isSuccessful) {
                    for (document in it.result) {
                        if (document.data.getValue("E-mail") == auth.currentUser?.email) {
                            setUsername(document.data.getValue("Username").toString())
                            setFullname(document.data.getValue("Name").toString())
                            setEmail(document.data.getValue("E-mail").toString())
                            setAccCreatedAt(document.data.getValue("Sign-up date").toString())
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
            setUsername("")
            setFullname("")
            setEmail("")
            setAccCreatedAt("")
            setImageUrl("")
        }
    }

    private fun setUsername(string: String) {
        usernameLiveData.value = string
    }

    private fun setFullname(string: String) {
        fullnameLiveData.value = string
    }

    private fun setAccCreatedAt(string: String) {
        accCreatedAtLiveData.value = string
    }

    private fun setEmail(string: String) {
        emailLiveData.value = string
    }

    private fun setImageUrl(string: String){
        imageUrlLiveData.value = string
    }

    fun getUsername(): MutableLiveData<String> {
        return usernameLiveData
    }

    fun getFullname(): MutableLiveData<String> {
        return fullnameLiveData
    }

    fun getEmail(): MutableLiveData<String> {
        return emailLiveData
    }

    fun getAccCreatedAt(): MutableLiveData<String> {
        return accCreatedAtLiveData
    }

    fun getImageUrl(): MutableLiveData<String>{
        return imageUrlLiveData
    }

    fun uploadPicture(uri: Uri?, context: Context){
        val sd = getFileName(context.applicationContext, uri!!)
        val uploadTask = storageRef.child("profile-pictures/$sd").putFile(uri)
        uploadTask.addOnSuccessListener {
            storageRef.child("profile-pictures/$sd").downloadUrl.addOnCompleteListener {
                userDB.collection("users").get().addOnCompleteListener { task ->
                    if(task.isSuccessful){
                        for(document in task.result){
                            if(document.data.getValue("E-mail") == auth.currentUser?.email){
                                userDB.collection("users").document(document.id).update("ImageURL", it.result.toString())
                            }
                        }
                    }
                    setImageUrl(it.result.toString())
                }.addOnFailureListener{ Toast.makeText(context, it.message, Toast.LENGTH_LONG).show() }
            }.addOnFailureListener{ Toast.makeText(context, it.message, Toast.LENGTH_LONG).show() }
        }.addOnFailureListener{ Toast.makeText(context, it.message, Toast.LENGTH_LONG).show() }
    }

    private fun getFileName(context: Context, uri: Uri): String? {
        if (uri.scheme == "content") {
            val cursor = context.contentResolver.query(uri, null, null, null, null)
            cursor.use {
                if (cursor != null) {
                    if(cursor.moveToFirst()) {
                        return cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)+1)
                    }
                }
            }
        }
        return uri.path?.lastIndexOf('/')?.let { uri.path?.substring(it) }
    }

    private fun deleteAccount(context: Context, navController: NavController) {
        val user = auth.currentUser!!
        val credential =
            EmailAuthProvider.getCredential(email.value.toString(), password.value.toString())

        if (email.value == auth.currentUser?.email) {
            user.reauthenticate(credential).addOnCompleteListener {
                if (it.isSuccessful) {
                    userDB.collection("users").get().addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            for (document in task.result) {
                                if (document.data.getValue("E-mail") == auth.currentUser?.email) {
                                    userDB.collection("users").document(document.id).delete()
                                }
                            }
                        }
                    }.addOnFailureListener { e ->
                        Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
                    }

                    user.delete().addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            auth.signOut()
                            navController.navigate("welcome_screen")
                            Toast.makeText(context, "User deleted", Toast.LENGTH_LONG).show()
                        } else {
                            Toast.makeText(context, task.exception?.message, Toast.LENGTH_LONG)
                                .show()
                        }
                    }.addOnFailureListener { e ->
                        Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
                    }
                }
            }.addOnFailureListener {
                Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
            }
        } else {
            Toast.makeText(context, "The given e-mail address is incorrect!", Toast.LENGTH_LONG)
                .show()
        }
    }

    fun isDeleteInputValid(context: Context, navController: NavController) {
        if (password.value.toString()
                .isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email.value.toString()).matches()
        ) {
            deleteAccount(context, navController)
        } else {
            Toast.makeText(context, "Please provide your credentials!", Toast.LENGTH_LONG).show()
        }
    }

    fun reAuthPassword(_pass: String) {
        password.value = _pass
    }

    fun reAuthEmail(_email: String) {
        email.value = _email
    }
}
