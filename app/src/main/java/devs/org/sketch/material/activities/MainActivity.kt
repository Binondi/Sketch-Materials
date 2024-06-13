package devs.org.sketch.material.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Rect
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.ProgressBar
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import devs.org.sketch.material.R
import devs.org.sketch.material.dataModel.UpdateData
import devs.org.sketch.material.databinding.ActivityMainBinding
import devs.org.sketch.material.fragments.BlocksFragment
import devs.org.sketch.material.fragments.HomeFragment
import devs.org.sketch.material.fragments.MoreFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val home = "Home"
    private val blocks = "Blocks"
    private val more = "More"
    private var state = home
    private var isKeyboardVisible = false

    private var version = "1.0"
    private var shortDesc = "New Update Available"
    private var longDesc = "Bug Fixes"
    private var versionName = "1.0"
    private var link = ""
    private val intent = Intent()

    private val update = FirebaseDatabase.getInstance().reference.child("Update")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.main)
        binding.title.text = home
        replaceFragment(HomeFragment())

        try {
            val packageInfo = packageManager.getPackageInfo(packageName, 0)
            versionName = packageInfo.versionName

        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        // Add keyboard visibility listener
        binding.root.viewTreeObserver.addOnGlobalLayoutListener {
            val rect = Rect()
            binding.root.getWindowVisibleDisplayFrame(rect)
            val screenHeight = binding.root.height
            val keypadHeight = screenHeight - rect.bottom

            val isKeyboardOpen = keypadHeight > screenHeight * 0.15
            if (isKeyboardOpen != isKeyboardVisible) {
                isKeyboardVisible = isKeyboardOpen
                onKeyboardVisibilityChanged(isKeyboardVisible)
            }
        }

        checkPermissions()
        getUpdate()
        clickListeners()
    }

    private fun checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            if (Environment.isExternalStorageManager()){

                intent.setAction(android.provider.Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
                startActivity(intent)
            }
        }else{

        }
    }

    private fun getUpdate() {
        update.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                val updateData = mutableListOf<UpdateData>()

                for(snap in snapshot.children){
                    val data = snap.getValue(UpdateData::class.java)
                    data?.let {
                        updateData.add(it)
                        version = it.version
                        shortDesc = it.shortDesc
                        longDesc = it.longDesc
                        link = it.link
                    }


                }

                if (version.toDouble() > versionName.toDouble()) showUpdateDialog()

            }

            override fun onCancelled(error: DatabaseError) {
                // Handle onCancelled event if needed
            }
        })
    }

    @SuppressLint("QueryPermissionsNeeded", "MissingInflatedId")
    private fun showUpdateDialog() {
        if (!isFinishing && !isDestroyed) {
            val dialogView = LayoutInflater.from(this).inflate(R.layout.new_update, null)
            val dialogBuilder = MaterialAlertDialogBuilder(this)
                .setView(dialogView)
                .setCancelable(false)

            val dialog = dialogBuilder.create()
            val button = dialogView.findViewById<Button>(R.id.button)
            val progressBar = dialogView.findViewById<ProgressBar>(R.id.progressBar)

            button.setOnClickListener {

                progressBar.visibility = View.VISIBLE
                button.isClickable = false
                button.text = "Downloading..."
                openLink(link)
            }

            dialog.show()
        }
    }

    private fun openLink(link: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setData(Uri.parse(link))
        startActivity(intent)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun clickListeners() {
        binding.bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> {
                    replaceFragment(HomeFragment())
                    binding.title.text = home
                    binding.search.visibility = View.VISIBLE
                    state = home
                    hideKeyboard()
                    binding.search.visibility = View.VISIBLE
                    binding.searchCard.visibility = View.GONE
                    true
                }
                R.id.blocks -> {
                    replaceFragment(BlocksFragment())
                    binding.title.text = blocks
                    binding.search.visibility = View.VISIBLE
                    state = blocks
                    hideKeyboard()
                    binding.search.visibility = View.VISIBLE
                    binding.searchCard.visibility = View.GONE
                    true
                }
                else -> {
                    replaceFragment(MoreFragment())
                    binding.title.text = more
                    state = more
                    hideKeyboard()
                    binding.search.visibility = View.GONE
                    binding.searchCard.visibility = View.GONE
                    true
                }
            }
        }
        binding.search.setOnClickListener {
            binding.search.visibility = View.GONE
            binding.searchCard.visibility = View.VISIBLE
        }
        binding.cut.setOnClickListener {
            hideKeyboard()
            binding.search.visibility = View.VISIBLE
            binding.searchCard.visibility = View.GONE
            binding.bottomNav.visibility = View.VISIBLE
        }

        binding.edtSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val searchText = binding.edtSearch.text.toString()
                binding.edtSearch.setText("")
                search()
                hideKeyboard()
                binding.bottomNav.visibility = View.VISIBLE
                true
            } else {
                false
            }
        }

    }

    private fun search() {
        // Implement search functionality here
    }

    private fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val view = currentFocus ?: View(this)
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun replaceFragment(fragment: Fragment) {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.frameLayout)

        if (currentFragment == null || currentFragment::class.java != fragment::class.java) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.frameLayout, fragment)
                .commit()
        }
    }

    private fun onKeyboardVisibilityChanged(visible: Boolean) {
        if (visible) {
            binding.bottomNav.visibility = View.GONE
        } else {
            binding.bottomNav.visibility = View.VISIBLE
        }
    }
}
