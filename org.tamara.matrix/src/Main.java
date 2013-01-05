import java.io.FileNotFoundException;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws FileNotFoundException, IOException, Exception {
        Matrix m1 = new Matrix("1.txt");
        Matrix m2 = m1.mul(10);
        System.out.println(m2);
        System.out.println(m2.div(3));
    }
}