package dev.mv.engine.math.matrix;

public class Matrix4f {

    private float[][] matrix = new float[4][4];

    public Matrix4f(float[][] matrix) {
        this.matrix = matrix;
    }

    public Matrix4f(float a, float b, float c, float d,
                    float e, float f, float g, float h,
                    float i, float j, float k, float l,
                    float m, float n, float o, float p) {
        matrix[0][0] = a; matrix[0][1] = b; matrix[0][2] = c; matrix[0][3] = d;
        matrix[1][0] = e; matrix[1][1] = f; matrix[1][2] = g; matrix[1][3] = h;
        matrix[2][0] = i; matrix[2][1] = j; matrix[2][2] = k; matrix[2][3] = l;
        matrix[3][0] = m; matrix[3][1] = n; matrix[3][2] = o; matrix[3][3] = p;
    }

    public Matrix4f() {
        matrix[0][0] = 1;
        matrix[1][1] = 1;
        matrix[2][2] = 1;
        matrix[3][3] = 1;
    }

    public Matrix4f clone() {
        return new Matrix4f(matrix);
    }

    public Matrix4f add(Matrix4f m) {
        for (int i = 0; i < 16; i++) {
            matrix[i / 4][i % 4] += m.getMatrix()[i / 4][i % 4];
        }
        return this;
    }

    public Matrix4f subtract(Matrix4f m) {
        return add(m.multiply(-1));
    }

    public Matrix4f multiply(float d) {
        for (int i = 0; i < 16; i++) {
            matrix[i / 4][i % 4] *= d;
        }
        return this;
    }

