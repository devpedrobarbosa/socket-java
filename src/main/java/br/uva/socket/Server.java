/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.uva.socket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author 19485701730
 */
public class Server {
    
    public static void main(String[] args) {
        System.out.println("Iniciando servidor...");
        try(final ServerSocket server = new ServerSocket(12345)) {
                System.out.println("Aguardando conexoes...");
            boolean run = true;
            while(run) {
                try(final Socket socket = server.accept();
                        final DataInputStream in = new DataInputStream(socket.getInputStream());
                        final DataOutputStream out = new DataOutputStream(socket.getOutputStream())) {
                    System.out.println("Cliente conectado. Aguardando operações...");
                    while(socket.isConnected()) {
                        byte opId;
                        try {
                            opId = in.readByte();
                            System.out.println("Operação recebida: " + opId);
                            if(opId == 0) {
                                run = false;
                                break;
                            }
                            final SocketProgram.Operacao op = SocketProgram.Operacao.findById(opId);
                            if(op == null) {
                                out.writeUTF("Operacao inexistente.");
                                out.flush();
                                continue;
                            }
                            final double a = in.readDouble(), b = in.readDouble(), result = op.make(a, b);
                            if(op.invalidate(a, b)) {
                                out.writeUTF("Operacao invalida.");
                                out.flush();
                                continue;
                            }
                            System.out.println("Respondendo operacao " + opId);
                            out.writeUTF("%s %.2f com %.2f = %.2f".formatted(op.displayName, a, b, result));
                            out.flush();
                        } catch(EOFException exception) {
                            System.out.println("Aguardando próxima operação...");
                            break;
                        }
                    }
                    in.close();
                    socket.close();
                }
            }
            System.out.println("Fechando servidor...");
            server.close();
        } catch(Exception exception) {
            exception.printStackTrace();
        }
    }
}
