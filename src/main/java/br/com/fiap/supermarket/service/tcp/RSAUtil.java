package br.com.fiap.supermarket.service.tcp;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class RSAUtil {

    private static final int P = 17;
    private static final int Q = 23;
    private static final int N = P * Q;         // 391
    private static final int PHI = (P - 1) * (Q - 1); // 352
    private static final int E = 3;            // chave pública
    private static final int D = 235;          // chave privada

    public static int getPublicE() { return E; }
    public static int getPublicN() { return N; }
    public static int getPrivateD() { return D; }
    public static int getPrivateN() { return N; }

    public static List<Integer> encrypt(String mensagem, int e, int n) {
        List<Integer> criptografado = new ArrayList<>();

        for (char caractere : mensagem.toCharArray()) {
            int ascii = caractere;
            BigInteger m = BigInteger.valueOf(ascii);
            BigInteger c = m.modPow(BigInteger.valueOf(e), BigInteger.valueOf(n));
            criptografado.add(c.intValue());
        }
        return criptografado;
    }

    public static String decrypt(List<Integer> dadosCriptografados, int d, int n) {
        StringBuilder mensagem = new StringBuilder();

        for (Integer cInt : dadosCriptografados) {
            BigInteger c = BigInteger.valueOf(cInt);
            BigInteger m = c.modPow(BigInteger.valueOf(d), BigInteger.valueOf(n));
            mensagem.append((char) m.intValue());
        }
        return mensagem.toString();
    }
}
