package devs.org.sketch.material.fragments

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
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
        binding.edtSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val searchText = binding.edtSearch.text.toString()
                searchItems(searchText)


                hideKeyboard()
                true
            } else {
                false
            }
        }
        binding.edtSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                if (binding.edtSearch.text.isNotEmpty()){
                    searchItems(binding.edtSearch.text.toString())
                    binding.cut.visibility = View.VISIBLE
                }else{
                    adapter.submitList(list)
                    binding.cut.visibility = View.GONE
                    binding.noItem.visibility = View.GONE
                    binding.swipeView.visibility = View.VISIBLE
                }
            }
        })
        binding.cut.setOnClickListener {
            binding.edtSearch.setText("")
            binding.cut.visibility = View.GONE
            if (list.isNotEmpty()){
                adapter.submitList(list)
                binding.noItem.visibility = View.GONE
                binding.swipeView.visibility = View.VISIBLE
            }else{
                binding.noItem.visibility = View.VISIBLE
                binding.swipeView.visibility = View.GONE
            }

        }
    }

    private fun searchItems(search: String) {
        val filteredList = list.filter {
            it.title.contains(search, ignoreCase = true)
        }
        if (filteredList.isNotEmpty()){
            adapter.submitList(filteredList)
            binding.noItem.visibility = View.GONE
            binding.swipeView.visibility = View.VISIBLE
        }else{
            binding.noItem.visibility = View.VISIBLE
            binding.swipeView.visibility = View.GONE
        }

    }
    private fun hideKeyboard() {
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val view = activity?.currentFocus ?: View(requireContext())
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}