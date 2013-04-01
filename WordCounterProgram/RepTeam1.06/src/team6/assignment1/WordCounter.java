package team6.assignment1;

import java.io.FileNotFoundException;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;

public class WordCounter {
	
	private String filePath;
	private String delimiters = " ,.;:";
	private int wordLength = 1;
	private int wordCount;
	private int bufferSize = 1048576;
	private boolean fileProcessed;
	
	/**
	 * Constructor
	 * @param filePath
	 * @param delimiters
	 * @param wordLength
	 */
	public WordCounter (String filePath, String delimiters, int wordLength) throws IOException {
		this.filePath = filePath;
		if (delimiters != null) {
			for (int i = 0; i < delimiters.length(); i++) {
				if (delimiters.charAt(i) > 255) {
					throw new IOException("Error: Invalid Input Arguments. One or more delimiters are NON-ASCII");
				}
			}
			this.delimiters = delimiters;	
		}
		if (wordLength > 0) {
			this.wordLength = wordLength;
		}
		this.fileProcessed = false;
		this.wordCount = 0;
	}
	
	public int getCount() throws IllegalStateException {
		if (!fileProcessed) {
			throw new IllegalStateException("The count is not available because the file has still not been processed");
		}
		return wordCount;
	}
	
	/**
	 * Checks if the inputs are valid and constructs a WordCounter
	 * @param inputArguments
	 * @return WordCounter
	 * @throws IOException 
	 */
	private static WordCounter initializeWordCounter (String[] inputArguments) throws IOException {
		int argsLength = inputArguments.length;
		if (argsLength != 1 && argsLength != 3 && argsLength != 5) {
			throw new IOException("Error: Invalid Input Arguments. Please look at the man page for further information");
		}
		
		String filePath = inputArguments[0];
		String delimiters = null;
		int wordLength = -1;
		
		for (int i = 1; i < argsLength; i+=2){
			if (inputArguments[i].equals("-c")){
				delimiters = inputArguments[i+1];
				if (delimiters.equals("")) {
					throw new IOException("Error: Invalid Input Arguments. No delimiters specified");
				}
			} else if (inputArguments[i].equals("-l")){
				wordLength = Integer.parseInt(inputArguments[i+1]);
				if(wordLength<=0)
				{throw new IOException("Error: Invalid Input Arguments. Please look at the man page for further information");}
			    } else {
				throw new IOException("Error: Invalid Input Arguments. Please look at the man page for further information");
			}
		}
		
		return new WordCounter(filePath, delimiters, wordLength);
	}

	/**
	 * Uses FileInputStream to read blocks of bytes
	 * @throws Exception 
	 */
	public void processFile () throws Exception {
		try {
			// Create an array of 256 positions with value=true if that ASCII char is a delimiter.
			int asciiSize = 256;
			boolean[] isDelimiter = new boolean[asciiSize];
			Arrays.fill(isDelimiter, Boolean.FALSE);
			
			for (int i = 0; i < this.delimiters.length(); ++i) {
				isDelimiter[this.delimiters.charAt(i)] = true;
			}
			
			// Open file
			FileInputStream inputStream = new FileInputStream(this.filePath);
			byte[] bufferArray = new byte[this.bufferSize];
			int bytesRead;
			int currentWordLength = 0;
			
			while ((bytesRead = inputStream.read(bufferArray)) > 0) { 
				currentWordLength = countWords(currentWordLength, bufferArray, bytesRead, isDelimiter);			
			}
			// include the last word in the last block
			if (currentWordLength >= this.wordLength) {					
				if (this.wordCount < Integer.MAX_VALUE) {
					this.wordCount ++;
				}
				else {
					inputStream.close();
					throw new NumberFormatException ("Error: word count exceeds Integer max value");
				}
			}
			
			inputStream.close();
			
			this.fileProcessed = true;
		}
		catch (FileNotFoundException e) {
			throw new FileNotFoundException("Error: " + e.getMessage());
		}
		catch (IOException e) {
			throw new IOException("Error: " + e.getMessage());
		}
		catch (NumberFormatException e) {
			throw e;
		}
		catch (Throwable e) {
			throw new Exception("Error: Unexpected error");
		}
 	}
	
	/**
	 * Counts the words in a byteArray and updates wordcount.
	 * @param currentWordLength
	 * @param buffer
	 * @param bytesRead
	 * @param isDelimiter
	 * @return wordLength of the last word int he block
	 * @throws NumberFormatException
	 */
	private int countWords (int currentWordLength, byte[] buffer, int bytesRead, boolean[] isDelimiter) throws NumberFormatException {
		for (int i = 0; i < bytesRead; ++i) {
			
			int currentChar = (buffer[i] + 256)%256; 
			if (isDelimiter[currentChar]) {
				// Current Char is a delimiter
				if (currentWordLength >= this.wordLength) {					
					// Last word was long enough to be taken into account
					if (wordCount < Integer.MAX_VALUE) {
						this.wordCount ++;
					} else {
						throw new NumberFormatException ("Error: wordCount exceeds Integer max value");
					}
				}
				// Reset the current word length to zero
				currentWordLength = 0;
			} else {

				// We are still reading a word, keep counting
				currentWordLength++;
			}
		}
		return currentWordLength;	
	}
	
	public static void main (String[] args) {
		try {
			WordCounter wordCounter = initializeWordCounter (args);
			wordCounter.processFile();
			System.out.println("Number of words in the file: " + wordCounter.getCount());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
