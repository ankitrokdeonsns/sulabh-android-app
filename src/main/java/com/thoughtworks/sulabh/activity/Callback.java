package com.thoughtworks.sulabh.activity;

import org.codehaus.jackson.JsonParseException;

import java.io.IOException;

public interface Callback<T> {
    public void execute(T object) throws IOException;
}