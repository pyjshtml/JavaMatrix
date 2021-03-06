import java.util.ArrayList;

public class Matrix implements Cloneable{
    public static class MatrixDimension implements Cloneable{
        private int rows;
        private int cols;
        public MatrixDimension(int rows, int cols){
            this.rows = rows;
            this.cols = cols;
        }
        public int rows(){
            return rows;
        }
        public int cols(){
            return cols;
        }
        public boolean equals(MatrixDimension other){
            return this.rows == other.rows && this.cols == other.cols;
        }
        public MatrixDimension clone(){
            return new MatrixDimension(rows, cols);
        }
    }
    public static class MatrixDimensionMismatch extends Exception{
        public MatrixDimensionMismatch(){
            super();
        }
        public MatrixDimensionMismatch(String e){
            super(e);
        }
    }
    protected MatrixDimension size;
    private double[][] matrix;
    private Matrix(double[][] matrix, MatrixDimension size){
        this.matrix = matrix;
        this.size = size;
    }
    public Matrix(double[][] matrix){
        this(matrix, new MatrixDimension(matrix.length, matrix[0].length));
    }
    public Matrix(MatrixDimension size){ 
        this(Matrix.generateMatrix(size), size);
    }
    public Matrix(int rows, int cols){
        this(new MatrixDimension(rows, cols));
    }
    public static double[][] generateMatrix(MatrixDimension size){
        var matrix = new double[size.rows()][size.cols()];
        for(int i = 0; i < size.rows(); i++)
            for(int j = 0; j < size.cols(); matrix[i][j++] = 0D);
        return matrix;
    }
    public MatrixDimension size(){
        return this.size;
    }
    public double get(int row, int col){
        return this.matrix[row][col];
    }
    public void set(int row, int col, double value){
        this.matrix[row][col] = value;
    }
    public void set(double[][] values) throws MatrixDimensionMismatch{
        if(!this.size.equals(new MatrixDimension(values.length, values[0].length)))
            throw new MatrixDimensionMismatch();
        for(int r = 0; r < values.length; r++)
            for(int c = 0; c < values[r].length; c++)
                this.set(r, c, values[r][c]);
    }
    public static Matrix add(Matrix a, Matrix b) throws MatrixDimensionMismatch{
        return a.clone().add(b);
    }
    public Matrix add(Matrix other) throws MatrixDimensionMismatch{
        if(!this.size.equals(other.size))
            throw new MatrixDimensionMismatch();
        for(int r = 0; r < this.size.rows(); r++)
            for(int c = 0; c < this.size.cols(); c++)
                this.set(r, c, this.get(r,c) + other.get(r,c));
        return this;
    }
    public static Matrix add(Matrix matrix, double scalar) throws MatrixDimensionMismatch{
        return matrix.clone().add(scalar);
    }
    public Matrix add(double scalar){
        for(int r = 0; r < this.size.rows(); r++)
            for(int c = 0; c < this.size.cols(); c++)
                this.set(r, c, this.get(r,c) + scalar);
        return this;
    }
    public static Matrix multiply(Matrix a, Matrix b) throws MatrixDimensionMismatch{
        return a.clone().multiply(b);
    }
    public Matrix multiply(Matrix other) throws MatrixDimensionMismatch{
        if(this.size.cols() != other.size.rows())
            throw new MatrixDimensionMismatch();
        var output = new double[this.size.rows()][other.size.cols()];
        for(int y = 0; y < this.size.rows(); y++){
            for(int o = 0; o < other.size.cols(); o++){
                int sum = 0;
                for(int x = 0; x < this.size.cols(); sum += this.get(y,x) * other.get(x,o), x++);
                output[y][o] = sum;
            }
        }
        this.set(output);
        return this;
    }
    @Override
    public String toString(){
        var l = new ArrayList<ArrayList<Double>>();
        for(int r = 0; r < this.size.rows(); r++){
            var s = new ArrayList<Double>();
            for(int c = 0; c < this.size.cols(); c++)
                s.add(this.matrix[r][c]);
            l.add(s);
        }
        return l.toString();
    }
    @Override
    public Matrix clone(){
        var m = new Matrix(size);
        for(int i = 0; i < size.rows(); i++)
            for(int j = 0; j < size.cols(); j++)
                m.set(i, j, this.get(i,j));
        return m;
    }
    public static void main(String[] args) throws Exception{
        var a = new Matrix(2,2);
        var b = new Matrix(2,2);
        double[][] values = {{1,2},{3,4}};
        a.set(values);
        b.set(values);
        System.out.println(a.multiply(b));
        System.out.println(a.add(b));
        System.out.println(Matrix.add(a, 2));
        System.out.println(a.add(1));
    }
}
