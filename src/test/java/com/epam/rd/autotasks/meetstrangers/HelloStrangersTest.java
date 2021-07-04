package com.epam.rd.autotasks.meetstrangers;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.*;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class HelloStrangersTest {

    @Nested
    @DisplayName("Tests for illegal input(0, -1, null)")
    class illegalInput {
        @Test
        @Order(1)
        @DisplayName("Test for zero input")
        public void zeroInputTest() {
            final String namesCount = "0";
            final ByteArrayInputStream byteIn = new ByteArrayInputStream(namesCount.getBytes());
            BufferedInputStream controlledIn = new BufferedInputStream(byteIn);
            InputStream defaultIn = System.in;

            System.setIn(controlledIn);

            try {
                NoSuchElementException thrown = assertThrows( //maybe runtimeEx ?
                        NoSuchElementException.class,
                        ()-> HelloStrangers.main(new String[]{}),
                        "The program should not greet anyone because the number of strangers is zero"
                );
                assertTrue(thrown.getMessage().contains("No line found"));
            }
            finally {
                System.setIn(defaultIn);
            }
        }
        @Test
        @Order(2)
        @DisplayName("Test for negative input")
        public void negInputTest() {
            final String namesCount = "-1";
            final ByteArrayInputStream byteIn = new ByteArrayInputStream(namesCount.getBytes());
            BufferedInputStream controlledIn = new BufferedInputStream(byteIn);
            InputStream defaultIn = System.in;

            System.setIn(controlledIn);
            try {
                NegativeArraySizeException thrown = assertThrows( //maybe runtimeEx ?
                        NegativeArraySizeException.class,
                        ()-> HelloStrangers.main(new String[]{}),
                        "The program should not work because the strangers count is negative"
                );
                assertTrue(thrown.getMessage().contains("-1"));
            }  finally {
                System.setIn(defaultIn);
            }
        }
        @Test
        @Order(3)
        @DisplayName("Test for null input")
        public void nullInputTest() {
            BufferedInputStream controlledIn = new BufferedInputStream(null);
            InputStream defaultIn = System.in;

            System.setIn(controlledIn);
            try {
                assertThrows( //maybe runtimeEx ?
                        NoSuchElementException.class,
                        ()-> HelloStrangers.main(new String[]{}),
                        "The program should not work because the strangers count is null"
                );
            }  finally {
                System.setIn(defaultIn);
            }
        }
    }
    @Test
    @Order(4)
    @DisplayName("Test on single word name")
    public void singleWordNameTest() {
        ArrayList<String> list = new ArrayList<>();
        list.add("Angus");
        anyNamesTest(list);
    }
    @Test
    @Order(5)
    @DisplayName("Test on multiple words name")
    public void multipleWordsNameTest() {
        ArrayList<String> list = new ArrayList<>();
        list.add("Agent Smith");
        anyNamesTest(list);
    }
    @ParameterizedTest
    @Order(6)
    @DisplayName("Test on random input names")
    @MethodSource("randomNames")
    public void anyNamesTest(ArrayList<String> namesList) {

        //compound name strings to one input string by format:
        // "{namesCount}\lineSep{nameOne}\lineSep...{nameN}\lineSep"
        //e.g.:2\nJohn\nDave\n
        final int namesCount = namesList.size();
        StringBuilder sb = new StringBuilder();
        sb.append(namesList.size()).append(System.lineSeparator());
        for (String s : namesList) {
            sb.append(s).append(System.lineSeparator());
        }
        String names = sb.toString();

        final ByteArrayInputStream byteIn = new ByteArrayInputStream(names.getBytes());
        BufferedInputStream controlledIn = new BufferedInputStream(byteIn);
        InputStream defaultIn = System.in;

        System.setIn(controlledIn);

        final ByteArrayOutputStream sink = new ByteArrayOutputStream();
        PrintStream controlledOut = new PrintStream(sink);
        PrintStream defaultOut = System.out;

        System.setOut(controlledOut);

        try {
            HelloStrangers.main(new String[]{});
            controlledOut.flush();

            final String actual = sink.toString().trim();
            String[] temp = actual.split(System.lineSeparator());
            sb = new StringBuilder();
            for (int i = temp.length-namesCount; i < temp.length; i++){
                sb.append(temp[i]).append(System.lineSeparator());
            }
            String outHellos = sb.toString();

            temp = names.split(System.lineSeparator());
            sb = new StringBuilder();
            for (int i = 1; i < temp.length; i++){ //first el is name count
                sb.append("Hello, ").append(temp[i]).append(System.lineSeparator());
            }
            String chekHellos = sb.toString();

            assertEquals(chekHellos, outHellos, "Your program must print (e.g): \"" +
                    chekHellos + "\" but printed \"" + outHellos + "\" instead. " +
                    "(Make sure that program prints \" Hello, ...\" in the end of output)");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            System.setOut(defaultOut);
            System.setIn(defaultIn);
        }
    }
    static Stream<ArrayList<String>> randomNames() {
        String[] names = {
                "John", "Dave", "Martin",
                "Jimmy", "Robert", "Paul",
                "Darth Vader", "Bilbo Baggins",
                "Carl Johnson", "Wastelander",
                "Billy Harrington", "R2-D2",
                "C-3PO", "1+-Pro-100-Tascher-+1"
        };
        return Stream.of(
                filler(names),
                filler(names),
                filler(names)
        );
    }
    static ArrayList<String> filler(String[] names) {
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i <= (int)(Math.random()*100)%10; i++){
            list.add(names[(int)(Math.random()*100)%names.length]);
        }
        return list;
    }
}