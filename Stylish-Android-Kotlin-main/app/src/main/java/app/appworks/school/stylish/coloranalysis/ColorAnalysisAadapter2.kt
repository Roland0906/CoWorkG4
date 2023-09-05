package app.appworks.school.stylish.coloranalysis

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import app.appworks.school.stylish.data.Color
import app.appworks.school.stylish.databinding.ItemColorAnalysis2Binding
import app.appworks.school.stylish.databinding.ItemColorAnalysisBinding

class ColorAnalysisAdapter2(val viewModel: ColorAnalysisViewModel) : ListAdapter<Color, ColorAnalysisAdapter2.ColorViewHolder> (DiffCallback) {

    private lateinit var context: Context

    class ColorViewHolder(
        private val binding: ItemColorAnalysis2Binding,
        private val viewModel: ColorAnalysisViewModel
    ) : RecyclerView.ViewHolder(binding.root), LifecycleOwner {

        val isSelected: LiveData<Boolean> = viewModel.selectedColorPosition2.map {
            it == adapterPosition
        }

        fun bind(color: Color) {
            binding.lifecycleOwner = this
            binding.color = color
            binding.viewHolder2 = this
            binding.viewModel = viewModel
            binding.executePendingBindings()
        }

        private val lifecycleRegistry = LifecycleRegistry(this)

        init {
            lifecycleRegistry.currentState = Lifecycle.State.INITIALIZED
        }

        fun markAttach() {
            lifecycleRegistry.currentState = Lifecycle.State.STARTED
        }

        fun markDetach() {
            lifecycleRegistry.currentState = Lifecycle.State.DESTROYED
        }

        override val lifecycle: Lifecycle
            get() = lifecycleRegistry
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Color>() {
        override fun areItemsTheSame(oldItem: Color, newItem: Color): Boolean {
            return oldItem === newItem
        }
        override fun areContentsTheSame(oldItem: Color, newItem: Color): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorViewHolder {
        context = parent.context
        return ColorViewHolder(
            ItemColorAnalysis2Binding
                .inflate(LayoutInflater.from(parent.context), parent, false),
            viewModel
        )
    }

    override fun onBindViewHolder(holder: ColorViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onViewAttachedToWindow(holder: ColorViewHolder) {
        super.onViewAttachedToWindow(holder)
        holder.markAttach()
    }

    override fun onViewDetachedFromWindow(holder: ColorViewHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.markDetach()
    }


}