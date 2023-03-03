package fr.m2i.bddtodolist.model;

import java.util.LinkedList;

public class Todolist {
    private LinkedList<Todo> todolist;

    public Todolist() {
        this.todolist = new LinkedList<>();
    }

    public LinkedList<Todo> getTodo() {
        return this.todolist;
    }

}