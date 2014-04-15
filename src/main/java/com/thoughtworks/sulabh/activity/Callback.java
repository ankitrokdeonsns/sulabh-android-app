package com.thoughtworks.sulabh.activity;

import com.thoughtworks.sulabh.Loo;

import java.io.IOException;
import java.util.List;

public interface Callback<T> {
	public void execute(List<Loo> object) throws IOException;
}