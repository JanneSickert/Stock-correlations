package parser;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public interface ExportData {
	default public void writeFile(String path, ArrayList<String> text) {
		File ff = new File(path);
		if (!(ff.exists())) {
			try {
				ff.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			FileWriter fwf = new FileWriter(ff, false);
			for (int i = 0; i < text.size(); i++) {
				fwf.write(text.get(i));
				fwf.write("\n");
			}
			fwf.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	default public void write() {
	}
}
