/*
*This class represents a Request made by a user on webtree.
*/
public class Request {
	private int var_num, id, crn, tree, branch, ceiling, number, seq;
	private String subj;
	
	public Request(int var, int request_id, int crn, int tree, int branch, int course_ceil, int number, int seq, String subject){
		this.var_num = var;
		this.id = request_id;
		this.crn = crn;
		this.tree = tree;
		this.branch = branch;
		this.ceiling = course_ceil;
		this.number = number;
		this.seq = seq;
		this.subj = subject;
	}
	
	public int getVarNum(){
		return this.var_num;
	}
	public int getId(){
		return this.id;
	}
	public int getCrn(){
		return this.crn;
	}
	public String getSubj(){
		return this.subj;
	}
	public int getNumber(){
		return this.number;
	}

	public int getCeil() {
		return this.ceiling;
	}
}
