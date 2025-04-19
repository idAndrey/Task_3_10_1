
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ExecutionException;


public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        double a, b, c, d, formulaResult;

        System.out.println();
        System.out.println("Расчёт формулы (a ^ 2 + b ^ 2) * log(c) / sqrt(d)");
        System.out.println("Введите значения переменных A, B, C и D через пробел,");
        System.out.println("(значения переменных C и D больше нуля)");
        System.out.print("==> ");

        a = scanner.nextDouble();
        b = scanner.nextDouble();
        c = scanner.nextDouble();
        d = scanner.nextDouble();
        scanner.close();

        System.out.println();

        if (c <= 0) {
            System.out.println("Ошибка!\n Введите значение С больше нуля.");
            return;
        }
        if (d <= 0) {
            System.out.println("Ошибка!\n Введите значение D больше нуля.");
            return;
        }

        try {
            CompletableFuture<Double> summa = CompletableFuture.supplyAsync(() -> calculateSumma(a, b));
            CompletableFuture<Double> logC = CompletableFuture.supplyAsync(() -> calculateLog(c));
            CompletableFuture<Double> sqrtD = CompletableFuture.supplyAsync(() -> calculateSqrt(d));

            formulaResult = summa.thenCombine(logC, (squares, logValue) -> squares * logValue)
                    .thenCombine(sqrtD, (partialResult, sqrtValue) -> partialResult / sqrtValue)
                    .get();

            System.out.println();
            System.out.println("Final result of the formula: " + formulaResult);

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    private static double calculateSumma(double a, double b) {

        String message = "Вычисляем сумму в потоке " + Thread.currentThread().getName();
        delaySimulation(9, message);

        double resultSumma;
        resultSumma = a * a + b * b;
        System.out.println("Calculating sum of squares: " + resultSumma);
        return resultSumma;
    }

    private static double calculateLog(double c) {

        String message = "Вычисляем логарифм в потоке " + Thread.currentThread().getName();
        delaySimulation(6, message);

        double resultLog = Math.log(c);
        System.out.println("Calculating log(c): " + resultLog);
        return resultLog;
    }

    private static double calculateSqrt(double d) {

        String message = "Вычисляем корень в потоке " + Thread.currentThread().getName();
        delaySimulation(3, message);

        double result = Math.sqrt(d);
        System.out.println("Calculating sqrt(d): " + result);
        return result;
    }

    private static void delaySimulation(int seconds, String message) {
        try {
            System.out.println(message);
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}