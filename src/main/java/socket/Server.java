package socket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class Server {
    public static final String LOCALHOST = "127.0.0.1";
    public static final int SERVER_PORT = 4444;

    public static void main(String[] args) {
        try {
            final ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.bind(new InetSocketAddress(LOCALHOST, SERVER_PORT));
            end:
            while (true) {
                try (SocketChannel socketChannel = serverSocketChannel.accept()) {
                    final ByteBuffer inputBuffer = ByteBuffer.allocate(2 << 10);
                    while (socketChannel.isConnected()) {
                        int bytesCount = socketChannel.read(inputBuffer);
                        if (bytesCount == -1) break end;
                        final String msg = new String(inputBuffer.array(), 0, bytesCount, StandardCharsets.UTF_8);
                        inputBuffer.clear();
                        Integer number = Integer.parseInt(msg);
                        System.out.println("Получено число от клиента: " + number);
                        Integer numberFibonacci = getNumberFibonacci(number);
                        //socketChannel.write(ByteBuffer.wrap(inputBuffer.putInt(numberFibonacci).array()));
                        String inputMsg = String.valueOf(numberFibonacci);
                        socketChannel.write(ByteBuffer.wrap((number + "-е число ряда Фибоначчи равно: "
                                + inputMsg).getBytes(StandardCharsets.UTF_8)));
                    }
                } catch (NumberFormatException e) {
                    System.out.println(e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static Integer getNumberFibonacci(Integer number) {
        if (number == 0 || number == 1) return number;
        Integer numberOne = 0;
        Integer numberTwo = 1;
        Integer sum = 0;
        for (int i = 2; ; i++) {
            sum = numberOne + numberTwo;
            if (i == number) break;
            numberOne = numberTwo;
            numberTwo = sum;
        }
        //System.out.println("Число Фибоначчи: " + sum);
        return sum;
    }
}
