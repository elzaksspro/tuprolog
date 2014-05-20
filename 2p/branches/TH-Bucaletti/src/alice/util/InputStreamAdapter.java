package alice.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

public class InputStreamAdapter extends InputStream {
	
	private Reader reader;
	
	public InputStreamAdapter(Reader rd) {
		this.reader = rd;
	}

	@Override
	public int read() throws IOException {
		int x = reader.read();
		
		if(x == -1) return -1;
		else return x & 0xFF;
	}
	
	@Override
	public int available() {
		try {
			if(reader.ready()) return 1;
			else return 0;
		}
		catch(IOException ex) { return 0; }
	}
	
	@Override
	public void mark(int readLimit) {
		try {
			reader.mark(readLimit);
		}
		catch(IOException ex) {}
	}
	
	@Override
	public boolean markSupported() {
		return reader.markSupported();
	}
	
	@Override
	public void reset() throws IOException {
		reader.reset();
	}
	
	@Override
	public long skip(long n) throws IOException {
		return reader.skip(n);
	}
	
	@Override
	public void close() throws IOException {
		reader.close();
	}
	
}
