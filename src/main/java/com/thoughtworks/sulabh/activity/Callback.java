package com.thoughtworks.sulabh.activity;

public interface Callback<T> {
    public void execute(T object);
}