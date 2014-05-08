package com.thoughtworks.sulabh.helper;

import com.thoughtworks.sulabh.model.Loo;

import java.io.IOException;
import java.util.List;

public interface Callback<T> {
	public void execute(List<Loo> object) throws IOException;
}