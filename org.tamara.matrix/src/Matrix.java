import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Matrix {

    private double[][] matrix;
    private int rowsCount;
    private int columnsCount;

    public static Matrix sum(Matrix matrix1, Matrix matrix2) throws Exception {
        return sum(matrix1, matrix2, 1);
    }

    public static Matrix sub(Matrix matrix1, Matrix matrix2) throws Exception {
        return sum(matrix1, matrix2, -1);
    }

    public static Matrix mul(Matrix matrix1, Matrix matrix2) throws Exception {
        if (matrix1.columnsCount == matrix2.rowsCount) {
            Matrix resultMatrix = new Matrix(matrix1.rowsCount, matrix2.columnsCount);
            for (int i = 0; i < matrix1.rowsCount; i++) {
                for (int j = 0; j < matrix2.columnsCount; j++) {
                    for (int k = 0; k < matrix1.columnsCount; k++) {
                        resultMatrix.matrix[i][j] += matrix1.matrix[i][k] * matrix2.matrix[k][j];
                    }
                }
            }
            return resultMatrix;
        } else {
            throw new Exception("Invalid data format. Columns count of first matrix is not equal rows count of second matrix.");
        }
    }

    private static Matrix sum(Matrix matrix1, Matrix matrix2, int sign) throws Exception {
        if (matrix1.rowsCount == matrix2.rowsCount && matrix1.columnsCount == matrix2.columnsCount) {
            Matrix resultMatrix = new Matrix(matrix1.rowsCount, matrix1.columnsCount);
            for (int i = 0; i < matrix1.rowsCount; i++) {
                for (int j = 0; j < matrix1.columnsCount; j++) {
                    resultMatrix.matrix[i][j] = matrix1.matrix[i][j] + sign * matrix2.matrix[i][j];
                }
            }
            return resultMatrix;
        } else {
            throw new Exception("Invalid data format. Matrices have different sizes.");
        }
    }

    public Matrix(int rowsCount, int columnsCount) {
        matrix = new double[rowsCount][columnsCount];
        this.rowsCount = rowsCount;
        this.columnsCount = columnsCount;
    }

    public Matrix(double[][] matrixArray) throws Exception {
        double[][] matrixClone = new double[matrixArray.length][];
        for (int i = 0; i < matrixArray.length; i++) {
            matrixClone[i] = new double[matrixArray[i].length];
            System.arraycopy(matrixArray[i], 0, matrixClone[i], 0, matrixArray[i].length);
        }
        matrix = matrixClone;
        rowsCount = matrix.length;
        for (int i = 0; i < matrix.length - 1; i++) {
            if (matrix[i] != null && matrix[i].length == matrix[i + 1].length) {
                columnsCount = matrix[i].length;
            } else {
                throw new Exception("Invalid data format. Sub-arrays have different sizes.");
            }
        }
    }

    public Matrix(String path) throws Exception, FileNotFoundException, IOException {
        BufferedReader reader = new BufferedReader(new FileReader(path));
        ArrayList<String> lines = new ArrayList<String>();
        String str;
        while ((str = reader.readLine()) != null) {
            if (!str.isEmpty()) {
                lines.add(str);
            }
        }
        reader.close();
        int size = 0;
        matrix = new double[lines.size()][];
        for (int i = 0; i < lines.size(); i++) {
            String[] s = lines.get(i).split("\\s+");
            matrix[i] = new double[s.length];

            if (i == 0) {
                size = s.length;
            }
            if (size == s.length) {
                for (int j = 0; j < s.length; j++) {
                    matrix[i][j] = Double.parseDouble(s[j]);
                }
            } else {
                throw new Exception("Invalid data format.");
            }
        }
        rowsCount = matrix.length;
        columnsCount = matrix[0].length;
    }

    public int rows() {
        return rowsCount;
    }

    public int columns() {
        return columnsCount;
    }

    public double get(int i, int j) {
        return matrix[i][j];
    }

    public Matrix set(int i, int j, double value) {
        matrix[i][j] = value;
        return this;
    }

    public double det() throws Exception {
        return det(new boolean[columnsCount], rowsCount);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < rowsCount; i++) {
            sb.append('|');
            for (int j = 0; j < columnsCount; j++) {
                sb.append(matrix[i][j]);
                if (j < columnsCount - 1) {
                    sb.append('\t');
                }
            }
            sb.append('|');
            if (i < rowsCount - 1) {
                sb.append('\n');
            }
        }
        return sb.toString();
    }

    public Matrix transpose() {
        Matrix resultMatrix = new Matrix(columnsCount, rowsCount);
        for (int i = 0; i < columnsCount; i++) {
            for (int j = 0; j < rowsCount; j++) {
                resultMatrix.matrix[i][j] = matrix[j][i];
            }
        }
        return resultMatrix;
    }

    public Matrix mul(double number) {
        Matrix resultMatrix = new Matrix(rowsCount, columnsCount);
        for (int i = 0; i < columnsCount; i++) {
            for (int j = 0; j < rowsCount; j++) {
                resultMatrix.matrix[i][j] = matrix[i][j] * number;
            }
        }
        return resultMatrix;
    }

    public Matrix div(double number) {
        return mul(1 / number);
    }

    private double det(boolean[] disabledColumns, int size) throws Exception {
        if (rowsCount == columnsCount) {
            if (size == 1) {
                for (int i = 0; i < disabledColumns.length; i++) {
                    if (!disabledColumns[i]) {
                        return matrix[rowsCount - 1][i];
                    }
                }
            }
            double det = 0;
            int sign = 1;
            for (int i = 0; i < columnsCount; i++) {
                if (disabledColumns[i]) {
                    continue;
                }
                disabledColumns[i] = true;
                double minor = det(disabledColumns, size - 1);
                disabledColumns[i] = false;
                det += matrix[rowsCount - size][i] * minor * sign;
                sign *= -1;
            }
            return det;
        } else {
            throw new Exception("Matrix is not square, so it is impossible to calc determinant.");
        }
    }
}