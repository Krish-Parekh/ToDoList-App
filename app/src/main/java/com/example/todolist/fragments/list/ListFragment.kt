package com.example.todolist.fragments.list

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.todolist.R
import com.example.todolist.data.models.ToDoData
import com.example.todolist.data.viewmodel.SharedViewModel
import com.example.todolist.data.viewmodel.ToDoViewModel
import com.example.todolist.databinding.FragmentListBinding
import com.example.todolist.fragments.list.adapter.ListAdapter
import com.example.todolist.utils.hideKeyBoard
import com.google.android.material.snackbar.Snackbar
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator


class ListFragment : Fragment() , SearchView.OnQueryTextListener{


    // for ViewBinding
    private lateinit var binding: FragmentListBinding

    // recycler view adapter
    private lateinit var adapter: ListAdapter


    /*
    * In simple words, you can understand "by" keyword as provided by.
    *
    * We are using delegation -> Delegation is the assignment of authority to another person to carry out specific activities.
    * here we are delegating the responsibility of viewModel to mToDoViewModel and mSharedViewModel
    * In kotlin we can delegate responsibility by using "by" keyword
    *
    * Two ways to initialize view model
    * private val mToDoViewModel: ToDoViewModel by viewModels()
    * or
    * private val viewModel = ViewModelProvider(this).get(ToDoViewModel::class.java)
    *
    * Both of them do the same thing, but there is a discriminative advantage for the first one.
    * Kotlin property delegation uses the idea of Lazy Initialization.
    *
    * In computer programming, lazy initialization is the tactic of delaying the creation of an object,
    * the calculation of a value, or some other expensive process until the first time it is needed.
    * It is a kind of lazy evaluation that refers specifically to the instantiation of objects or other resources.
    *
    * mToDoViewModel.getAllData.observe(viewLifecycleOwner)
    * is the first time that the viewModel field is touched, the instantiation of it will happen there. (creation of a ToDoViewModel instance)
    *  */
    private val mToDoViewModel: ToDoViewModel by viewModels()
    private val mSharedViewModel: SharedViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // set menu -> If true, the fragment has menu items to contribute.
        setHasOptionsMenu(true)
        /*
        * Inflating List Fragment layout
        * LayoutInflater is used to create a new View (or Layout) object from one of your xml layouts.
        * */
        binding = FragmentListBinding.inflate(inflater, container, false)

        // Recycler View
        setupRecyclerView()

        //Observing data from TodoViewModel
        mToDoViewModel.getAllData.observe(viewLifecycleOwner, Observer { data ->
            mSharedViewModel.checkIfDatabaseEmpty(data)
            adapter.setData(data)
        })

        //Observing data from SharedViewModel
        mSharedViewModel.emptyDatabase.observe(viewLifecycleOwner, Observer {
            showEmptyDatabaseView(it)
        })


        val floatingActionButton = binding.floatingActionButton2
        val listLayout = binding.listlayout

        // OnClickListener to navigate from list fragment to add fragment
        floatingActionButton.setOnClickListener {
            findNavController().navigate(R.id.action_listFragment_to_addFragment)
        }

        // hide keyboard
        hideKeyBoard(requireActivity())
        return binding.root
    }

    private fun setupRecyclerView() {
        val recyclerView = binding.recyclerView
        adapter = ListAdapter()
        recyclerView.adapter = adapter

        /*
        * StaggeredGridLayoutManager (int spanCount, int orientation)
        *
        * spanCount -> int: If orientation is vertical, spanCount is number of columns. If orientation is horizontal, spanCount is number of rows.
        *
        * orientation -> int: VERTICAL or HORIZONTAL
        * */
        recyclerView.layoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
        // Using third party library for recycler view animation link -> https://github.com/wasabeef/recyclerview-animators
        recyclerView.itemAnimator = SlideInUpAnimator().apply {
            addDuration = 300
        }

        // ToSwipe and delete view
        swipeToDelete(recyclerView)
    }


    // This function is implemented when database is empty to show the (Image and textView) -> which indicates database is empty
    private fun showEmptyDatabaseView(emptyDatabase: Boolean) {
        if (emptyDatabase) {
            binding.noDataImageView.visibility = View.VISIBLE
            binding.noDataTextView.visibility = View.VISIBLE
        } else {
            binding.noDataImageView.visibility = View.INVISIBLE
            binding.noDataTextView.visibility = View.INVISIBLE
        }
    }

    private fun swipeToDelete(recyclerView: RecyclerView) {
        /*
        * SwipeToDelete is a abstract class
        * object Keyword to declare single instance
        * swipeToDeleteCallBack -> anonymous object : SwipeToDelete
        * */
        val swipeToDeleteCallBack = object : SwipeToDelete() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val deleteItem = adapter.dataList[viewHolder.adapterPosition]

                // delete Item
                mToDoViewModel.deleteData(deleteItem)
                adapter.notifyItemRemoved(viewHolder.adapterPosition)

                // restore deleted Item
                restoreDeletedData(viewHolder.itemView, deleteItem)
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallBack)
        itemTouchHelper.attachToRecyclerView(recyclerView)

    }

    // we have created a snackBar which will reinsert deleted data inside database we get this deleted data from swipeToDelete Function
    private fun restoreDeletedData(view: View, deleteItem: ToDoData) {
        val snackbar = Snackbar.make(
            view,
            "Deleted : ${deleteItem.title}",
            Snackbar.LENGTH_LONG
        )
        snackbar.setAction("undo")
        {
            mToDoViewModel.insertData(deleteItem)
        }
        snackbar.show()
    }


    // You use onCreateOptionsMenu() to specify the options menu for an activity. In this method, you can inflate your menu resource (defined in XML) into the Menu provided in the callback.
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.list_fragment_menu, menu)
        val search = menu.findItem(R.id.menu_search)
        val searchView = search.actionView as? SearchView
        searchView?.isSubmitButtonEnabled = true
        searchView?.setOnQueryTextListener(this)
    }

    // Here we are sorting list data
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.menu_delete_all -> deleteAllItem()
            R.id.menu_priority_high -> mToDoViewModel.sortByHighPriority.observe(this, Observer { adapter.setData(it) })
            R.id.menu_priority_low -> mToDoViewModel.sortByLowPriority.observe(this, Observer { adapter.setData(it) })
        }
        return super.onContextItemSelected(item)
    }

    // Here we are searching when text is submitted
    override fun onQueryTextSubmit(query: String?): Boolean {
        if(query!=null)
        {
            searchThroughDatabase(query)
        }
        return true
    }


    // Here we are searching for individual text which is entered
    override fun onQueryTextChange(query: String?): Boolean {
        if(query != null)
        {
            searchThroughDatabase(query)
        }
        return true
    }


    /*
    * This function is used for searching in database we are sending string from SearchView and sending it %string%
    * %a% -> this will check every word in database
    * */
    private fun searchThroughDatabase(query: String) {
        val searchQuery = "%$query%"
        mToDoViewModel.searchDatabase(searchQuery).observe(this, Observer { list ->

            list?.let {
                adapter.setData(it)
            }

        })
    }


    // This will delete all item from list
    private fun deleteAllItem() {
        val builder = AlertDialog.Builder(requireContext())
            .setPositiveButton("yes") { _, _ ->
                mToDoViewModel.deleteAll()
                Toast.makeText(
                    requireContext(),
                    "Successfully deleted everything",
                    Toast.LENGTH_SHORT
                ).show()

            }
            .setNegativeButton("No") { _, _ -> }
            .setTitle("Delete Everything")
            .setMessage("Are you sure you want to remove everything")
            .create().show()
    }



}