package ru.ikon.webviewapp

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import android.webkit.CookieManager
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.edit
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import dagger.hilt.android.AndroidEntryPoint
import ru.ikon.webviewapp.news.NewsFragment
import java.util.*

const val APP_PREFERENCES = "mysettings"
const val APP_PREFERENCES_SITE = "APP_PREFERENCES_SITE"
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {


    private lateinit var onBackPressed: OnBackPressed

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE).contains(APP_PREFERENCES_SITE) && getSharedPreferences(
                APP_PREFERENCES, MODE_PRIVATE).getString(APP_PREFERENCES_SITE, "") != "") {
            if (isOnline(this)) {
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.container, WebViewFragment())
                    .commit()
            } else {
                findViewById<TextView>(R.id.text).visibility = View.VISIBLE
            }
        } else {
            if (isOnline(this)) {
                var remoteConfig = Firebase.remoteConfig
                val configSettings = remoteConfigSettings {
                    minimumFetchIntervalInSeconds = 0L
                }
                remoteConfig.setConfigSettingsAsync(configSettings)
                remoteConfig.setDefaultsAsync(R.xml.default_config)

                remoteConfig.fetchAndActivate()
                    .addOnCompleteListener(this) {
                        if (it.isComplete) {
                            Toast.makeText(applicationContext, "Fetch", Toast.LENGTH_SHORT).show()
                        }
                    }

                if (!checkIsEmu() && remoteConfig.getString("site") != "") {
                    getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE)?.edit {
                        putString(APP_PREFERENCES_SITE, remoteConfig.getString("site"))
                    }

                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.container, WebViewFragment())
                        .commit()
                } else {
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.container, NewsFragment())
                        .commit()
                }
            }
        }


    }

    fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager != null) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                    return true
                }
            }
        }
        return false
    }

    override fun onBackPressed() {
        onBackPressed.onClick()
    }

    private fun checkIsEmu(): Boolean {
        if (BuildConfig.DEBUG) return false // when developer use this build on emulator
        val phoneModel = Build.MODEL
        val buildProduct = Build.PRODUCT
        val buildHardware = Build.HARDWARE
        var brand = Build.BRAND;
        var result = (Build.FINGERPRINT.startsWith("generic")
                || phoneModel.contains("google_sdk")
                || phoneModel.lowercase(Locale.getDefault()).contains("droid4x")
                || phoneModel.contains("Emulator")
                || phoneModel.contains("Android SDK built for x86")
                || Build.MANUFACTURER.contains("Genymotion")
                || buildHardware == "goldfish"
                || Build.BRAND.contains("google")
                || buildHardware == "vbox86"
                || buildProduct == "sdk"
                || buildProduct == "google_sdk"
                || buildProduct == "sdk_x86"
                || buildProduct == "vbox86p"
                || Build.BOARD.lowercase(Locale.getDefault()).contains("nox")
                || Build.BOOTLOADER.lowercase(Locale.getDefault()).contains("nox")
                || buildHardware.lowercase(Locale.getDefault()).contains("nox")
                || buildProduct.lowercase(Locale.getDefault()).contains("nox"))
        if (result) return true
        result = result or (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
        if (result) return true
        result = result or ("google_sdk" == buildProduct)
        return result
    }

    fun setOnBackPressedListener(onBackPressed: OnBackPressed) {
        this.onBackPressed = onBackPressed
    }

}