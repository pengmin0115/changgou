public class Solution {

    public static void main(String[] args) {
        System.out.println(replaceSpace(new StringBuffer("We Are Happy.")));
    }

    public static String replaceSpace(StringBuffer str) {
    	StringBuffer sb = new StringBuffer();
        String sourceStr = str.toString();
        for(int i=0; i< sourceStr.length();i++){
            char ch = sourceStr.charAt(i);
            if(" ".equals(new String(new char[]{ch}))){
                sb.append("%20");
            }else{
                sb.append(ch);
            }
        }
        return sb.toString();
    }
}