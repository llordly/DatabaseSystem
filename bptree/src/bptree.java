import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.util.Random;
import java.util.StringTokenizer;

public class bptree {

	public static void main(String[] args) throws IOException, ClassNotFoundException {

		String index_file = null;
		String data_file = null;
		String output_file = null;
		
		int maxChild;
		
		BufferedWriter out = null;
		BufferedReader in = null;

		BufferedReader br = null;
		
		String line = null;
		StringTokenizer st;

		String command = args[0];

		switch (command) {
		case "-c":
			index_file = args[1];
			maxChild = Integer.parseInt(args[2]);
			out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(index_file)));
			out.write(String.valueOf(maxChild));
			out.close();
			break;
		case "-i":
			index_file = args[1];
			data_file = args[2];
			in = new BufferedReader(new InputStreamReader(new FileInputStream(index_file)));
			br = new BufferedReader(new InputStreamReader(new FileInputStream(data_file)));
			
			BPlusTree readInsertTree = new BPlusTree(Integer.parseInt(in.readLine()));
			
			while ((line = in.readLine()) != null) {
				st = new StringTokenizer(line, ",");
				readInsertTree.insertion(Integer.parseInt(st.nextToken()),
						Integer.parseInt(st.nextToken()));
			}
			in.close();
			
			while ((line = br.readLine()) != null) {
				st = new StringTokenizer(line, ",");
				readInsertTree.insertion(Integer.parseInt(st.nextToken()),
						Integer.parseInt(st.nextToken()));
			}
			
			out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(index_file)));
			readInsertTree.writeTree(out);
			out.close();
			in.close();
			br.close();
			break;
		case "-d":
			index_file = args[1];
			data_file = args[2];
			in = new BufferedReader(new InputStreamReader(new FileInputStream(index_file)));
			br = new BufferedReader(new InputStreamReader(new FileInputStream(data_file)));
			BPlusTree readDeleteTree = new BPlusTree(Integer.parseInt(in.readLine()));
			
			while ((line = in.readLine()) != null) {
				st = new StringTokenizer(line, ",");
				readDeleteTree.insertion(Integer.parseInt(st.nextToken()),
						Integer.parseInt(st.nextToken()));
			}
			in.close();
			
			while ((line = br.readLine()) != null) {
				readDeleteTree.delete(Integer.parseInt(line));
			}
			out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(index_file)));
			readDeleteTree.writeTree(out);
			out.close();
			in.close();
			break;
		case "-s":
			index_file = args[1];
			int searchKey = Integer.parseInt(args[2]);
			in = new BufferedReader(new InputStreamReader(new FileInputStream(index_file)));
			
			BPlusTree readSingleSearchTree = new BPlusTree(Integer.parseInt(in.readLine()));
			
			while ((line = in.readLine()) != null) {
				st = new StringTokenizer(line, ",");
				readSingleSearchTree.insertion(Integer.parseInt(st.nextToken()),
						Integer.parseInt(st.nextToken()));
			}
			
			if (readSingleSearchTree.search(searchKey) != -1)
				readSingleSearchTree.singleSearch(searchKey);
			else
				System.out.println("NOT FOUND");
			in.close();
			break;
		case "-r":
			index_file = args[1];
			int start_key = Integer.parseInt(args[2]);
			int end_key = Integer.parseInt(args[3]);
			in = new BufferedReader(new InputStreamReader(new FileInputStream(index_file)));
			BPlusTree readRangeSearchTree = new BPlusTree(Integer.parseInt(in.readLine()));
			
			while ((line = in.readLine()) != null) {
				st = new StringTokenizer(line, ",");
				readRangeSearchTree.insertion(Integer.parseInt(st.nextToken()),
						Integer.parseInt(st.nextToken()));
			}
			
			readRangeSearchTree.rangeSearch(start_key, end_key);
			in.close();
			break;
		case "-p":
			index_file = args[1];
			output_file = args[2];
			in = new BufferedReader(new InputStreamReader(new FileInputStream(index_file)));
			out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(output_file)));
			
			BPlusTree readPrintTree = new BPlusTree(Integer.parseInt(in.readLine()));
			
			while ((line = in.readLine()) != null) {
				st = new StringTokenizer(line, ",");
				readPrintTree.insertion(Integer.parseInt(st.nextToken()),
						Integer.parseInt(st.nextToken()));
			}
			readPrintTree.printTree(out);
			in.close();
			out.close();
		default:
			break;
		}
		
	}
}