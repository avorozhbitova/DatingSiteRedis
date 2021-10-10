import java.util.Scanner;

public class RedisTest {
    public static final int COUNT_OF_USERS = 20;
    public static final int FREQUENCY_OF_PAID_CASE = 10;
    public static final int MILLIS_TO_SLEEP = 1000;
    public static final String KEY = "users";

    private static int score = 1;
    private static RedisStorage redis;

    public static void main(String[] args) throws InterruptedException {
        Scanner scanner = new Scanner(System.in);
        String input = "";

        redis = new RedisStorage(KEY);
        redis.init();

        info();
        while (!stop(input)) {
            input = scanner.nextLine();
            if (countCase(input)) {
                showNumberOfRegisteredUsers();
            } else if (listCase(input)) {
                redis.list();
            } else {
                showCase();
            }
            score = 0;
            Thread.sleep(MILLIS_TO_SLEEP);
        }
        redis.shutdown();
    }

    private static void info() {
        System.out.println("Для продолжения работы программы, введите любой символ\n" +
                "Для просмотра всех зарегистрированных пользователей, введите list\n" +
                "Для просмотра количества зарегистрированных пользователей, введите count\n" +
                "Для выхода из программы, введите stop");
    }

    private static boolean stop(String input) {
        return input.equals("stop");
    }

    private static boolean countCase(String input) {
        return input.equals("count");
    }

    private static boolean listCase(String input) {
        return input.equals("list");
    }

    private static void showNumberOfRegisteredUsers() {
        System.out.println("Количество зарегистрированных пользователей: " + redis.count());
    }

    private static void showCase() {
        for (int i = 1; i <= COUNT_OF_USERS; i++) {
            redis.registerUser(score, i);
            if (score % FREQUENCY_OF_PAID_CASE == 0) {
                String paidUser = redis.getRandomUser();
                System.out.printf("> Пользователь %s оплатил платную услугу\n", paidUser);
                showUser(paidUser);
            }
            showUser(String.valueOf(i));
            score++;
        }
    }

    private static void showUser(String id) {
        System.out.printf("На главной странице показываем пользователя %s\n", id);
    }
}