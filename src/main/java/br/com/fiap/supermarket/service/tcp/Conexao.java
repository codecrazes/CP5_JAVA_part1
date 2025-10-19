package br.com.fiap.supermarket.service.tcp;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Conexao {

    private Socket socket;
    private DataInputStream input;
    private DataOutputStream output;

    public Conexao(Socket socket) throws IOException {
        this.socket = socket;
        this.input = new DataInputStream(socket.getInputStream());
        this.output = new DataOutputStream(socket.getOutputStream());
    }

    public void enviarChavePublica(int e, int n) throws IOException {
        output.writeInt(e);
        output.writeInt(n);
        output.flush();
    }

    public int[] receberChavePublica() throws IOException {
        int e = input.readInt();
        int n = input.readInt();
        return new int[]{e, n};
    }

    public void enviarMensagemCriptografada(List<Integer> dados) throws IOException {
        output.writeInt(dados.size());
        for (int num : dados) {
            output.writeInt(num);
        }
        output.flush();
    }

    public List<Integer> receberMensagemCriptografada() throws IOException {
        int tamanho = input.readInt();
        List<Integer> dados = new ArrayList<>();
        for (int i = 0; i < tamanho; i++) {
            dados.add(input.readInt());
        }
        return dados;
    }

    public void fechar() {
        try {
            if (socket != null) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
