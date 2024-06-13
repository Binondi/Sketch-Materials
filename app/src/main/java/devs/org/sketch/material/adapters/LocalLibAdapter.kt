package devs.org.sketch.material.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import devs.org.sketch.material.activities.downloaderActivity.LibDownloaderActivity
import devs.org.sketch.material.dataModel.LocalLib
import devs.org.sketch.material.databinding.LibItemsBinding

class LocalLibAdapter(var context: Context):ListAdapter<LocalLib,LocalLibAdapter.ViewHolder>(DiffUtil()) {

    class DiffUtil:androidx.recyclerview.widget.DiffUtil.ItemCallback<LocalLib>(){
        override fun areItemsTheSame(oldItem: LocalLib, newItem: LocalLib): Boolean {
            return oldItem.key == newItem.key
        }

        override fun areContentsTheSame(oldItem: LocalLib, newItem: LocalLib): Boolean {
            return oldItem == newItem
        }

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = LibItemsBinding.inflate(LayoutInflater.from(context),parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = getItem(position)
        holder.binding.apply {
            title.text = data.title
            bg.setOnClickListener {
                context.startActivity(
                    Intent(context, LibDownloaderActivity::class.java)
                        .putExtra("title",data.title)
                        .putExtra("key",data.key)
                        .putExtra("date",data.date)
                        .putExtra("downloadLink",data.downloadLink)
                        .putExtra("description",data.description)
                )
            }
        }
    }

    class ViewHolder(var binding: LibItemsBinding):RecyclerView.ViewHolder(binding.root)
}