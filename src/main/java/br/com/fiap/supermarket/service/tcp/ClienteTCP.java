package br.com.fiap.supermarket.service.tcp;

import java.net.Socket;
import java.util.List;

public class ClienteTCP {

    private static final String HOST = "localhost";
    private static final int PORTA = 5000;

    public static String enviarMensagem(String mensagem) {
        try (Socket socket = new Socket(HOST, PORTA)) {
            Conexao conexao = new Conexao(socket);

            conexao.enviarChavePublica(RSAUtil.getPublicE(), RSAUtil.getPublicN());

            int[] chaveServidor = conexao.receberChavePublica();
            int eServidor = chaveServidor[0];
            int nServidor = chaveServidor[1];

            List<Integer> dadosCript = RSAUtil.encrypt(mensagem, eServidor, nServidor);
            conexao.enviarMensagemCriptografada(dadosCript);

            List<Integer> respostaCriptografada = conexao.receberMensagemCriptografada();
            String resposta = RSAUtil.decrypt(respostaCriptografada, RSAUtil.getPrivateD(), RSAUtil.getPrivateN());

            conexao.fechar();
            return resposta;

        } catch (Exception e) {
            e.printStackTrace();
            return "Erro ao enviar mensagem: " + e.getMessage();
        }
    }
}
