package com.example.todolist.data

import androidx.room.TypeConverter
import com.example.todolist.data.models.Priority

class Convertor {

    @TypeConverter
    fun fromPriority(priority: Priority):String{
        return priority.name
    }

    fun toPriority(priority: String) : Priority {
        return Priority.valueOf(priority)
    }


}