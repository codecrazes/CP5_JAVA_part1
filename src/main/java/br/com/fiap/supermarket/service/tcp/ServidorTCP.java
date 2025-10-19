package br.com.fiap.supermarket.service.tcp;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class ServidorTCP {

    private static final int PORTA = 5000;

    public static void iniciar() {
        try (ServerSocket serverSocket = new ServerSocket(PORTA)) {
            System.out.println("✅ Servidor aguardando conexão na porta " + PORTA);

            while (true) {
                Socket socket = serverSocket.accept();
                new Thread(() -> {
                    try {
                        Conexao conexao = new Conexao(socket);

                        int[] chaveCliente = conexao.receberChavePublica();
                        int eCliente = chaveCliente[0];
                        int nCliente = chaveCliente[1];
                        System.out.println("🔑 Chave pública do Cliente recebida: (" + eCliente + ", " + nCliente + ")");

                        conexao.enviarChavePublica(RSAUtil.getPublicE(), RSAUtil.getPublicN());
                        System.out.println("🔑 Chave pública do Servidor enviada: (" + RSAUtil.getPublicE() + ", " + RSAUtil.getPublicN() + ")");

                        List<Integer> dadosCriptografados = conexao.receberMensagemCriptografada();
                        System.out.println("📥 Mensagem recebida (cifrada): " + dadosCriptografados);

                        String mensagem = RSAUtil.decrypt(dadosCriptografados, RSAUtil.getPrivateD(), RSAUtil.getPrivateN());
                        System.out.println("📥 Mensagem decriptada: " + mensagem);

                        String resposta = "Servidor recebeu sua mensagem: " + mensagem;
                        List<Integer> respostaCript = RSAUtil.encrypt(resposta, eCliente, nCliente);
                        conexao.enviarMensagemCriptografada(respostaCript);
                        System.out.println("📤 Resposta enviada (cifrada).");

                        conexao.fechar();
                        System.out.println("❌ Conexão encerrada.\n");

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }).start();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
