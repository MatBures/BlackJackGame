package Services;

import java.util.Scanner;


public class InputValidatorService {
    private Scanner validateScanner = new Scanner(System.in);

    //Method for checking if scanned input is integer. If not, its returned and user needs to input number again.
    public int getValidatedIntegerInput() {
        int inputtedInteger;
        if (validateScanner.hasNextInt()) {
            inputtedInteger = validateScanner.nextInt();
            return inputtedInteger;
        }
        validateScanner.next();
        System.out.println("Invalid input. You need to write a number.");
        return getValidatedIntegerInput();
    }

}


