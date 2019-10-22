package sat;


//import static org.junit.Assert.*;
//import org.junit.Test;


import sat.env.*;
import sat.formula.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;


public class SATSolverTest {
    Literal a = PosLiteral.make("a");
    Literal b = PosLiteral.make("b");
    Literal c = PosLiteral.make("c");
    Literal na = a.getNegation();
    Literal nb = b.getNegation();
    Literal nc = c.getNegation();




    // This is the main method that reads the .cnf file and calls SATSolver.solve to determine the satisfiability of the problem.
    public static void main(String[] args) {
        // ADD YOUR FILEPATH HERE OF FOLDER CONTAINING CNF FILE TO TEST (EXCLUDE NAME OF FILE)
        String yourFilePath = "C:\\Users\\Daryll\\Desktop\\comp struc\\code2d\\app\\sampleCNF\\";
        String nameOfCNFfile = "s8Sat.cnf"; // Name of CNF you want to test (located in filepath provided)
        Formula formula = new Formula();
        int numOfVar = 0;
        String[] elements;
        try {
            FileReader fr = new FileReader(yourFilePath + nameOfCNFfile);
            BufferedReader reader = new BufferedReader(fr);
            String currentline;
            while ((currentline = reader.readLine()) != null && !currentline.trim().isEmpty()) { // if there is a line AND line has to be something (not spaces, trimmed here in case)
                if (currentline.startsWith("p")){ // if currentline is a problem statement: p FORMAT VARIABLES CLAUSES
                    elements = currentline.trim().split("\\s"); // split this line into an array
                    numOfVar = Integer.parseInt(elements[2]); // number of variables is the 3rd element
                } else if (currentline.startsWith("c")){ // if its a comment, ignore.
                    continue;
                } else { // the rest of the lines are normal clause content lines, not problem or comments
                    Clause clause = new Clause();
                    elements = currentline.trim().split("\\s"); // split the line into array
                    for (String ele: elements){ // looking at the individual ele of elements array
                        if (ele != "0"){ // if its not the last ending ele which is 0, then continue.
                            if (ele.charAt(0)=='-'){ // if this literal is a negation ie. has a '-' in front of it
                                clause = clause.add(NegLiteral.make(ele.substring(1))); // Add this NegLiteral
                            } else { // literal is positive
                                clause = clause.add(PosLiteral.make(ele)); // Add this PosLiteral
                            }
                        }
                    }
                    formula = formula.addClause(clause); // Iterate and fill up formula with clauses
                }

            }
        }
        /*
        For Catching Error statements
        view: https://www.youtube.com/watch?v=waXvGUEjTTs
         */
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("SAT solver starts!!!");
        long started = System.nanoTime();
        Environment env = SATSolver.solve(formula);
        //Environment env = SATSolver.solve(makeFm(makeCl(a,b)) ); // for testing only.
        long time = System.nanoTime();
        long timeTaken= time - started;
        System.out.println("Time:" + timeTaken/1000000.0 + "ms");
        if (env == null) {
            System.out.println("not satisfiable");
        }
        else {
            System.out.println("satisfiable");
            try {
                String nameOfOutputFile = "BoolAssignment.txt";
                FileWriter fw = new FileWriter(yourFilePath + nameOfOutputFile);
                PrintWriter output = new PrintWriter(fw);
                for (int ivar = 1; ivar <= numOfVar; ivar++) {
                    Bool bool = env.get(new Variable(Integer.toString(ivar))); // get the boolean values of the variables from the env
                    if (bool == Bool.TRUE) {
                        output.println(Integer.toString(ivar) + ":TRUE");
                    }
                    else { // if (bool == Bool.FALSE)
                        output.println(Integer.toString(ivar) + ":FALSE");
                    }
                }
                output.close();
            } catch (IOException e) { // to catch error, if there is.
                e.printStackTrace();
            }
        }
    }

    /*
    FOR SMALL INTEGRATIVE TESTING OF SATSolver.solve()
    Not needed in main code after code is done.
    */
    public void testSATSolver1(){
        // (a v b)
        Environment e = SATSolver.solve(makeFm(makeCl(a,b))  );


/*
      assertTrue( "one of the literals should be set to true",
          Bool.TRUE == e.get(a.getVariable())
          || Bool.TRUE == e.get(b.getVariable())  );

*/
    }


    public void testSATSolver2(){
        // (~a)
        Environment e = SATSolver.solve(makeFm(makeCl(na)));
/*
      assertEquals( Bool.FALSE, e.get(na.getVariable()));
*/
    }

    private static Formula makeFm(Clause... e) {
        Formula f = new Formula();
        for (Clause c : e) {
            f = f.addClause(c);
        }
        return f;
    }

    private static Clause makeCl(Literal... e) {
        Clause c = new Clause();
        for (Literal l : e) {
            c = c.add(l);
        }
        return c;
    }



}



















//package sat;
//
///*
//import static org.junit.Assert.*;
//
//import org.junit.Test;
//*/
//
//import sat.env.*;
//import sat.formula.*;
//
//
//public class SATSolverTest {
//    Literal a = PosLiteral.make("a");
//    Literal b = PosLiteral.make("b");
//    Literal c = PosLiteral.make("c");
//    Literal na = a.getNegation();
//    Literal nb = b.getNegation();
//    Literal nc = c.getNegation();
//
//
//
//
//	// TODO: add the main method that reads the .cnf file and calls SATSolver.solve to determine the satisfiability
//
//    public static Bool main(String[] args){
//        return null;
//    }
//
//
//    public void testSATSolver1(){
//    	// (a v b)
//    	Environment e = SATSolver.solve(makeFm(makeCl(a,b))	);
///*
//    	assertTrue( "one of the literals should be set to true",
//    			Bool.TRUE == e.get(a.getVariable())
//    			|| Bool.TRUE == e.get(b.getVariable())	);
//
//*/
//    }
//
//
//    public void testSATSolver2(){
//    	// (~a)
//    	Environment e = SATSolver.solve(makeFm(makeCl(na)));
///*
//    	assertEquals( Bool.FALSE, e.get(na.getVariable()));
//*/
//    }
//
//    private static Formula makeFm(Clause... e) {
//        Formula f = new Formula();
//        for (Clause c : e) {
//            f = f.addClause(c);
//        }
//        return f;
//    }
//
//    private static Clause makeCl(Literal... e) {
//        Clause c = new Clause();
//        for (Literal l : e) {
//            c = c.add(l);
//        }
//        return c;
//    }
//
//
//
//}