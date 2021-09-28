package socket;
//java.nio позволяет отправлять последующие запросы на сервер не дождавшись ответа на предыдущие
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Client {
    public static final String LOCALHOST = "127.0.0.1";
    public static final int SERVER_PORT = 4444;
    public static final int TIMEOUT = 3000;
    public static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        InetSocketAddress socketAddress = new InetSocketAddress(LOCALHOST, SERVER_PORT);
        SocketChannel socketChannel = null;
        try {
            socketChannel = SocketChannel.open();
            socketChannel.connect(socketAddress);
            final ByteBuffer inputBuffer = ByteBuffer.allocate(2 << 10);
            String msg;
            while (true) {
                System.out.println("Введите номер числа ряда Фибоначчи (\'end\' - для выхода)...");
                msg = scanner.nextLine();
                if ("end".equals(msg)) break;
                socketChannel.write(ByteBuffer.wrap(msg.getBytes(StandardCharsets.UTF_8)));
                //Thread.sleep(TIMEOUT);
                int bytesCount = socketChannel.read(inputBuffer);
                System.out.println(new String(inputBuffer.array(), 0, bytesCount, StandardCharsets.UTF_8).trim());
                inputBuffer.clear();
            }
        } catch (Exception e) {
            System.out.println("Введены некорректные данные");
        } finally {
            scanner.close();
            try {
                socketChannel.close();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
