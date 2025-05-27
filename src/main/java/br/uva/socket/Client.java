/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.uva.socket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    
    private static void pause() {
        System.out.println("Pressione qualquer tecla para continuar...");
        new Scanner(System.in).nextLine();
    }

    private static void printMenu() {
        System.out.println("Operacoes:\n[0] Sair");
        for(SocketProgram.Operation op : SocketProgram.Operation.values())
            System.out.printf("[%d] %s%n", (int) op.id, op.displayName);
        System.out.print("Escolha uma operação\n> ");
    }

    private static void request(SocketProgram.Operation op, double a, double b, DataOutputStream out, DataInputStream in) throws IOException {
        System.out.printf("Enviando requisicao para %s %.2f com %.2f%n", op.displayName, a, b);
        out.writeByte(op.id);
        out.writeDouble(a);
        out.writeDouble(b);
        out.flush();
        System.out.println("Response:\n" + in.readUTF());
    }
    
    public static void main(String[] args) {
        System.out.println("Iniciando cliente...");
        try(final Socket socket = new Socket("localhost", 12345);
                final DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                final DataInputStream in = new DataInputStream(socket.getInputStream())) {
            System.out.println("Conectado ao servidor.");
            final Scanner scanner = new Scanner(System.in);
            while(true) {
                printMenu();
                final int opId = scanner.nextInt();
                if(opId == 0)
                    return;
                final SocketProgram.Operation op = SocketProgram.Operation.findById((byte) opId);
                if(op == null) {
                    System.out.println("Operacao inexistente.");
                    pause();
                    continue;
                }
                final double a, b;
                System.out.print("Num. A: ");
                a = scanner.nextDouble();
                System.out.print("Num. B: ");
                b = scanner.nextDouble();
                request(op, a, b, out, in);
                pause();
            }
        } catch(Exception exception) {
            exception.printStackTrace();
        }
    }   
}
