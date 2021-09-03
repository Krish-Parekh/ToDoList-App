package com.example.todolist.fragments.add

import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.todolist.R
import com.example.todolist.data.models.Priority
import com.example.todolist.data.models.ToDoData
import com.example.todolist.data.viewmodel.SharedViewModel
import com.example.todolist.data.viewmodel.ToDoViewModel
import com.example.todolist.databinding.FragmentAddBinding


class AddFragment : Fragment() {
    private lateinit var binding: FragmentAddBinding
    private val mToDoViewModel : ToDoViewModel by viewModels()
    private val mSharedViewModel : SharedViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // set menu
        setHasOptionsMenu(true)
        // Inflate the layout for this fragment
        binding = FragmentAddBinding.inflate(inflater,container,false)
        binding.prioritiesSpinner.onItemSelectedListener = mSharedViewModel.listener
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_fragment_menu,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.menu_add)
        {
            insertDataToDb()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun insertDataToDb() {
        val mTitle = binding.titleEt.text.toString()
        val mPriority = binding.prioritiesSpinner.selectedItem.toString()
        val mDescription = binding.descriptionEt.text.toString()

        val validation = mSharedViewModel.verifyDataFromUser(mTitle,mDescription)
        if(validation) {
            // Insert Data to Database
            val newData = ToDoData(0,mTitle,mSharedViewModel.parsePriority(mPriority),mDescription)
            mToDoViewModel.insertData(newData)
            Toast.makeText(requireContext(),"Successfully added! ",Toast.LENGTH_SHORT).show()
            // Navigate Back
            findNavController().navigate(R.id.action_addFragment_to_listFragment)
        }else{
            Toast.makeText(requireContext(),"Please fill out every field",Toast.LENGTH_SHORT).show()
        }
    }


}