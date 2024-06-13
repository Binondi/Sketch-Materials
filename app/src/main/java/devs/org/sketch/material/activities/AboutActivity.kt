package devs.org.sketch.material.activities

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import devs.org.sketch.material.R
import devs.org.sketch.material.databinding.ActivityAboutBinding

class AboutActivity : AppCompatActivity() {

    private lateinit var binding:ActivityAboutBinding
    private var packageNamee:String = "8052302931795159064"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val savedTheme = sharedPreferences.getInt("selectedTheme", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        val currentTheme = AppCompatDelegate.getDefaultNightMode()

        if (savedTheme != currentTheme) {
            AppCompatDelegate.setDefaultNightMode(savedTheme)
            updateUIForTheme(savedTheme)
        }

        when (savedTheme) {
            AppCompatDelegate.MODE_NIGHT_NO -> binding.radioGroup.check(R.id.radioButtonLight)
            AppCompatDelegate.MODE_NIGHT_YES -> binding.radioGroup.check(R.id.radioButtonDark)
            AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM -> binding.radioGroup.check(R.id.radioButtonSystemDefault)
        }

        clickListeners()
        getPackageInfo()
    }

    private fun isChromeInstalled(): Boolean {
        val packageName = "com.android.chrome"
        return try {
            packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }

    private fun openInChrome(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        intent.`package` = "com.android.chrome"
        startActivity(intent)
    }

    private fun openInDefaultBrowser(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }


    private fun openInstagram(username:String){
        val packageName = "com.instagram.android"

        try {

            var intent = packageManager.getLaunchIntentForPackage(packageName)
            if (intent != null) {
                val uri = Uri.parse("https://www.instagram.com/$username")
                intent = Intent(Intent.ACTION_VIEW, uri)
                intent.setPackage("com.instagram.android")
                startActivity(intent)
            } else {
                val uri = Uri.parse("https://www.instagram.com/$username")
                intent = Intent(Intent.ACTION_VIEW, uri)
                intent.setPackage("com.android.chrome")
                startActivity(intent)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getPackageInfo(){
        try {
            val packageInfo = packageManager.getPackageInfo(packageName, 0)
            val versionCode = packageInfo.versionName

            binding.versionCode.text = versionCode.toString()
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
    }

    @SuppressLint("ObsoleteSdkInt")
    private fun statusBar(){
        val typedValue = android.util.TypedValue()
        theme.resolveAttribute(android.R.attr.windowBackground, typedValue, true)
        val defaultBackgroundColor = typedValue.data
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = defaultBackgroundColor
        }
    }

    private fun clickListeners(){
        binding.instagram.setOnClickListener{
            val username = "the_devs_org"
            openInstagram(username)
        }
        binding.rateUs.setOnClickListener{
            rateApp()
        }
        binding.email.setOnClickListener{
            openEmail()
        }
        binding.moreApps.setOnClickListener{
            moreApps()
        }
        binding.privacy.setOnClickListener{
            privacy()
        }
        binding.developer.setOnClickListener{
            val username = "b_borthakur07"
            openInstagram(username)
        }

        binding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.radioButtonLight -> setAppTheme(AppCompatDelegate.MODE_NIGHT_NO)
                R.id.radioButtonDark -> setAppTheme(AppCompatDelegate.MODE_NIGHT_YES)
                R.id.radioButtonSystemDefault -> setAppTheme(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            }
        }

        binding.back.setOnClickListener {
            finish()
        }

    }

    private fun privacy() {
        val url = "https://binondiborthakur56.wixsite.com/splashzone"

        if (isChromeInstalled()) {
            openInChrome(url)
        } else {
            openInDefaultBrowser(url)
        }
    }

    private fun setAppTheme(mode: Int) {
        AppCompatDelegate.setDefaultNightMode(mode)
        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().putInt("selectedTheme", mode).apply()
        updateUIForTheme(mode)
    }


    private fun updateUIForTheme(mode: Int) {
        when (mode) {
            AppCompatDelegate.MODE_NIGHT_NO -> {
            }
            AppCompatDelegate.MODE_NIGHT_YES -> {
            }
            AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM -> {
            }
        }
    }



    private fun moreApps() {
        val uri = Uri.parse("market://dev?id=$packageNamee")
        val goToMarket = Intent(Intent.ACTION_VIEW, uri)
        try {
            startActivity(goToMarket)
        } catch ( e: ActivityNotFoundException) {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/dev?id=$packageNamee")))
        }
    }

    private fun openEmail() {
        val recipient = "borthakurbinondi@gmail.com"
        val subject = "I have an issue"
        val body = ""

        val intent = Intent(Intent.ACTION_SENDTO)
        intent.setData(Uri.parse("mailto:$recipient"))

        intent.putExtra(Intent.EXTRA_SUBJECT, subject)
        intent.putExtra(Intent.EXTRA_TEXT, body)
        startActivity(intent)
    }

    private fun rateApp() {
        val uri = Uri.parse("market://details?id=$packageName")
        val goToMarket = Intent(Intent.ACTION_VIEW, uri)
        try {
            startActivity(goToMarket)
        } catch ( e: ActivityNotFoundException) {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=$packageName")))
        }
    }
}