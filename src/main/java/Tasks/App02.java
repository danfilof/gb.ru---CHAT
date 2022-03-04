package Tasks;

public class App02 {
//    Написать метод, который проверяет состав массива из чисел 1 и 4. Если в нем нет хоть одной четверки или единицы,
//    то метод вернет false; Написать набор тестов для этого метода (по 3-4 варианта входных данных).
//            [ 1 1 1 4 4 1 4 4 ] -> true
//            [ 1 1 1 1 1 1 ] -> false
//            [ 4 4 4 4 ] -> false
//            [ 1 4 4 1 1 4 3 ] -> false

    public static void main(String[] args) {
        int[] arr = {1,1,0,2,3,4,9,1};

    }

    public static boolean taskTwo(int[] arr) {
        boolean valueOne = false;
        boolean valueFour = false;

        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == 1) valueOne = true;
            if (arr[i] == 4) valueFour = true;

            if (valueOne & valueFour) break;
        }
        if (valueOne & valueFour) {
            return true;
        } else {
            return false;
        }
    }

}
