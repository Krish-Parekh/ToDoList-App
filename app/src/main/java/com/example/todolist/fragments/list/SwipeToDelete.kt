package com.example.todolist.fragments.list

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView


/*
* Callback is A function that will execute later when some other function is completed executing.
*
* Real world example:
* You call your friend and you tell him to go to your house.
*
* And you also provide another function that your friend needs to do when he gets home. That is the callback.
*
* Pseudocode:
* callback = function what2doWhenReachedHome() {
*    Find my keys;
*    Get the pizza kept in the microwave
* }
* Friend.gotoHouse( callback )
*
*
*
* ItemTouchHelper :
* This is a utility class to add swipe to dismiss and drag & drop support to RecyclerView.
*
* It works with a RecyclerView and a Callback class, which configures what type of interactions
* are enabled and also receives events when user performs these actions.
*
* ItemTouchHelper.SimpleCallback
* A simple wrapper to the default Callback which you can construct with drag and swipe directions and this class will handle the flag callbacks.
*
* SimpleCallback (int dragDirs, int swipeDirs)
*
* Drag Direction :
* int: Binary OR of direction flags in which the Views can be dragged. Must be composed of ItemTouchHelper.LEFT, ItemTouchHelper.RIGHT,
* ItemTouchHelper.START, ItemTouchHelper.END, ItemTouchHelper.UP and ItemTouchHelper.DOWN.
*
* swipe Direction :
* int: Binary OR of direction flags in which the Views can be swiped. Must be composed of ItemTouchHelper.LEFT, ItemTouchHelper.RIGHT,
* ItemTouchHelper.START, ItemTouchHelper.END, ItemTouchHelper.UP and ItemTouchHelper.DOWN.
*
*
* onMove (RecyclerView: The RecyclerView to which the ItemTouchHelper is attached to. and  RecyclerView.ViewHolder: The ViewHolder for which the swipe direction is queried.)
* */

abstract class SwipeToDelete : ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT){
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }
}