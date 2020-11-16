import java.io.FileNotFoundException;

public class Main {
	public static void main(String[] args) throws FileNotFoundException	{
		Solution s1 = new Solution("data/input1.txt");	
		s1.solveDynamic();
		s1.print();
		System.out.println();
	}
}
