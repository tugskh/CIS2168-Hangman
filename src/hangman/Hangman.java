/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hangman;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 *
 * @author tugsbilegkhaliunbat
 */
public class Hangman {

    /**
     * @param args the command line arguments
     */
    public static Map<Integer, List<String>> splitWordsByLength(String filename) throws FileNotFoundException{
        HashMap<Integer, List<String>> wordsByLength = new HashMap<>();
        File words = new File(filename);
        Scanner sc = new Scanner(words);
        while(sc.hasNext()){
            String word = sc.nextLine();
            int length = word.length();
            if(!wordsByLength.containsKey(length)){
                List wordList = new ArrayList<String>();
                wordList.add(word);
                wordsByLength.put(length, wordList);
            } else {
                wordsByLength.get(length).add(word);
            }
        }
        return wordsByLength;
    }
    
    public static Map<String, List<String>> createWordFamilies(List<String> words, Set<Character> guesses){
        HashMap<String, List<String>> wordFamilies = new HashMap<>();
        for (String word: words){
            word = word.toLowerCase();
            String forPlayer = "";
            for (int i = 0; i < word.length(); i++) {
                char letter = word.charAt(i);
                if(guesses.contains(word.charAt(i))){
                    forPlayer+=letter+" ";
                } else {
                    forPlayer+="_ ";
                }
            }
            if(!wordFamilies.containsKey(forPlayer)){
                List<String> wordList = new ArrayList<>();
                wordList.add(word);
                wordFamilies.put(forPlayer, wordList);
            } else {
                wordFamilies.get(forPlayer).add(word);
            }
        }
        return wordFamilies;
    }    
    
    
    
    public static String bestFamily(Map<String, List<String>> families){
        int mostWords = 0;
        String forPlayer = "";
        for(Map.Entry<String, List<String>> family : families.entrySet()){
            if(family.getValue().size() > mostWords){
                mostWords = family.getValue().size();
                forPlayer = family.getKey();
            }
        }
        return forPlayer;
    } 
    
    public static void Hangman(int wordLength, int wrongGuesses) throws FileNotFoundException{
        List<String> lengthWords = splitWordsByLength("words.txt").get(wordLength);
        Map<String, List<String>> families;
        Set<Character> guesses = new HashSet<>();
        Scanner sc = new Scanner(System.in);
        String forPlayer = "";
        for (int i = 0; i < wordLength; i++) {
            forPlayer+="_ ";
        }
        
        while(wrongGuesses > 0 && forPlayer.contains("_ ")){
            System.out.println("Enter a letter: ");
            char guess = sc.nextLine().charAt(0);
            while(!guesses.add(guess)){
                System.out.println("Enter a letter you haven't entered: ");
                guess = sc.nextLine().charAt(0);
            }
            guesses.add(guess);
            families = createWordFamilies(lengthWords, guesses);
            //System.out.println(families);
            String bestFam= bestFamily(families);
            lengthWords = families.get(bestFam);
            //System.out.println(lengthWords);
            forPlayer = bestFam;
            System.out.println(forPlayer);
            if(!forPlayer.contains(Character.toString(guess))){
                wrongGuesses--;
            }
            System.out.println("Left guesses: "+ wrongGuesses+"\n");
            System.out.println("Guessed letters: "+guesses+"\n");
            if(!forPlayer.contains("_ ")){
                System.out.println("Congratulations! You won.\n");
            } else if(wrongGuesses == 0){
                System.out.println("You've ran out of guesses.");
                System.out.println("The word was: "+ lengthWords.get(lengthWords.size()-1));
            }
        }
    }
    
    public static void main(String[] args) throws FileNotFoundException {
        
        Scanner sc = new Scanner(System.in);
        System.out.println("Hello, this is Hangman.");
        System.out.println("Please enter the length of the word you would like to guess: ");
        int wordLength = sc.nextInt();
        System.out.println("How many wrong guesses would you like to have?");
        int wrongGuesses = sc.nextInt();
        Hangman(wordLength, wrongGuesses);
        
        while(true){
            Scanner sc1 = new Scanner(System.in);
            System.out.println("\nDo you want to play again? ");
            String ans = sc1.nextLine();
            if(ans.equals("Yes")||ans.equals("yes")){
                System.out.println("Please enter the length of the word you would like to guess: ");
                wordLength = sc1.nextInt();
                System.out.println("How many wrong guesses would you like to have?");
                wrongGuesses = sc1.nextInt();
                Hangman(wordLength, wrongGuesses);
            }
            else {
                System.out.println("Thank you for playing. Bye!");
                break;
            }
        }
        
    }
    
}
