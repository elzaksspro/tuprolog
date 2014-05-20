package alice.util;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

public class OutputStreamAdapter extends OutputStream {
	
	private Writer writer;
	
	public OutputStreamAdapter(Writer wr) {
		this.writer = wr;
	}

	@Override
	public void write(int b) throws IOException {
		if(b == -1) 
			writer.write(-1);
		else 
			writer.write(0xFF & b);
		
		flush();
	}
	
	@Override
	public void flush() throws IOException {
		writer.flush();
	}
	
	@Override
	public void close() throws IOException {
		flush();
		writer.close();
	}
}
