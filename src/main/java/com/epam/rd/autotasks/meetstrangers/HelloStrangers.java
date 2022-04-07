package com.epam.rd.autotasks.meetstrangers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class HelloStrangers {
    public static void main(String[] args) throws IOException {
        //Write a program, asks for a number - amount of strangers to meet.
        //Then reads stranger names line by line and prints line by line "Hello, ...".
        Scanner scanNumber = new Scanner(System.in);
        Scanner scanName = new Scanner(System.in);

        int numberOfStrangers = scanNumber.nextInt();
        if (numberOfStrangers > 0){
            ArrayList <String> names = new ArrayList<>();
            for (int i = 0; i < numberOfStrangers; i++) {
                names.add(scanName.nextLine());
                //System.out.println("Hello, " + name);
            }
            for (int i = 0; i < numberOfStrangers; i++) {
                System.out.println("Hello, " + names.get(i));
            }
        } else if (numberOfStrangers == 0) {
            System.out.println("Oh, it looks like there is no one here");
        } else {
            System.out.println("Seriously? Why so negative?");
        }
    }
}
