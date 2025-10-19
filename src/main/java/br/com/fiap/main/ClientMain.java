package br.com.fiap.main;

public class ClientMain {
    public static void main(String[] args) {
        String resp = br.com.fiap.supermarket.service.tcp.ClienteTCP.enviarMensagem("Olá servidor");
        System.out.println("Resposta do servidor: " + resp);
    }
}