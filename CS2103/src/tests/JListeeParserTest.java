// @@author A0126070U

package tests;

import static org.junit.Assert.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.BeforeClass;
import org.junit.Test;

import parser.JListeeParser;

public class JListeeParserTest {
	public static JListeeParser parse;

	@BeforeClass
	public static void createParser() {
		parse = new JListeeParser();
	}

//	/* This is a test for the add command partition */
//	@Test
//	public void testAddCommandType() {
//		String[] separateInputLine = "add cs2103 lecture (03/17/2016 2pm to 03/27/2016 5pm) @LT27 #hihi #me".split(" ");
//		String expected = separateInputLine[0];
//		String actual = parse.determineCommandType(separateInputLine);
//		
//		assertEquals(expected, actual);
//	}
//
//	/* This is a test for the delete command partition */
//	@Test
//	public void testDeleteCommandType() {
//		String[] separateInputLine = ("delete 1,2,3,4".split(" "));
//		String expected = separateInputLine[0];
//		String actual = parse.determineCommandType(separateInputLine);
//		assertEquals(expected, actual);
//	}
//
//	/* This is a test for the show command partition */
//	@Test
//	public void testShowCommandType() {
//		String[] separateInputLine = ("show keyword".split(" "));
//		String expected = separateInputLine[0];
//		String actual = parse.determineCommandType(separateInputLine);
//		assertEquals(expected, actual);
//	}

	/* This is a test to find hash tags when hash tag exists */
	@Test
	public void testHashTagExist() throws Exception {
		ArrayList<String> expected = new ArrayList<String>();
		expected.add("one");
		expected.add("two");
		expected.add("three");
		
		Method method = JListeeParser.class.getDeclaredMethod("findHashTags", String.class);
		method.setAccessible(true);
		ArrayList<String> actual = (ArrayList<String>) method.invoke(parse, "add #one #two #three");
		
		assertEquals(expected.size(), actual.size());
	}

	/* This is a test to find hash tags when hash tag dosen't exist */
	@Test
	public void testHashTagDontExist() throws Exception {
		Method method = JListeeParser.class.getDeclaredMethod("findHashTags", String.class);
		method.setAccessible(true);
		ArrayList<String> actual = (ArrayList<String>) method.invoke(parse, "add @location description");
		
		assertTrue(actual.isEmpty());
	}

	/* This is a test to find location exist */
	@Test
	public void testLocationExist() throws Exception {
		String expected = "location";
		
		Method method = JListeeParser.class.getDeclaredMethod("findLocation", String.class);
		method.setAccessible(true);
		String actual = (String) method.invoke(parse, "add @location description");
		
		assertEquals(expected, actual);
	}

	/* This is a test to find location dont exist */
	@Test
	public void testLocationDontExist() throws Exception {
		String expected = null;
		
		Method method = JListeeParser.class.getDeclaredMethod("findLocation", String.class);
		method.setAccessible(true);
		String actual = (String) method.invoke(parse, "add location description");
		
		assertEquals(expected, actual);
	}

	/* This is a test to get TaskDescription */
	@Test
	public void getTaskDescription() throws Exception {
		String expected = "hello this is task";
		
		Method method = JListeeParser.class.getDeclaredMethod("trimInputLineToDescriptionOnly", String.class, String.class, ArrayList.class);
		method.setAccessible(true);
		String actual = (String) method.invoke(parse, "hello this is task @location", "location", null);
		
		assertEquals(expected, actual);
	}

}
