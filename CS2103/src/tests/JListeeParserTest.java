// @@author A0126070U

package tests;

import static org.junit.Assert.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import com.joestelmach.natty.*;

import parser.JListeeParser;


public class JListeeParserTest {
	public static JListeeParser parse;

	@BeforeClass
	public static void createParser() {
		parse = new JListeeParser();
	}

	/* This is a test for the add command partition */
	@Test
	public void testAddCommandType() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Method method = JListeeParser.class.getDeclaredMethod("determineCommandType", String[].class);
		method.setAccessible(true);
		
		String[] inputLine = "add #one hihi @location".split(" ");
		
		String actual = (String) method.invoke(parse, new Object[]{inputLine});
	
		String expected = "add";
	
		assertEquals(expected, actual);
	}

	/* This is a test for the delete command partition */
	@Test
	public void testDeleteCommandType() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Method method = JListeeParser.class.getDeclaredMethod("determineCommandType", String[].class);
		method.setAccessible(true);
		
		String[] inputLine = "add #one hihi @location".split(" ");
		
		String actual = (String) method.invoke(parse, new Object[]{inputLine});
	
		String expected = "add";
	
		assertEquals(expected, actual);
	}

	/* This is a test for the show command partition */
	@Test
	public void testShowCommandType() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Method method = JListeeParser.class.getDeclaredMethod("determineCommandType", String[].class);
		method.setAccessible(true);
		
		String[] inputLine = "show /today 4pm".split(" ");
		
		String actual = (String) method.invoke(parse, new Object[]{inputLine});
	
		String expected = "show";
	
		assertEquals(expected, actual);
	}

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
	
	/* This is a test to find location with @ as task description */
	@Test
	public void testLocationDontExistWithAt() throws Exception {
		String expected = "location";
		
		Method method = JListeeParser.class.getDeclaredMethod("findLocation", String.class);
		method.setAccessible(true);
		String actual = (String) method.invoke(parse, "add @ @location @@ description");
		
		assertEquals(expected, actual);
	}
	
	
	/* This is a test to find location with special characters */
	@Test
	public void testLocationWithSpecialCharacters() throws Exception {
		String expected = "COM1-b2-03!!!";
		
		Method method = JListeeParser.class.getDeclaredMethod("findLocation", String.class);

		method.setAccessible(true);
		String actual = (String) method.invoke(parse, "add @COM1-b2-03!!! description");
	
		assertEquals(expected, actual);
	}
	
	/* This is a test to get PrepositionIndex that exist */
	@Test
	public void getPrepositionIndex() throws Exception {
		Method method = JListeeParser.class.getDeclaredMethod("getPrepositionIndex", String.class, int.class);
		method.setAccessible(true);
		int expected = 36;
		int actual = (int) method.invoke(parse, "add what from today to tomorrow and due sunday", -1);
		assertEquals(expected, actual);
	}
	
	/* This is a test to get PrepositionIndex that don't exist */
	@Test
	public void getPrepositionIndex2() throws Exception {
		Method method = JListeeParser.class.getDeclaredMethod("getPrepositionIndex", String.class, int.class);
		method.setAccessible(true);
		int expected = -1;
		int actual = (int) method.invoke(parse, "add good friday", -1);
		assertEquals(expected, actual);
	}

	/* This is a test to get Preposition index if user only input from without to */
	@Test
	public void checkFromAndToPrepositionIndex() throws Exception {
		Method method = JListeeParser.class.getDeclaredMethod("checkFromAndTo", String.class, int.class);
		method.setAccessible(true);
		int expected = -1;
		int actual = (int) method.invoke(parse, "add task description from", -1);
		assertEquals(expected, actual);
	}
	
	/* This is a test to get TaskDescription with location */
	@Test
	public void getTaskDescription() throws Exception {
		String expected = "hello this is task";
		
		Method method = JListeeParser.class.getDeclaredMethod("trimInputLineToDescriptionOnly", String.class, String.class, ArrayList.class);
		method.setAccessible(true);
		String actual = (String) method.invoke(parse, "hello this is task @location", "location", null);
		
		assertEquals(expected, actual);
	}
	
	/* This is a test to get TaskDescription with special characters */
	@Test
	public void getTaskDescriptionWithSpecialCharacters() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		
		Method method = JListeeParser.class.getDeclaredMethod("trimInputLineToDescriptionOnly", String.class, String.class, ArrayList.class);
		method.setAccessible(true);
		ArrayList<String> tagLists = new ArrayList<String>();
		tagLists.add("hihihi");
		String actual = (String) method.invoke(parse, "inputline with special !@#$%^&*() @location #hihihi", "location", tagLists);
		
		String expected = "inputline with special !@#$%^&*()";
		assertEquals(expected,actual);
	}
	
	
	/* This is a test to get tasknumbers with extra spaces */
	@Test
	public void getTaskNumbersWithExtraSpace() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{	
		Method method = JListeeParser.class.getDeclaredMethod("extractTaskNumbers", String.class, ArrayList.class);
		method.setAccessible(true);
		ArrayList<Integer>  actual = new ArrayList<Integer>();
		actual = (ArrayList<Integer>) method.invoke(parse, "1  , 2, 3, 4",  actual);
		int expected = 4;
		assertEquals(expected, actual.size());
	}
	
	/* This is a test to get tasknumbers with usage of dash */
	@Test
	public void getTaskNumberWithDash() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		
		Method method = JListeeParser.class.getDeclaredMethod("extractTaskNumbers", String.class, ArrayList.class);
		method.setAccessible(true);
		ArrayList<Integer>  actual = new ArrayList<Integer>();
		actual = (ArrayList<Integer>) method.invoke(parse, "1-5,   2-9, 3-10",  actual);
		int expected = 10;
		assertEquals(expected, actual.size());
	}
	
	/* This is a test to get tasknumbers with usage of commas and dash */
	@Test
	public void getTaskNumberWithCommasAndDash() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		Method method = JListeeParser.class.getDeclaredMethod("extractTaskNumbers", String.class, ArrayList.class);
		method.setAccessible(true);
		ArrayList<Integer>  actual = new ArrayList<Integer>();
		actual = (ArrayList<Integer>) method.invoke(parse, "1-5,   6,7, 8-10",  actual);
		int expected = 10;
		assertEquals(expected, actual.size());
	}
	

/*	public void testDate(){
		com.joestelmach.natty.Parser dateParser = new com.joestelmach.natty.Parser();
		List<DateGroup> dateGroups = dateParser.parse("event task from today 4pm to 6pm".substring(12));
		Method method = JListeeParser.class.getDeclaredMethod("setAllDates", String.class, <List<DateGroup>>.getClass());

	}
	*/
	
	
	

}
