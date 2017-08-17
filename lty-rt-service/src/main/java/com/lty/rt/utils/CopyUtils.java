package com.lty.rt.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

public class CopyUtils {
	@SuppressWarnings("unchecked")
	public static <T> List<T> deepCopyList(List<T> src)
	{
	    List<T> dest = null;
	    try
	    {
	        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
	        ObjectOutputStream out = new ObjectOutputStream(byteOut);
	        out.writeObject(src);
	        ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
	        ObjectInputStream in = new ObjectInputStream(byteIn);
	        dest = (List<T>) in.readObject();
	    }
	    catch (IOException e)
	    {

	    }
	    catch (ClassNotFoundException e)
	    {

	    }
	    return dest;
	}
}
