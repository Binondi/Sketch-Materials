package devs.org.sketch.material.activities.viewerActivity

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import devs.org.sketch.material.R
import devs.org.sketch.material.databinding.ActivityCodeViewerBinding

class CodeViewerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCodeViewerBinding
    private var titleText = ""
    private var codeText = ""
    private var dateText = ""
    private var descriptionText = ""

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCodeViewerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        titleText = intent.getStringExtra("title").toString()
        codeText = intent.getStringExtra("code").toString().replace("ENTER","\n")
        dateText = intent.getStringExtra("date").toString()
        descriptionText = intent.getStringExtra("description").toString()
        binding.apply {
            toolbar.title = titleText
            code.text = codeText
            date.text = "Date Added: $dateText"
            codeDescription.text = descriptionText
            copy.setOnClickListener {
                copyCode(codeText)
            }
            share.setOnClickListener {
                shareCode(codeText)
            }

            toolbar.setOnMenuItemClickListener(Toolbar.OnMenuItemClickListener {
                when(it.itemId){
                    R.id.help ->{

                        help("I have a issue with this code : $titleText")

                        return@OnMenuItemClickListener true
                    }
                    R.id.report ->{
                        help("I want to report this code : $titleText")
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
    private fun shareCode(text: String) {
        // Create an intent to share the text
        val shareIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, text)
            type = "text/plain"
        }

        val chooser = Intent.createChooser(shareIntent, "Share this code to")
        startActivity(chooser)
    }

    private fun copyCode(text: String) {

        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

        val clip = ClipData.newPlainText("label", text)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(this, "Text copied to clipboard", Toast.LENGTH_SHORT).show()

    }


}