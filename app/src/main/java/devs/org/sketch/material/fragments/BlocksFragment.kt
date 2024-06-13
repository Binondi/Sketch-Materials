package devs.org.sketch.material.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import devs.org.sketch.material.adapters.BlockAdapter
import devs.org.sketch.material.dataModel.Blocks
import devs.org.sketch.material.databinding.FragmentBlocksBinding

class BlocksFragment : Fragment() {

    private lateinit var binding: FragmentBlocksBinding
    private val blockReference = FirebaseDatabase.getInstance().reference.child("Blocks")
    private lateinit var adapter: BlockAdapter
    private lateinit var list: List<Blocks>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBlocksBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (isAdded){
            list = ArrayList()
            adapter = BlockAdapter(requireContext())
            binding.blockRecycler.adapter = adapter
            listeners()
            getBlocks()
        }
    }

    private fun getBlocks() {
        blockReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val blockList = mutableListOf<Blocks>()
                for (dso in snapshot.children){
                    val data = dso.getValue(Blocks::class.java)
                    if (data != null) blockList.add(data)
                }
                list = blockList
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
                binding.blockRecycler.visibility = View.VISIBLE
                binding.noItem.visibility = View.GONE
                binding.loading.visibility = View.GONE
                Snackbar.make(binding.root,"Something want wrong please try again", Snackbar.LENGTH_SHORT).show()
            }
        })
    }

    private fun listeners() {
        binding.swipeView.setOnRefreshListener {
            getBlocks()
        }
    }

}