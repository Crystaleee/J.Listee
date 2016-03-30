// @@author Her Kai Lin (A0126070U)

package parser;

import static org.junit.Assert.*;

import java.util.ArrayList;
import org.junit.BeforeClass;
import org.junit.Test;

public class JListeeParserTest {
	private static JListeeParser parse;

	@BeforeClass
	public static void createParser() {
		parse = new JListeeParser();
	}

	/* This is a test for the add command partition */
	@Test
	public void testAddCommandType() {
		String[] separateInputLine = "add cs2103 lecture (03/17/2016 2pm to 03/27/2016 5pm) @LT27 #hihi #me".split(" ");
		String expected = separateInputLine[0];
		String actual = parse.determineCommandType(separateInputLine);
		assertEquals(expected, actual);
	}

	/* This is a test for the delete command partition */
	@Test
	public void testDeleteCommandType() {
		String[] separateInputLine = ("delete 1,2,3,4".split(" "));
		String expected = separateInputLine[0];
		String actual = parse.determineCommandType(separateInputLine);
		assertEquals(expected, actual);
	}

	/* This is a test for the show command partition */
	@Test
	public void testShowCommandType() {
		String[] separateInputLine = ("show keyword".split(" "));
		String expected = separateInputLine[0];
		String actual = parse.determineCommandType(separateInputLine);
		assertEquals(expected, actual);
	}

	/* This is a test to find hash tags when hash tag exists */
	@Test
	public void testHashTagExist() {
		ArrayList<String> expected = new ArrayList<String>();
		expected.add("one");
		expected.add("two");
		expected.add("three");
		ArrayList<String> actual = parse.findHashTags("add #one #two #three");
		assertEquals(expected.size(), actual.size());
	}

	/* This is a test to find hash tags when hash tag dosen't exist */
	@Test
	public void testHashTagDontExist() {
		ArrayList<String> actual = parse.findHashTags("add @location description");
		assertTrue(actual.isEmpty());
	}

	/* This is a test to find location exist */
	@Test
	public void testLocationExist() {
		String expected = "location";
		String actual = parse.findLocation("add @location description");
		assertEquals(expected, actual);
	}

	/* This is a test to find location dont exist */
	@Test
	public void testLocationDontExist() {
		String expected = null;
		String actual = parse.findLocation("add location description");
		assertEquals(expected, actual);
	}

	/* This is a test to get TaskDescription */
	@Test
	public void getTaskDescription() {
		String expected = "hello this is task";
		String actual = parse.trimInputLineToDescriptionOnly("hello this is task @location", "location", null);
		assertEquals(expected, actual);
	}

}
