package fastcampus.part1.fc_1_chapter8

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import fastcampus.part1.fc_1_chapter8.databinding.ItemFrameBinding

class FrameAdapter(private val list: List<FrameItem>) : RecyclerView.Adapter<FrameViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FrameViewHolder {
        val inflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val binding = ItemFrameBinding.inflate(inflater, parent, false)
        return FrameViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FrameViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size
}

class FrameViewHolder(private val binding : ItemFrameBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: FrameItem) {
        binding.frameImageView.setImageURI(item.uri)
    }
}