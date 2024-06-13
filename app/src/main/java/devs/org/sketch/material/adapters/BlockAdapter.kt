package devs.org.sketch.material.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import devs.org.sketch.material.activities.downloaderActivity.BlockDownloaderActivity
import devs.org.sketch.material.dataModel.Blocks
import devs.org.sketch.material.databinding.BlockItemsBinding

class BlockAdapter(var context: Context):ListAdapter<Blocks,BlockAdapter.ViewPager>(DiffUtil()) {

    class DiffUtil:androidx.recyclerview.widget.DiffUtil.ItemCallback<Blocks>(){
        override fun areItemsTheSame(oldItem: Blocks, newItem: Blocks): Boolean {
            return oldItem.key == newItem.key
        }

        override fun areContentsTheSame(oldItem: Blocks, newItem: Blocks): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPager {
        return ViewPager(BlockItemsBinding.inflate(LayoutInflater.from(context),parent, false))
    }

    override fun onBindViewHolder(holder: ViewPager, position: Int) {
        val data = getItem(position)
        holder.binding.apply {
            title.text = data.title
            bg.setOnClickListener {
                context.startActivity(Intent(context, BlockDownloaderActivity::class.java)
                    .putExtra("title",data.title)
                    .putExtra("key",data.key)
                    .putExtra("date",data.date)
                    .putExtra("downloadLink",data.downloadLink)
                    .putExtra("description",data.description)
                )
            }
        }
    }

    class ViewPager(var binding: BlockItemsBinding):ViewHolder(binding.root)
}