package devs.org.sketch.material.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import devs.org.sketch.material.adapters.HomeAdapter
import devs.org.sketch.material.dataModel.JavaCodes
import devs.org.sketch.material.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {

    private val database = FirebaseDatabase.getInstance()
    private val javaCodesReference = database.reference.child("Java Codes")
    private lateinit var list: List<JavaCodes>
    private lateinit var adapter: HomeAdapter
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentHomeBinding.inflate(inflater,container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (isAdded){
            list = ArrayList()
            adapter = HomeAdapter(requireContext())
            binding.homeRecycler.adapter = adapter
            getCodes()
            listeners()
        }
    }

    private fun listeners() {
        binding.swipeView.setOnRefreshListener {
            getCodes()
        }
    }

    private fun getCodes() {
        javaCodesReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val codes = mutableListOf<JavaCodes>()
                for (dso in snapshot.children){
                    val data = dso.getValue(JavaCodes::class.java)
                    if (data != null){
                        codes.add(data)
                    }
                }
                list = codes
                binding.swipeView.isRefreshing = false

                if (list.isNotEmpty()){
                    adapter.submitList(list)
                    binding.swipeView.visibility = View.VISIBLE
                    binding.noItem.visibility = View.GONE
                    binding.loading.visibility = View.GONE
                }else{
                    adapter.submitList(list)
                    binding.noItem.visibility = View.VISIBLE
                    binding.swipeView.visibility = View.GONE
                    binding.loading.visibility = View.GONE
                }


            }

            override fun onCancelled(error: DatabaseError) {
                binding.homeRecycler.visibility = View.VISIBLE
                binding.noItem.visibility = View.GONE
                binding.loading.visibility = View.GONE
                Snackbar.make(binding.root,"Something want wrong please try again",Snackbar.LENGTH_SHORT).show()
            }
        })
    }

}