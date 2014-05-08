package com.thoughtworks.sulabh.helper;

import java.io.IOException;

public interface AddCallback<T> {
	public void execute(Boolean isAdded) throws IOException;
}