    public Matrix4f multiply(Matrix4f m) {
        float[][] result = new float[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                result[i][j] = matrix[i][0] * m.matrix[0][j] + matrix[i][1] * m.matrix[1][j] + matrix[i][2] * m.matrix[2][j] + matrix[i][3] * m.matrix[3][j];
            }
        }
        matrix = result;
        return this;
    }

    public Matrix4f inverse() {
        float[] inv = new float[16];
        float[] m = flatten();
        float det;
        int i;

        inv[0]  =  m[5] * m[10] * m[15] - m[5] * m[11] * m[14] - m[9] * m[6] * m[15] + m[9] * m[7] * m[14] + m[13] * m[6] * m[11] - m[13] * m[7] * m[10];
        inv[4]  = -m[4] * m[10] * m[15] + m[4] * m[11] * m[14] + m[8] * m[6] * m[15] - m[8] * m[7] * m[14] - m[12] * m[6] * m[11] + m[12] * m[7] * m[10];
        inv[8]  =  m[4] * m[9]  * m[15] - m[4] * m[11] * m[13] - m[8] * m[5] * m[15] + m[8] * m[7] * m[13] + m[12] * m[5] * m[11] - m[12] * m[7] * m[9];
        inv[12] = -m[4] * m[9]  * m[14] + m[4] * m[10] * m[13] + m[8] * m[5] * m[14] - m[8] * m[6] * m[13] - m[12] * m[5] * m[10] + m[12] * m[6] * m[9];
        inv[1]  = -m[1] * m[10] * m[15] + m[1] * m[11] * m[14] + m[9] * m[2] * m[15] - m[9] * m[3] * m[14] - m[13] * m[2] * m[11] + m[13] * m[3] * m[10];
        inv[5]  =  m[0] * m[10] * m[15] - m[0] * m[11] * m[14] - m[8] * m[2] * m[15] + m[8] * m[3] * m[14] + m[12] * m[2] * m[11] - m[12] * m[3] * m[10];
        inv[9]  = -m[0] * m[9]  * m[15] + m[0] * m[11] * m[13] + m[8] * m[1] * m[15] - m[8] * m[3] * m[13] - m[12] * m[1] * m[11] + m[12] * m[3] * m[9];
        inv[13] =  m[0] * m[9]  * m[14] - m[0] * m[10] * m[13] - m[8] * m[1] * m[14] + m[8] * m[2] * m[13] + m[12] * m[1] * m[10] - m[12] * m[2] * m[9];
        inv[2]  =  m[1] * m[6]  * m[15] - m[1] * m[7]  * m[14] - m[5] * m[2] * m[15] + m[5] * m[3] * m[14] + m[13] * m[2] * m[7]  - m[13] * m[3] * m[6];
        inv[6]  = -m[0] * m[6]  * m[15] + m[0] * m[7]  * m[14] + m[4] * m[2] * m[15] - m[4] * m[3] * m[14] - m[12] * m[2] * m[7]  + m[12] * m[3] * m[6];
        inv[10] =  m[0] * m[5]  * m[15] - m[0] * m[7]  * m[13] - m[4] * m[1] * m[15] + m[4] * m[3] * m[13] + m[12] * m[1] * m[7]  - m[12] * m[3] * m[5];
        inv[14] = -m[0] * m[5]  * m[14] + m[0] * m[6]  * m[13] + m[4] * m[1] * m[14] - m[4] * m[2] * m[13] - m[12] * m[1] * m[6]  + m[12] * m[2] * m[5];
        inv[3]  = -m[1] * m[6]  * m[11] + m[1] * m[7]  * m[10] + m[5] * m[2] * m[11] - m[5] * m[3] * m[10] - m[9]  * m[2] * m[7]  + m[9]  * m[3] * m[6];
        inv[7]  =  m[0] * m[6]  * m[11] - m[0] * m[7]  * m[10] - m[4] * m[2] * m[11] + m[4] * m[3] * m[10] + m[8]  * m[2] * m[7]  - m[8]  * m[3] * m[6];
        inv[11] = -m[0] * m[5]  * m[11] + m[0] * m[7]  * m[9]  + m[4] * m[1] * m[11] - m[4] * m[3] * m[9]  - m[8]  * m[1] * m[7]  + m[8]  * m[3] * m[5];
        inv[15] =  m[0] * m[5]  * m[10] - m[0] * m[6]  * m[9]  - m[4] * m[1] * m[10] + m[4] * m[2] * m[9]  + m[8]  * m[1] * m[6]  - m[8]  * m[2] * m[5];

        det = m[0] * inv[0] + m[1] * inv[4] + m[2] * inv[8] + m[3] * inv[12];

        if (det == 0) {
            throw new ArithmeticException("Matrix determinant was 0!");
        }

        det = 1.0f / det;

        for (i = 0; i < 16; i++) {
            inv[i] *= det;
        }

        matrix = new Matrix4f(inv).getMatrix();
        return this;
    }

    public static Matrix4f perspectiveMatrix() {
        return null;
    }

    public String toString() {
        String r = "";
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                r += matrix[i][j] + ", ";
            }
            r += "\b\b\n";
        }
        return r;
    }

    public float[][] getMatrix() {
        return matrix;
    }

    public void setMatrix(float[][] matrix) {
        this.matrix = matrix;
    }

    public float getValue(int id) {
        if (id > 15 || id < 0 ) {
            throw new IndexOutOfBoundsException(id + " is greater than or smaller than the limits of the matrix (0-15)");
        }
        return matrix[id / 4][id % 4];
    }

    public void setValue(int id, float value) {
        if (id > 15 || id < 0 ) {
            throw new IndexOutOfBoundsException(id + " is greater than or smaller than the limits of the matrix (0-15)");
        }
        matrix[id / 4][id % 4] = value;
    }

    public float getValue(int x, int y) {
        if (x > 3 || x < 0 ) {
            throw new IndexOutOfBoundsException(x + " is greater than or smaller than the limits of the matrix (0-3)");
        }
        if (y > 3 || y < 0 ) {
            throw new IndexOutOfBoundsException(y + " is greater than or smaller than the limits of the matrix (0-3)");
        }
        return matrix[x][y];
    }

    public void setValue(int x, int y, float value) {
        if (x > 3 || x < 0 ) {
            throw new IndexOutOfBoundsException(x + " is greater than or smaller than the limits of the matrix (0-3)");
        }
        if (y > 3 || y < 0 ) {
            throw new IndexOutOfBoundsException(y + " is greater than or smaller than the limits of the matrix (0-3)");
        }
        matrix[x][y] = value;
    }

    public float[] flatten() {
        float[] flat = new float[16];
        for (int i = 0; i < 16; i++) {
            flat[i] = matrix[i / 4][i % 4];
        }
        return flat;
    }

    public Matrix4f(float[] flat) {
        for (int i = 0; i < 16; i++) {
            matrix[i / 4][i % 4] = flat[i];
        }
    }

}
