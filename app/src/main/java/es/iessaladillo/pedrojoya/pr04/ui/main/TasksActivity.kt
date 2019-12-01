package es.iessaladillo.pedrojoya.pr04.ui.main

import android.app.Application
import android.content.ActivityNotFoundException
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.annotation.MenuRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.observe
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import es.iessaladillo.pedrojoya.pr04.R
import es.iessaladillo.pedrojoya.pr04.base.observeEvent
import es.iessaladillo.pedrojoya.pr04.data.LocalRepository
import es.iessaladillo.pedrojoya.pr04.data.Repository
import es.iessaladillo.pedrojoya.pr04.data.entity.Task
import es.iessaladillo.pedrojoya.pr04.utils.hideKeyboard
import es.iessaladillo.pedrojoya.pr04.utils.invisibleUnless
import es.iessaladillo.pedrojoya.pr04.utils.setOnSwipeListener
import kotlinx.android.synthetic.main.tasks_activity.*
import kotlinx.android.synthetic.main.tasks_activity_item.view.*


class TasksActivity : AppCompatActivity() {

    private val viewModel: TasksActivityViewModel by viewModels {
        TasksActivityViewModelFactory(LocalRepository, application)
    }

    private var mnuFilter: MenuItem? = null
    private var txtConcept: TextView? = null
    private var imgAddTask: ImageView? = null
    private val listAdapter: TasksActivityAdapter = TasksActivityAdapter().apply {
        setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(position: Int) {
                Toast.makeText(this@TasksActivity, getItem(position).completed.toString(), Toast.LENGTH_LONG).show()
                viewModel.updateTaskCompletedState(getItem(position), getItem(position).completed)
                observe()
            }
        })
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tasks_activity)
        setupViews()
        setupRecyclerView()
        observe()
    }

    private fun observe() {
        viewModel.tasks.observe(this) {
            showTasks(it)
        }
    }

    private fun setupRecyclerView() {
        lstTasks.run {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@TasksActivity)
            itemAnimator = DefaultItemAnimator()
            addItemDecoration(DividerItemDecoration(this@TasksActivity, RecyclerView.VERTICAL))
            adapter = listAdapter
            setOnSwipeListener()
        }
    }

    private fun setupViews() {
        txtConcept = ActivityCompat.requireViewById(this, R.id.txtConcept)
        imgAddTask = ActivityCompat.requireViewById(this, R.id.imgAddTask)
        imgAddTask!!.setOnClickListener { addTaskListener() }
        //imgAddTask!!.setOnClickListener{Toast.makeText(this, viewModel.tasks.value.toString(), Toast.LENGTH_LONG).show()} // For testing purposes
    }

    private fun addTaskListener() {
        var concept: String = txtConcept!!.text.toString()
        if (viewModel.isValidConcept(concept)) {
            viewModel.addTask(concept)
            txtConcept!!.hideKeyboard()
            txtConcept!!.text = ""
            observe()
        } else {
            Toast.makeText(this, getString(R.string.invalid_text), Toast.LENGTH_LONG).show()
        }
        /*var size = viewModel.tasks.value?.size
        Toast.makeText(this, "tamaño $size", Toast.LENGTH_LONG).show()*/
    }


    // TODO TEST Toast.makeText(this, txtConcept!!.text, Toast.LENGTH_LONG).show()

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_activity, menu)
        mnuFilter = menu.findItem(R.id.mnuFilter)
        //  checkMenuItem(mnuFilter!!.itemId)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.mnuShare -> viewModel.shareTasks()
            R.id.mnuDelete -> viewModel.deleteTasks()
            R.id.mnuComplete -> viewModel.markTasksAsCompleted()
            R.id.mnuPending -> viewModel.markTasksAsPending()
            R.id.mnuFilterAll -> viewModel.filterAll()
            R.id.mnuFilterPending -> viewModel.filterPending()
            R.id.mnuFilterCompleted -> viewModel.filterCompleted()
            else -> { observe()
                return super.onOptionsItemSelected(item)}
        }
        observe()
        return true
    }

     /*private fun checkMenuItem(@MenuRes menuItemId: Int) {
         Toast.makeText(this, menuItemId.toString(), Toast.LENGTH_LONG).show()

         lstTasks.post {
            val item = mnuFilter.findItem(menuItemId)
             item?.let { menuItem ->
                menuItem.isChecked = true
             }
         }
     }*/

    private fun showTasks(tasks: List<Task>) {
        lstTasks.post {
            listAdapter.submitList(tasks)
            lblEmptyView.invisibleUnless(tasks.isEmpty())
        }
    }

}
