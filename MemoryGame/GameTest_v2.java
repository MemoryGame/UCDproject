import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class GameTest{

public static void main(String args[]){

//Two ArrayLists for pattern to guess and user guess

ArrayList<Integer> pattern = new ArrayList<Integer>();
ArrayList<Integer> userGuess = new ArrayList<Integer>();

//random num generator
Random r = new Random();

//set guess ArrayList declare variable p to change size later set initial lives
int p =4;
int lives = 3;
//insert game loop new pattern here
boolean correct =false;
for(int i =0; i<p;i++){
			pattern.add(r.nextInt(4));
	}

	System.out.println(pattern);
	
//User guess 
	//scanner for input prompt guess 
	Scanner sc = new Scanner(System.in);
	int usernum;
//check until sizes match
	Ol:while(lives>0 && correct !=true){
	for(int i=0;userGuess.size()<pattern.size();i++){
	//test current guess against pattern
			
		System.out.println("Guess: ");
		usernum = sc.nextInt();
		userGuess.add(usernum);
	
			if(userGuess.get(i) == pattern.get(i)){
				System.out.println("Good");
			}
			else{
					lives--;					
					System.out.println("Bad lives left "+lives);
					continue Ol;					
					//break;
			}
		}
		correct = true;
	}
//print two patterns		
	System.out.println("Computer Pattern "+pattern);
	System.out.println("User Guess "+userGuess);

}



}
