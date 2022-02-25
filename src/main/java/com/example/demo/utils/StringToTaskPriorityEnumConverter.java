package com.example.demo.utils;

import com.example.demo.task.TaskPriority;
import org.springframework.core.convert.converter.Converter;

public class StringToTaskPriorityEnumConverter implements Converter<String, TaskPriority> {

    @Override
    public TaskPriority convert(String s) {

        return TaskPriority.valueOf(s.toUpperCase());
    }
}
