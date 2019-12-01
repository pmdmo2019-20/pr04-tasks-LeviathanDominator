package es.iessaladillo.pedrojoya.pr04.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import es.iessaladillo.pedrojoya.pr04.R
import es.iessaladillo.pedrojoya.pr04.data.entity.Task
import es.iessaladillo.pedrojoya.pr04.utils.strikeThrough
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.tasks_activity_item.*


interface OnItemClickListener {
    fun onItemClick(position: Int)
}

class TasksActivityAdapter : RecyclerView.Adapter<TasksActivityAdapter.ViewHolder>() {

    private var data: List<Task> = emptyList()
    private var onItemClickListener: OnItemClickListener? = null

    init {
        setHasStableIds(true)
    }

    fun getItem(position: Int) = data[position]

    class ViewHolder(override val containerView: View, onItemClickListener: OnItemClickListener?) :
        RecyclerView.ViewHolder(containerView), LayoutContainer {

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClickListener?.onItemClick(position)
                }
            }
            chkCompleted.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClickListener?.onItemClick(position)
                }
            }
        }

        @Suppress("DEPRECATION")
        fun bind(task: Task) {
            lblConcept.text = task.concept
            if (task.completed) {
                lblCompleted.text = String.format("Completed at %s", task.completedAt)
                viewBar.setBackgroundColor(containerView.context.resources.getColor(R.color.colorCompletedTask))
            } else {
                lblCompleted.text = String.format("Created at %s", task.createdAt)
                viewBar.setBackgroundColor(containerView.context.resources.getColor(R.color.colorPendingTask))
            }
            chkCompleted.isChecked = task.completed
            lblConcept.strikeThrough(task.completed)
        }
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemView = layoutInflater.inflate(R.layout.tasks_activity_item, parent, false)
        return ViewHolder(itemView, onItemClickListener)
    }

    override fun getItemCount(): Int = data.size

    override fun getItemId(position: Int): Long {
        return data[position].id
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }

    fun submitList(tasks: List<Task>) {
        data = tasks
        notifyDataSetChanged()
    }
}
