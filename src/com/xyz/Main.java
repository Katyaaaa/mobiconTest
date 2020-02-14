package com.xyz;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.stream.Collectors.toMap;

public class Main {

	static String options;
	static String file;
	static int words;
	static int signs;
	static HashMap<String, Integer> countWordsHashMap;
	static HashMap<String, Integer> countSignsHashMap;


	public static void main(String[] args) {
			if (args.length < 1) {
				System.out.println("Incorrect input");
				System.exit(0);
			}
			if (args.length == 1) {
				options = "mv";
				file = args[0];
			} else {
				options = args[0];
				file = args[1];
			}

			if (options.contains("X")){
				if (options.contains("m") && !options.contains("v")) options = "Xm";
				else if (options.contains("v")  && !options.contains("m")) options = "Xv";
				else options = "Xmv";
			} else {
				if (options.contains("v") && !options.contains("m")) options = "v";
				else if (options.contains("m") && !options.contains("v")) options = "m";
				else options = "mv";
			}

		countSignsHashMap = new HashMap<>();
		countWordsHashMap = new HashMap<>();


		try (BufferedReader reader = new BufferedReader(new FileReader(getFileName(file)))){
			while (reader.ready()) {
				Pattern wordsPattern = Pattern.compile("[а-яА-ЯёЁ]+");
				Pattern signsPattern = Pattern.compile(".");
				String s = reader.readLine();
				Matcher wordsMatcher = wordsPattern.matcher(s);
				Matcher signsMatcher = signsPattern.matcher(s);

				while (wordsMatcher.find()) {
					words++;
					String[] words = wordsMatcher.group().split(" ");
					for (String word : words) {
						if (!countWordsHashMap.containsKey(word)) countWordsHashMap.put(word, 1);
						else countWordsHashMap.put(word, countWordsHashMap.get(word) + 1);
					}
				}

				while (signsMatcher.find()) {
					signs++;
					String[] chars = signsMatcher.group().split(" ");
					for (String aChar : chars) {
						if (!countSignsHashMap.containsKey(aChar)) countSignsHashMap.put(aChar, 1);
						else countSignsHashMap.put(aChar, countSignsHashMap.get(aChar) + 1);
					}
				}

			}
		} catch (IOException e){
			e.printStackTrace();
		}

		switch (options){
			case "m" : {
				System.out.println("Кол-во символов = "+signs);
				break;
			}
			case "v" : {
				System.out.println("Кол-во слов = "+words);
				break;
			}
			case "mv" : {
				System.out.println("Кол-во символов = "+signs);
				System.out.println("Кол-во слов = "+words);
				break;
			}
			case "Xmv" : {
				System.out.println("Кол-во символов = "+signs);
				System.out.println("Кол-во слов = "+words);
				System.out.println("10 самых повторяющихся символов:");
				int limit1 = 0;
				Map<String, Integer> sortedSigns = sort(countSignsHashMap);
				for (Map.Entry<String, Integer> entry : sortedSigns.entrySet()) {
					if (limit1==10) break;
					System.out.println(entry.getKey()+ ": "+entry.getValue());
					limit1++;
				}
				System.out.println("10 самых повторяющихся слов:");
				int limit2 = 0;
				Map<String, Integer> sortedWords = sort(countWordsHashMap);
				for (Map.Entry<String, Integer> entry : sortedWords.entrySet()) {
					if (limit2==10) break;
					System.out.println(entry.getKey()+ ": "+entry.getValue());
					limit2++;
				}
				break;
			}
			case "Xv" : {
				System.out.println("Кол-во слов = "+words);
				System.out.println("10 самых повторяющихся слов:");
				int limit = 0;
				Map<String, Integer> sorted = sort(countWordsHashMap);
				for (Map.Entry<String, Integer> entry : sorted.entrySet()) {
					if (limit==10) break;
					System.out.println(entry.getKey()+ ": "+entry.getValue());
					limit++;
				}
				break;
			}
			case "Xm" : {
				System.out.println("Кол-во символов = "+signs);
				System.out.println("10 самых повторяющихся символов:");
				int limit1 = 0;
				Map<String, Integer> sorted = sort(countSignsHashMap);
				for (Map.Entry<String, Integer> entry : sorted.entrySet()) {
					if (limit1==10) break;
					System.out.println(entry.getKey()+ ": "+entry.getValue());
					limit1++;
				}
				break;
			}
		}

	}

	private static String getFileName(String name) {
		String src = System.getProperty("user.dir") + File.separator + "src" + File.separator;
		return src + name;
	}

	private static Map<String, Integer> sort(HashMap<String, Integer> unSorted) {
		return unSorted
				.entrySet()
				.stream()
				.sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
				.collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
						LinkedHashMap::new));
	}


}
