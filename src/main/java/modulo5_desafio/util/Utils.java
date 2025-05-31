package modulo5_desafio.util;

public class Utils {
    public static void printSuccess(String msg) {
        System.out.println("\u001B[32m" + msg + "\u001B[0m"); // verde
    }

    public static void printError(String msg) {
        System.err.println("\u001B[31m" + msg + "\u001B[0m"); // vermelho
    }
}
