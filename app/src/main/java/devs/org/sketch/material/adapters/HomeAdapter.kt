package devs.org.sketch.material.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import devs.org.sketch.material.activities.CodeViewerActivity
import devs.org.sketch.material.dataModel.JavaCodes
import devs.org.sketch.material.databinding.CodeItemsBinding

class HomeAdapter(var context : Context): ListAdapter<JavaCodes, HomeAdapter.ViewHolder>(DiffUtil()) {

    class DiffUtil: androidx.recyclerview.widget.DiffUtil.ItemCallback<JavaCodes>(){
        override fun areItemsTheSame(oldItem: JavaCodes, newItem: JavaCodes): Boolean {
            return oldItem.key == newItem.key
        }

        override fun areContentsTheSame(oldItem: JavaCodes, newItem: JavaCodes): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(CodeItemsBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = getItem(position)
        holder.binding.apply {
            title.text = data.title
            bg.setOnClickListener {
                    val intent = Intent(context,CodeViewerActivity::class.java)
                    intent.putExtra("title",data.title)
                    intent.putExtra("code",data.code)
                    intent.putExtra("date",data.date)
                    intent.putExtra("description",data.codeDescription)
                context.startActivity(intent)
            }
        }
    }

    class ViewHolder(var binding: CodeItemsBinding):RecyclerView.ViewHolder(binding.root)
}