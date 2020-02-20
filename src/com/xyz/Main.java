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

		HashMap<String, Integer> countWordsHashMap = new HashMap<>();
		HashMap<String, Integer> countSignsHashMap = new HashMap<>();


		try (BufferedReader reader = new BufferedReader(new FileReader(getFileName(file)))){
			while (reader.ready()) {
				Pattern wordsPattern = Pattern.compile("[а-яА-ЯёЁ]+");
				Pattern signsPattern = Pattern.compile(".");
				String s = reader.readLine();
				Matcher wordsMatcher = wordsPattern.matcher(s);
				Matcher signsMatcher = signsPattern.matcher(s);

				while (wordsPattern.matcher(s).find()) {
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
				sortAndPrint(countSignsHashMap);
				System.out.println("10 самых повторяющихся слов:");
				sortAndPrint(countWordsHashMap);
				break;
			}
			case "Xv" : {
				System.out.println("Кол-во слов = "+words);
				System.out.println("10 самых повторяющихся слов:");
				sortAndPrint(countWordsHashMap);
				break;
			}
			case "Xm" : {
				System.out.println("Кол-во символов = "+signs);
				System.out.println("10 самых повторяющихся символов:");
				sortAndPrint(countSignsHashMap);
				break;
			}
		}
	}

	private static String getFileName(String name) {
		String src = System.getProperty("user.dir") + File.separator + "src" + File.separator;
		return src + name;
	}

	private static void sortAndPrint(HashMap<String, Integer> unSorted) {
		unSorted.entrySet()
				.stream()
				.sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
				.collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
						LinkedHashMap::new)).entrySet().stream().limit(10).forEach(System.out::println);
	}


}
