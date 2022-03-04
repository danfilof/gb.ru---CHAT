package Tasks;

import java.util.Arrays;

public class App01 {

//    Написать метод, которому в качестве аргумента передается не пустой одномерный целочисленный массив. Метод должен вернуть новый массив,
//    который получен путем вытаскивания из исходного массива элементов, идущих после последней четверки.
//    Входной массив должен содержать хотя бы одну четверку, иначе в методе необходимо выбросить RuntimeException.
//    Написать набор тестов для этого метода (по 3-4 варианта входных данных).
//    Вх: [ 1 2 4 4 2 3 4 1 7 ] -> вых: [ 1 7 ].

    public static void main(String[] args) {
        Integer[] arr = {1, 2, 4, 4, 2, 3, 4, 1, 7};
        System.out.println(Arrays.toString(arr) + " -------> " + Arrays.toString(taskOneMethod(arr)));
    }


    public static <T extends Number> T[] taskOneMethod(final T[] arr) throws taskOneException {
        T[] r = null;

        // Начинаю счетние массива с конца, дабы сразу обнаружить нужную 4-ку
        for (int i = arr.length - 1; i > 0; i--) {
            if (arr[i].equals(4)) {
                r = (T[]) new Number[arr.length - i - 1];
                // Использую System.arraycopy, с которым сталкивались в уроке про многопоточность в java курс 2
                System.arraycopy(arr, i + 1, r, 0, arr.length - i - 1);
                break;
            }
        }
        if (r == null) {
            throw new taskOneException("Неверно введенный массив");
        }
        return r;
    }


    static class taskOneException extends RuntimeException {
        public taskOneException(String message) {
            super(message);
        }
    }
}



