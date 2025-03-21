/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.uva.socket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Scanner;

/**
 *
 * @author 19485701730
 */
public class Client {
    
    private static void pause() {
        System.out.println("Pressione qualquer tecla para continuar...");
        new Scanner(System.in).nextLine();
    }
    
    public static void main(String[] args) {
        System.out.println("Iniciando cliente...");
        try(final Socket socket = new Socket("localhost", 12345);
                final DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                final DataInputStream in = new DataInputStream(socket.getInputStream())) {
            System.out.println("Conectado ao servidor.");
            boolean run = true;
            final Scanner scanner = new Scanner(System.in);
            while(run) {
                System.out.println("Operacoes:\n[0] Sair");
                for(SocketProgram.Operacao op : SocketProgram.Operacao.values()) {
                    System.out.println("[%d] %s".formatted((int) op.id, op.displayName));
                }
                System.out.print("Escolha uma operação\n> ");
                final int opId = scanner.nextInt();
                if(opId == 0) {
                    return;
                }
                final SocketProgram.Operacao op = SocketProgram.Operacao.findById((byte) opId);
                if(op == null) {
                    System.out.println("Operacao inexistente.");
                    continue;
                }
                final double a, b;
                System.out.print("A: ");
                a = scanner.nextDouble();
                System.out.print("B: ");
                b = scanner.nextDouble();
                System.out.println("Enviando requisicao para %s %.2f com %.2f".formatted(op.displayName, a, b));
                out.writeByte(op.id);
                out.writeDouble(a);
                out.writeDouble(b);
                out.flush();
                String response;
                while((response = in.readUTF()) != null) {
                    System.out.println(response);
                }
                System.out.println("a");
                pause();
            }
            out.close();
            socket.close();
        } catch(Exception exception) {
            exception.printStackTrace();
        }
    }   
}
