package com.thoughtworks.sulabh;

import java.io.IOException;

public interface UpdateCallback<T> {
	public void execute(Boolean isAdded) throws IOException;
}