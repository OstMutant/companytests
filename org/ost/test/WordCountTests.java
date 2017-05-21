package org.ost.test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.ost.test.text.WordCount;

public class WordCountTests {

  private ByteArrayOutputStream baos;
  private PrintStream oldSO;

  @Before
  public void setUp() throws Exception {
    oldSO = System.out;
    baos = new ByteArrayOutputStream();
    PrintStream ps = new PrintStream(baos);
    System.setOut(ps);
  }

  @After
  public void tearDown() throws Exception {
    System.setOut(oldSO);
  }

  private File createFile(String content) throws Exception {
    Path path = Files.createTempFile("test", ".txt");
    Files.write(path, content.getBytes(StandardCharsets.UTF_8));
    return path.toFile();
  }

  @Test
  public void testCalculate() throws Exception {
    File file = createFile("test1 test2" + System.lineSeparator());
    WordCount.main(new String[] { file.getAbsolutePath(), "0", });
    file.deleteOnExit();
    Assert.assertEquals("", "test1=1" + System.lineSeparator() + "test2=1" + System.lineSeparator(), baos.toString());
    baos.reset();

    file = createFile("test testT" + System.lineSeparator() + "test testT");
    WordCount.main(new String[] { file.getAbsolutePath(), "0" });
    file.deleteOnExit();
    Assert.assertEquals("", "test=2" + System.lineSeparator() + "testt=2" + System.lineSeparator(), baos.toString());
    baos.reset();

    file = createFile("test testT" + System.lineSeparator() + "test testT" + System.lineSeparator() + "test test1");
    WordCount.main(new String[] { file.getAbsolutePath(), "0" });
    file.deleteOnExit();
    Assert.assertEquals("", "test=3" + System.lineSeparator() + "testt=2" + System.lineSeparator() + "test1=1" + System.lineSeparator(), baos.toString());
    baos.reset();

    file = createFile("test testT" + System.lineSeparator() + "test testT" + System.lineSeparator() + "test test1");
    WordCount.main(new String[] { file.getAbsolutePath(), "3" });
    file.deleteOnExit();
    Assert.assertEquals("", "test=3" + System.lineSeparator(), baos.toString());
    baos.reset();

    file = createFile("test, testT" + System.lineSeparator() + "test. testT" + System.lineSeparator() + "test,. test1");
    WordCount.main(new String[] { file.getAbsolutePath(), "0" });
    file.deleteOnExit();
    Assert.assertEquals("", "test=3" + System.lineSeparator() + "testt=2" + System.lineSeparator() + "test1=1" + System.lineSeparator(), baos.toString());
    baos.reset();
  }

  @Test
  public void testParameterCalculate() throws Exception {
    WordCount.main(new String[] {});
    Assert.assertEquals("", WordCount.HELP_STR + System.lineSeparator(), baos.toString());
    baos.reset();

    WordCount.main(new String[] { "C:/", "---" });
    Assert.assertEquals("", "Parameter: 'amount of words' is incorrect" + System.lineSeparator(), baos.toString());
    baos.reset();

    try {
      WordCount.main(new String[] { "C:/", "0" });
    } catch (Exception e) {
      Assert.assertTrue(e.getMessage().contains("File is not loaded"));
    }
  }

  @Test
  public void testGeneral() throws Exception {
    File file = createFile("Hark. How the bells, sweet silver bells"
+"All seem to say, Throw cares away."
+"Christmas is here, bringing good cheer"
+"To young and old, meek and the bold"
+"Ding, dong, ding, dong, that is their song,"
+"With joyful ring, all caroling"
+"One seems to hear words of good cheer"
+"From everywhere, filling the air"
+"Oh, how they pound, raising their sound"
+"Over hill and dale, telling their tale"
+" "
+"Gaily they ring, while people sing"
+"Songs of good cheer, Christmas is here."
+"Merry, merry, merry, merry Christmas."
+"Merry, merry, merry, merry Christmas."
+" "
+"On, on they send, on without end"
+"Their joyful tone to every home"
+"Hark. How the bells, sweet silver bells"
+"All seem to say, Throw cares away."
+"Christmas is here, bringing good cheer"
+"To young and old, meek and the bold"
+"Ding, dong, ding, dong, that is their song"
+"With joyful ring, all caroling."
+"One seems to hear words of good cheer"
+"From everywhere, filling the air"
+"O, how they pound, raising their sound"
+"Over hill and dale, telling their tale"
+" "
+"Gaily they ring, while people sing"
+"Songs of good cheer, Christmas is here."
+"Merry, merry, merry, merry Christmas."
+"Merry, merry, merry, merry Christmas."
+""
+"On, on they send, on without end"
+"Their joyful tone to every home."
+"Ding dong ding dong." + System.lineSeparator());
    WordCount.main(new String[] { file.getAbsolutePath(), "6", });
    file.deleteOnExit();
    Assert.assertEquals("", "merry=16" + System.lineSeparator()
+"christmas=8" + System.lineSeparator()
+"and=6" + System.lineSeparator()
+"dong=6" + System.lineSeparator()
+"good=6" + System.lineSeparator()
+"is=6" + System.lineSeparator()
+"on=6" + System.lineSeparator()
+"the=6" + System.lineSeparator()
+"their=6" + System.lineSeparator()
+"they=6" + System.lineSeparator()
+"to=6" + System.lineSeparator(), baos.toString());
    baos.reset();
  }
  
}
