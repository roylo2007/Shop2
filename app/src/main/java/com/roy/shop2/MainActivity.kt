package com.roy.shop2

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu
import android.view.MenuItem
import com.firebase.ui.auth.AuthMethodPickerLayout
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.util.*

class MainActivity : AppCompatActivity(), FirebaseAuth.AuthStateListener {

    private val RC_SIGNIN = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }

    override fun onAuthStateChanged(auth: FirebaseAuth) {
        val user = auth.currentUser
        if (user == null) {
            user_info.text = "No Login"
        } else {
            user_info.text = "${user?.displayName}, welcome!"
        }
    }

    override fun onStart() {
        super.onStart()
        FirebaseAuth.getInstance().addAuthStateListener(this)
    }

    override fun onStop() {
        super.onStop()
        FirebaseAuth.getInstance().removeAuthStateListener(this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            R.id.action_signin -> {
                val pickerLayout = AuthMethodPickerLayout.Builder(R.layout.sign_in)
                    .setEmailButtonId(R.id.image_email)
                    .setGoogleButtonId(R.id.image_google)
                    .build()
                val providers = Arrays.asList(
                    AuthUI.IdpConfig.GoogleBuilder().build(),
                    AuthUI.IdpConfig.EmailBuilder().build()
                )
                startActivityForResult(AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setAvailableProviders(providers)
                    .setIsSmartLockEnabled(false)
                    .setLogo(R.drawable.shop)
                    .setTheme(R.style.SignIn)
                    .setAuthMethodPickerLayout(pickerLayout)
                    .build(),
                    RC_SIGNIN
                )
                true
            }

            R.id.action_signout -> {
                AuthUI.getInstance().signOut(this)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
