import java.util.Arrays;

public class Main {
    private static int[] privateKey = {2, 7, 11, 21, 42, 89, 180, 354};  //
    private static int r = 588;                                          // секретный ключ
    private static int q = 881;                                          //

    private static String s = "Everything is working!!!";  //исходный текст

    public static void main(String[] args) {
        int[] publicKey = getPublicKey(privateKey, r, q);
        int[] k = encrypt(publicKey, s);
        System.out.println("Шифротекст: " + Arrays.toString(k));
        System.out.println(">>>");
        System.out.println("Расшифрованное сообщение: " + decrypt(privateKey, r, q, k));
    }

    private static int[] encrypt(int[] b, String s) { //функция шифрования
        byte[][] text = textToBinary(s);
        int[] sum = new int[text.length];
        for (int i = 0; i < text.length; i++) {
            for (int j = 0; j < text[i].length; j++) {
                sum[i] += text[i][j] * b[j];
            }
        }
        return sum;
    }

    private static String decrypt(int[] w, int r, int q, int[] sum) { //функция расшифровки
        byte[][] text = new byte[sum.length][8];
        int[] rInverse = new int[sum.length];
        for (int i = 0; i < rInverse.length; i++) {
            rInverse[i] = sum[i] * multiplicativeInverse(r, q) % q;
        }
        for (int k = 0; k < rInverse.length; k++) {
            for (int i = w.length - 1; i >= 0; i--) {
                if (w[i] <= rInverse[k]) {
                    rInverse[k] -= w[i];
                    text[k][i] = 1;
                } else text[k][i] = 0;
            }
        }
        return binaryToText(text);
    }

    private static int[] getPublicKey(int[] w, int r, int q) {  // генерация открытого ключа
        int[] b = new int[w.length];
        for (int i = 0; i < b.length; i++) {
            b[i] = w[i] * r % q;
        }
        return b;
    }

    private static byte[][] textToBinary(String s) {  //перевод текста в двоичный вид
        byte[] bytes = s.getBytes();
        byte[][] binary = new byte[bytes.length][8];
        for (int i = 0; i < bytes.length; i++) {
            int val = bytes[i];
            for (int k = 0; k < 8; k++) {
                binary[i][k] = (val & 128) == 0 ? (byte) 0 : 1;
                val <<= 1;
            }
        }

        return binary;
    }

    private static String binaryToText(byte[][] text) {  //перевод двоичной последовательности в текст
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < text.length; i++) {
            StringBuilder cur = new StringBuilder();
            for (int j = 0; j < text[0].length; j++) {
                cur.append(text[i][j]);
            }
            result.append((char) Integer.parseInt(cur.toString(), 2));
        }
        return result.toString();
    }

    private static int multiplicativeInverse(int r, int q) {  // мультипликативная инверсия
        for (int i = 1; i < q; i++) {
            if (r * i % q == 1) return i;
        }
        return -1; //not found
    }
}