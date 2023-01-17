//imports
import java.io.File;
import java.io.IOException;
import java.io.FileWriter;
import java.util.Scanner;
import java.net.InetAddress;
import java.net.UnknownHostException;

//main class
class Main {
    //import colours
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_YELLOW = "\033[0;93m";
    public static final String ANSI_CYAN = "\033[4;36m";

    //sort by score method
    static void scoreSort(Person[] arr) {
        Person temp = new Person("", 0);

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9 - i; j++) {
                if (arr[j].score < arr[j + 1].score) {
                    temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                }
            }
        }
    }

    //sort by first letter method
    static void firstLetterSort(Person[] arr) {
        Person temp = new Person("", 0);

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9 - i; j++) {
                if (arr[j].name.charAt(0) > arr[j + 1].name.charAt(0)) {
                    temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                }
            }
        }
    }

    //sort by full name method
    static void fullNameSort(Person[] arr, int n) {
        if (n == 1) {
            return;
        }

        Person temp = new Person("", 0);

        for (int i = 0; i < n - 1; i++) {
            if (arr[i].name.compareTo(arr[i + 1].name) > 0) {
                temp = arr[i];
                arr[i] = arr[i+1];
                arr[i+1] = temp;
            }
        }

        fullNameSort(arr, n - 1);
    }

    //search method
    static int binarySearchMethod(Person[] arr, String key) {
        int mid = 5;
        int first = 0;
        int last = 9;

        while (first <= last) {
            if (arr[mid].name.compareTo(key) < 0) {
                first = mid + 1;
            }
            else if (arr[mid].name.equalsIgnoreCase(key)) {
                return mid;
            }
            else {
                last = mid - 1;
            }

            mid = (first + last) / 2;

            if (first > last) {
                return -1;
            }
        }

        return -1;
    }

    //output scoreboard method
    static void displayScoreboard(Person[] arr, int max) {
        System.out.println(ANSI_CYAN);
        System.out.printf("%-15s", "Name");
        System.out.printf("%-15s", "Score");
        System.out.println(ANSI_RESET);

        for (int i = 0; i < 10; i++) {
            if (arr[i].score == max) {
                System.out.print(ANSI_RED);
                System.out.printf("%-15s", arr[i].name);
                System.out.println(arr[i].score);
                System.out.print(ANSI_RESET);
            }
            else {
                System.out.print(ANSI_YELLOW);
                System.out.printf("%-15s", arr[i].name);
                System.out.println(arr[i].score);
                System.out.print(ANSI_RESET);
            }
        }
    }

    //add score method
    static Person addScore(Person[] arr, String tempName, int tempScore) {
        Person temp = new Person("", 0);

        temp.name = tempName;
        temp.score = tempScore;

        return temp;
    }

    //main method
    public static void main(String[] args) throws IOException, UnknownHostException {
        Scanner input = new Scanner(System.in);

        //declare array of objects
        Person[] arrayOfPersons = new Person[10];

        //declare variables
        char menu;
        int index, tempScore;
        int lowest = 2147483647;
        int highscore = -1;
        String tempName;

        File myFile = new File("scoreboard.txt");

        //read from file
        Scanner myReader = new Scanner (myFile);

        for (int i = 0; i < 10; i++) {
            tempName = myReader.nextLine();
            tempScore = Integer.parseInt(myReader.nextLine());

            arrayOfPersons[i] = new Person(tempName, tempScore);

            //track highest and lowest scores
            if (highscore < tempScore) {
                highscore = tempScore;
            }
            if (lowest > tempScore) {
                lowest = tempScore;
            }
        }

        myReader.close();

        System.out.println("\nWelcome to the scoreboard! \uD83D\uDE00");

        //ping host IP address
        InetAddress host = InetAddress.getByName(InetAddress.getLocalHost().getHostAddress());
        System.out.println("\nSending Ping Request to " + InetAddress.getLocalHost().getHostAddress());

        if (host.isReachable(5000)) {
            System.out.println("Host is reachable.");
        }
        else {
            System.out.println("Sorry! We can't reach to this host.");
        }

        while (true) {
            //menu with emojis
            System.out.println("\nChoose an option:\n\ta. sort by score \uD83D\uDCAF\n\tb. sort by first letter of name \uD83D\uDD20\n\tc. sort by name \uD83D\uDD24\n\td. add score \u2795\n\te. delete entry \u2796\n\tf. search name \uD83D\uDD0D\n\tg. save results \uD83D\uDCBE\n\th. exit the program \u274C");
            menu = input.nextLine().charAt(0);

            //sort by score
            if (menu == 'a' || menu == 'A') {
                scoreSort(arrayOfPersons);

                displayScoreboard(arrayOfPersons, highscore);
            }
            //sort by first letter of name
            else if (menu == 'b' || menu == 'B') {
                firstLetterSort(arrayOfPersons);

                displayScoreboard(arrayOfPersons, highscore);
            }
            //sort by name
            else if (menu == 'c' || menu == 'C') {
                fullNameSort(arrayOfPersons, 10);

                displayScoreboard(arrayOfPersons, highscore);
            }
            //add score
            else if (menu == 'd' || menu == 'D') {
                scoreSort(arrayOfPersons);

                System.out.println("\nEnter the person's name.");
                tempName = input.nextLine();
                System.out.println("\nEnter the person's score.");
                tempScore = Integer.parseInt(input.nextLine());

                if (tempScore > lowest) {
                    arrayOfPersons[9] = addScore(arrayOfPersons, tempName, tempScore);

                    if (tempScore > highscore) {
                        highscore = tempScore;
                    }
                    else if (tempScore < arrayOfPersons[8].score) {
                        lowest = tempScore;
                    }
                    else {
                        lowest = arrayOfPersons[8].score;
                    }

                    scoreSort(arrayOfPersons);

                    displayScoreboard(arrayOfPersons, highscore);
                }
                else {
                    System.out.println("\nThis player does not make the scoreboard.");
                }
            }
            //delete entry
            else if (menu == 'e' || menu == 'E') {
                System.out.println("\nEnter the name of the player.");
                tempName = input.nextLine();

                for (int i = 0; i < 10; i++) {
                    if (arrayOfPersons[i].name.equalsIgnoreCase(tempName)) {
                        arrayOfPersons[i].name = "N/A";
                        arrayOfPersons[i].score = 0;
                        lowest = -1;
                    }
                }

                if (lowest == -1) {
                    scoreSort(arrayOfPersons);

                    displayScoreboard(arrayOfPersons, highscore);

                    lowest--;
                }
                else {
                    System.out.println("\nThe player you entered is not on the scoreboard.");
                }
            }
            //search name
            else if (menu == 'f' || menu == 'F') {
                fullNameSort(arrayOfPersons, 10);

                System.out.println("\nEnter the name of the person you would like to search.");

                index = binarySearchMethod(arrayOfPersons, input.nextLine());

                if (index != -1) {
                    System.out.println("\nName: " + arrayOfPersons[index].name + "\nScore: " + arrayOfPersons[index].score);
                }
                else {
                    System.out.println("\nThe player you entered is not on the scoreboard.");
                }
            }
            //save results
            else if (menu == 'g' || menu == 'G') {
                FileWriter myWriter = new FileWriter("scoreboard.txt");

                for (int i = 0; i < 10; i++) {
                    myWriter.write(arrayOfPersons[i].name + "\n" + arrayOfPersons[i].score + "\n");
                }

                myWriter.close();
            }
            else {
                //exit the program
                try {
                    if (menu != 'h' && menu != 'H') {
                        throw new Exception();
                    }

                    break;
                }
                //error message when input does not match menu
                catch (Exception e) {
                    System.out.println("\nPlease choose one of the options.");
                }
            }
        }
    }
}