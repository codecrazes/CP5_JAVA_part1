package br.com.fiap.main;

public class ServerMain {
    public static void main(String[] args) {
        br.com.fiap.supermarket.service.tcp.ServidorTCP.iniciar();
    }
}
