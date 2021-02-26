/**
 * @author pengmin
 * @date 2020/12/9 21:52
 */

public class CatchTest {
    public static void main(String[] args){
        int num = get();
        System.out.println(num);
    }

    public static int get(){
        int a;
        try {
             a = 4;
            return a;
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            a = 5;
        }
        return a ;
    }
}
