import java.rmi.Naming;  

public class ClienteRMI {

    static int[][] separa_matriz(int[][] A,int inicio,int N)
    {
      int[][] M = new int[N/2][N];
      for (int i = 0; i < N/2; i++)
        for (int j = 0; j < N; j++)
          M[i][j] = A[i + inicio][j];
      return M;
    }
    
    static void acomoda_matriz(int[][] C,int[][] A,int renglon,int columna,int N)
    {
      for (int i = 0; i < N/2; i++)
        for (int j = 0; j < N/2; j++)
          C[i + renglon][j + columna] = A[i][j];
    }
    
    
    static void imprime_matriz(int[][] A) {
      for (int i = 0; i < A.length; i++) {
        System.out.println();
        for (int j = 0; j < A[0].length; j++) {
          System.out.print(" "+A[i][j]);
        }
      }
    }

    public static void main(String[] args) throws Exception{
        String url = "rmi://localhost/prueba";

        // obtiene una referencia que "apunta" al objeto remoto asociado a la URL
        InterfaceRMI r = (InterfaceRMI)Naming.lookup(url);
        int N=Integer.parseInt(args[0]);
        int[][] A = new int[N][N];
        int[][] B = new int[N][N];
        int[][] C = new int[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < args.length; j++) {
                A[i][j] = 2 * i - j;
                B[i][j] = i + 2 * j;
            }
        }
        int[][] A1 = separa_matriz(A,0,N);
        int[][] A2 = separa_matriz(A,N/2,N);
        int[][] B1 = separa_matriz(B,0,N);
        int[][] B2 = separa_matriz(B,N/2,N);
        int[][] C1 = r.multiplica_matrices(A1,B1,N);
        int[][] C2 = r.multiplica_matrices(A1,B2,N);
        int[][] C3 = r.multiplica_matrices(A2,B1,N);
        int[][] C4 = r.multiplica_matrices(A2,B2,N);            
        acomoda_matriz(C,C1,0,0,N);
        acomoda_matriz(C,C2,0,N/2,N);
        acomoda_matriz(C,C3,N/2,0,N);
        acomoda_matriz(C,C4,N/2,N/2,N);
        imprime_matriz(C);        

    }
}
