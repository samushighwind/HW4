import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

/*
 * This class creates and write the first two constraints.
 */
public class Client {

	final static String INPUT_FILE = "spring-2015.csv";
	final static String OUTPUT_FILE_1 = "constraint1.txt";
	final static String OUTPUT_FILE_2 = "constraint2.txt";
	final static String OUTPUT_FILE_3 = "constraint3.txt";
	final static String OUTPUT_FILE_4 = "constraint4.txt";
	
	public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
		
		Scanner scan = new Scanner(new File(INPUT_FILE));
		scan.nextLine();//skip the header line;
		List<Request> requests = new LinkedList<Request>();
		int i = 0;
		while(scan.hasNext()){
			String[] line = scan.next().split(",");
			int id = Integer.parseInt(line[0]);
			int crn = Integer.parseInt(line[2]);
			int tree = Integer.parseInt(line[3]);
			int branch = Integer.parseInt(line[4]);
			int ceil = Integer.parseInt(line[5]);
			String subject = line[8];
			int numb = Integer.parseInt(line[9].substring(0, 3)); //removes the "S" that sometimes appears.
			char seq = line[10].charAt(0);		
		
			requests.add(new Request(i, id, crn, tree, branch, ceil, numb, seq, subject));
			i++;
		}
		scan.close();
		
		getConstraint1(requests);
		getConstraint2(requests);
        getConstraint3(requests);
		getConstraint4(requests);
	}
	
	/*
	 * Writes the 2nd constraint to a file. This constraint ensures that courses do not exceed the course ceiling. 
	 * Courses with a ceiling of 0 (i.e. an independent study) are not included in the constraint.
	 */
	private static void getConstraint2(List<Request> requests) throws FileNotFoundException, UnsupportedEncodingException {
		System.out.println("Creating constraint 2: ensure courses do not exceed ceilings...");
		PrintWriter writer = new PrintWriter(OUTPUT_FILE_2, "UTF-8");
		Set<Integer> requestUsed = new HashSet<Integer>();
		
		for(Request i : requests){
			String curr = "";
			for(Request j : requests){
				if(i.getCrn()==j.getCrn()){
					if(!requestUsed.contains(j.getVarNum()) && !requestUsed.contains(i.getVarNum()) && i!=j && j.getCeil()!=0){
						curr+="+1 x" + j.getVarNum() + " ";
						requestUsed.add(j.getVarNum());
					}
				}
			}
			//Now, if there is a constraint, finalize it and write it to file.
			if(curr.length()!=0){
				curr = "+1 x" + i.getVarNum() + " " + curr;
				curr += "<= " + i.getCeil() + ";";
				requestUsed.add(i.getVarNum());
				writer.println(curr);
			}
		}
		writer.close();		
		System.out.println("Done with constraint 2. Written to file " + OUTPUT_FILE_2);
	}

	private static void getConstraint1(List<Request> requests) throws UnsupportedEncodingException, FileNotFoundException {
		System.out.println("Creating constraint 1: ensure student is not placed in the same course more than once...");
		PrintWriter writer = new PrintWriter(OUTPUT_FILE_1, "UTF-8");
	
		Set<Integer> requestUsed = new HashSet<Integer>();
		for(Request i:requests){
			int curr_id = i.getId();
			String curr = "";
			for(Request j:requests){
				if(curr_id==j.getId()){//if same student's request
					if(!requestUsed.contains(j.getVarNum()) && i.getSubj().equals(j.getSubj()) && i.getNumber()==j.getNumber() && i!=j){//if request is for the same subject and number
						curr+="+1 x" + j.getVarNum() + " ";
						requestUsed.add(j.getVarNum());
					}
					
				}
				
			}
			if(curr.length()!=0){
				curr += "+1 x" + i.getVarNum() + " ";
				curr += "<= 1;";
				requestUsed.add(i.getVarNum());
				writer.println(curr);
			}
			
		}
		writer.close();
		System.out.println("Done with constraint 1. Written to file " + OUTPUT_FILE_1);
	}
    
    private static void getConstraint3(List<Request> requests) throws FileNotFoundException, UnsupportedEncodingException {
        System.out.println("Creating constraint 3: ensure student is not placed in more than 4 courses...");
		PrintWriter writer = new PrintWriter(OUTPUT_FILE_3, "UTF-8");
        
        Set<Integer> requestUsed = new HashSet<Integer>();
        for(Request i:requests) {
            int curr_id = i.getId();
			String curr = "";
			for(Request j:requests){
				if(curr_id==j.getId()){//if same student's request
					if(!requestUsed.contains(j.getVarNum()) && i!=j){
						curr+="+1 x" + j.getVarNum() + " ";
						requestUsed.add(j.getVarNum());
					}
				}
			}
			if(curr.length()!=0){
				curr += "+1 x" + i.getVarNum() + " ";
				curr += "<= 4;";
				requestUsed.add(i.getVarNum());
				writer.println(curr);
			}
            
        }
        writer.close();
        System.out.println("Done with constraint 3. Written to file " + OUTPUT_FILE_4);
    }
    
    private static void getConstraint4(List<Request> requests) throws FileNotFoundException, UnsupportedEncodingException {        System.out.println("Creating constraint 4: minimize rank of all fulfilled requests...");
		PrintWriter writer = new PrintWriter(OUTPUT_FILE_4, "UTF-8");
        
        String s = "min: ";
        for(Request i:requests) {
			s += "+" + rank(i) +" x" + i.getVarNum() + " ";
        }
        writer.println(s);
        
        writer.close();
        System.out.println("Done with constraint 4. Written to file " + OUTPUT_FILE_4);
    }
    
    private static int rank(Request req) {
        if(req.getBranch() == 1) {
            return req.getTree();
        } else if(req.getTree() != 4) {
            return req.getBranch() % 2 + 1;
        } else return req.getBranch();
    }

}
