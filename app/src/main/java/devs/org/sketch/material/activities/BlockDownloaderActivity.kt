package devs.org.sketch.material.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.browser.customtabs.CustomTabsIntent
import devs.org.sketch.material.R
import devs.org.sketch.material.databinding.ActivityBlockDownloaderBinding

class BlockDownloaderActivity : AppCompatActivity() {

    private lateinit var binding : ActivityBlockDownloaderBinding

    private var titleText = ""
    private var downloadUrl = ""
    private var dateText = ""
    private var descriptionText = ""

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBlockDownloaderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        titleText = intent.getStringExtra("title").toString()
        downloadUrl = intent.getStringExtra("downloadLink").toString()
        dateText = intent.getStringExtra("date").toString()
        descriptionText = intent.getStringExtra("description").toString()


        binding.apply {
            toolbar.title = titleText
            description.text = descriptionText.replace("ENTER","\n")
            date.text = "Date Added: $dateText"
            download.setOnClickListener {
                openNewChromeTab(downloadUrl)
            }

            toolbar.setOnMenuItemClickListener(Toolbar.OnMenuItemClickListener {
                when(it.itemId){
                    R.id.help ->{

                        help("I have a issue with this block : $titleText")

                        return@OnMenuItemClickListener true
                    }
                    R.id.report ->{
                        help("I want to report this block : $titleText")
                        return@OnMenuItemClickListener true
                    }
                    else ->{
                        return@OnMenuItemClickListener true
                    }
                }
            })

        }

    }



    private fun help(subject:String) {

        val recipient = "borthakurbinondi@gmail.com"
        val body = ""

        val intent = Intent(Intent.ACTION_SENDTO)
        intent.setData(Uri.parse("mailto:$recipient"))

        intent.putExtra(Intent.EXTRA_SUBJECT, subject)
        intent.putExtra(Intent.EXTRA_TEXT, body)
        startActivity(intent)

    }

    private fun openNewChromeTab( url: String) {
        val builder = CustomTabsIntent.Builder()
        val customTabsIntent = builder.build()
        customTabsIntent.launchUrl(this, Uri.parse(url))
    }
}