import java.util.Comparator;
import java.util.HashMap;
import java.util.Scanner;
import java.util.TreeMap;

/**
* COMP 2503 Winter Assignment 4.
* This program reads input from the standard input, processes the words, and provides various statistics
* such as total words count, unique words count, stop words count, 10 most frequent words, 10 longest words,
* the longest word, the shortest word, and the average word length.
* It also excludes stop words (commonly used words like articles, prepositions, etc.) from the analysis.
* The program uses HashMap to store tokens and their counts, and TreeMap for ordered lists of tokens by natural order,
* length, and frequency.
* Tokens are normalized to lowercase and stripped of non-alphabetic characters.
* 
* @author Duy Tran
*/
public class A4 {

  private static final int TOP_NUM = 10;
  private Scanner inp = new Scanner(System.in);

  /* The HashMap of Tokens. */
  private HashMap<String, Token> token = new HashMap<>();
  /* The ordered tree maps of Tokens. */
  private TreeMap<Token, Token> wordsByNaturalOrder = new TreeMap<>(Comparator.naturalOrder());
  private TreeMap<Token, Token> wordsByLength = new TreeMap<>(Token.CompLengthDesc);
  private TreeMap<Token, Token> wordsByFreqDesc = new TreeMap<>(Token.CompFreqDesc);

  // there are 103 stopTokens in this list
  private String[] stopTokens = {"a", "about", "all", "am", "an", "and", "any",
      "are", "as", "at", "be", "been", "but", "by", "can", "cannot", "could",
      "did", "do", "does", "else", "for", "from", "get", "got", "had", "has",
      "have", "he", "her", "hers", "him", "his", "how", "i", "if", "in", "into",
      "is", "it", "its", "like", "more", "me", "my", "no", "now", "not", "of",
      "on", "one", "or", "our", "out", "said", "say", "says", "she", "so",
      "some", "than", "that", "thats", "the", "their", "them", "then", "there",
      "these", "they", "this", "to", "too", "us", "upon", "was", "we", "were",
      "what", "with", "when", "where", "which", "while", "who", "whom", "why",
      "will", "you", "your", "up", "down", "left", "right", "man", "woman",
      "would", "should", "dont", "after", "before", "im", "men"};

  private int totalTokenCount = 0;
  private int stopTokenCount = 0;

  public static void main(String[] args) {
    A4 a4 = new A4();
    a4.run();
  }

  /**
   * Runs the program by executing the necessary steps: reading file input, removing stop words,
   * creating frequency lists, and printing the results.
   */
  private void run() {
    readFile();
    removeStop();
    createFreqLists();
    printResults();
  }

  /**
   * Prints the results of the program, including total words count, unique words count, stop words count,
   * 10 most frequent words, 10 longest words, the longest word, the shortest word, and the average word length.
   */
  private void printResults() {
	// Print total words count, unique words count, and stop words count
    System.out.println("Total Words: " + totalTokenCount);
    System.out.println("Unique Words: " + token.size());
    System.out.println("Stop Words: " + stopTokenCount);
    System.out.println();
    
    // Print 10 most frequent words
    System.out.println("10 Most Frequent");

    printTopNum(wordsByFreqDesc);

    System.out.println();
    // Print 10 longest words
    System.out.println("10 Longest");

    printTopNum(wordsByLength);

    System.out.println();
    // Print the longest word, the shortest word, and the average word length
    System.out.println("The longest word is " + returnLongestWord(wordsByLength));
    System.out.println("The shortest word is " + returnShortestWord(wordsByLength));
    System.out.println("The average word length is " + avgLength());

    System.out.println();
    System.out.println("All");
    // Print all words
    printAll(wordsByNaturalOrder);
  }


  /**
   * Prints the top 'TOP_NUM' tokens from the specified list.
   * 
   * @param listToPrint The TreeMap containing tokens to print.
   */
  private void printTopNum(TreeMap<Token, Token> listToPrint) {

    int amount = Math.min(listToPrint.size(), TOP_NUM);

    int count = 0;

    for (Token key : listToPrint.keySet()) {
      if (count >= amount) {
        return;
      }
      System.out.println(key);
      count++;
    }
  }

  /**
   * Prints all tokens from the specified list.
   * 
   * @param listToPrint The TreeMap containing tokens to print.
   */
  private void printAll(TreeMap<Token, Token> listToPrint) {

    for (Token key : listToPrint.keySet()) {
      System.out.println(key);
    }

  }

  /**
   * Calculates the average length of tokens.
   * 
   * @return The average length of tokens.
   */
  private int avgLength() {

    int sum = 0;
    for (Token key : wordsByLength.keySet()) {
      sum += key.getLength();
    }
    int average = 0;
    if (!wordsByLength.isEmpty()) {
      average = sum / wordsByLength.size();
    }
    return average;
  }

  /**
   * Returns the shortest word from the specified TreeMap of tokens by length.
   * 
   * @param wordsByLength2 The TreeMap of tokens ordered by length.
   * @return The shortest word.
   */
  private String returnShortestWord(TreeMap<Token, Token> wordsByLength2) {

    return wordsByLength2.isEmpty() ? "None" : wordsByLength2.lastKey().getWord();
  }

  /**
   * Returns the longest word from the specified TreeMap of tokens by length.
   * 
   * @param wordsByLength2 The TreeMap of tokens ordered by length.
   * @return The longest word.
   */
  private String returnLongestWord(TreeMap<Token, Token> wordsByLength2) {

    return wordsByLength2.isEmpty() ? "None" : wordsByLength2.firstKey().getWord();
  }

  /**
   * Reads input from the standard input and processes each word.
   */
  private void readFile() {

    while (inp.hasNext()) {
      String word = inp.next().toLowerCase().replaceAll("[^a-z]", "").trim();

      if (word.length() > 0) {
        checkAdd(word);
      }
    }
  }

  /**
   * Checks if a word should be added to the token HashMap and increments its count if it exists.
   * 
   * @param curWord The word to check and potentially add.
   */
  private void checkAdd(String curWord) {

    Token foundToken = token.get(curWord);
    if (foundToken != null) {
      foundToken.incrCount();
    } else {
      token.put(curWord, new Token(curWord));
    }
    totalTokenCount++;
  }

  /**
   * Removes stop words from the token HashMap.
   */
  private void removeStop() {

    for (String stopWord : stopTokens) {
      if (token.remove(stopWord) != null) {
        stopTokenCount++;
      }
    }
  }

  /**
   * Creates frequency lists for tokens by adding them to TreeMap instances.
   */
  private void createFreqLists() {

    Token blank = new Token("");
    for (String key : token.keySet()) {
      Token curToken = token.get(key);
      wordsByNaturalOrder.put(curToken, blank);
      wordsByLength.put(curToken, blank);
      wordsByFreqDesc.put(curToken, blank);
    }
  }
}