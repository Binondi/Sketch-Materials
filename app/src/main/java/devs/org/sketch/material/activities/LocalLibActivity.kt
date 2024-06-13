package devs.org.sketch.material.activities

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import devs.org.sketch.material.adapters.LocalLibAdapter
import devs.org.sketch.material.dataModel.LocalLib
import devs.org.sketch.material.databinding.ActivityLocalLibBinding

class LocalLibActivity : AppCompatActivity() {

    private lateinit var binding:ActivityLocalLibBinding
    private val libReference = FirebaseDatabase.getInstance().reference.child("Libs")
    private lateinit var list: List<LocalLib>
    private lateinit var adapter: LocalLibAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLocalLibBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        setSupportActionBar(binding.toolbar)
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)
//        supportActionBar?.setDisplayShowHomeEnabled(true)

        list = ArrayList()
        adapter = LocalLibAdapter(this)
        binding.recyclerView.adapter = adapter
        getLibs()

        listeners()
    }

    private fun listeners() {
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        binding.swipeView.setOnRefreshListener {
            getLibs()
        }
    }

    private fun getLibs() {
        libReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val arr = mutableListOf<LocalLib>()
                for (dso in snapshot.children){
                    val data = dso.getValue(LocalLib::class.java)
                    if (data != null){
                        arr.add(data)
                    }
                }
                list = arr
                binding.swipeView.isRefreshing =false
                if (list.isNotEmpty()){
                    binding.noItem.visibility = View.GONE
                    binding.swipeView.visibility = View.VISIBLE
                    binding.loading.visibility = View.GONE
                    adapter.submitList(list)
                }else{
                    binding.noItem.visibility = View.VISIBLE
                    binding.swipeView.visibility = View.GONE
                    binding.loading.visibility = View.GONE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                //
            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}