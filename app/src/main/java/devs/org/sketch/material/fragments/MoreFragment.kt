package devs.org.sketch.material.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import devs.org.sketch.material.activities.AboutActivity
import devs.org.sketch.material.activities.LocalLibActivity
import devs.org.sketch.material.activities.LottieFileActivity
import devs.org.sketch.material.databinding.FragmentMoreBinding

class MoreFragment : Fragment() {

    private lateinit var binding: FragmentMoreBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentMoreBinding.inflate(inflater,container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (isAdded){
            binding.apply {
                aboutApp.setOnClickListener {
                    goTo(AboutActivity::class.java)
                }
                localLibrary.setOnClickListener {
                    goTo(LocalLibActivity::class.java)
                }
                lottieAnimation.setOnClickListener {
                    goTo(LottieFileActivity::class.java)
                }
                more.setOnClickListener {
                    Snackbar.make(binding.root,"Coming Soon",Snackbar.LENGTH_SHORT).show()
                }

            }
        }
    }

    private fun goTo(java: Class<*>) {
        startActivity(Intent(requireContext(),java))
    }

}