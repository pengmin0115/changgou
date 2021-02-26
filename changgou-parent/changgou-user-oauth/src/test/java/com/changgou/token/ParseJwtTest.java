package com.changgou.token;

import org.junit.Test;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;

/*****
 * @Author: www.itheima
 * @Date: 2019/7/7 13:48
 * @Description: com.changgou.token
 *  使用公钥解密令牌数据
 ****/
public class ParseJwtTest {

    /***
     * 校验令牌
     */
    @Test
    public void testParseToken(){
        //令牌
        String token = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJyb2xlcyI6IlJPTEVfVklQLFJPTEVfVVNFUiIsIm5hbWUiOiJpdGhlaW1hIiwiaWQiOiIxIn0.ILswrYPNOkw6dIrQOKRLne7Sb-ObFo7vqK7HCl3zjbETdnGYBDahdMJ-qPQTstrUVKP6XzAlyg06Wi_GpITi3WIdQXWHa447qSeYlbUddQn0cwTX-2Hg2SfmyAZbObup9Bkm7jz4-g4yOLOG6GzjOXs-lv7ehPc2hcT0jQUBEaqjdD3wqisXr0aS0qonDJsKJ7XSMr2Jgp4MIGczbLzLKEG2QLlUD_Qvyrsu9G2aDJrSgzNl3mwBqeFl5lfc80ZkLP1SOjXc7R4sOTA5yHafKZty3aoKTXL__MoRonw9Em2jBYdg15REzeddxCfnfI-9LNih_Ehhf6_wPifOWwG-LA";

        //公钥
        String publicKey = "-----BEGIN PUBLIC KEY-----MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEArKrb/hJiF6jqiFVF0zi7vgHik7HZ8H0b5al54ltcu51h8LgM0ZClc/K8uHHWyMMRayatJ3njWZ0jG4lgXZ+P91Qu4aZpvUKiCNXntLIQJsVn3nDo8t+eLhDmF/iqY1aJdRnt4qu51MaUsnzjif7ITYtVBXLLGQtx8zhsY6/tNVHXh5ym81ANKl5OH1bVq2uESaGWGXPw9bPy7OBmsclIs6xSlP0mTyCISzm4+Z7uF6sSsuZorpKVEa1lq/9BrXk107JoirqsyPF5k7eehi4nLGg8dLS2J7BYPjj7mbIi4dHbwRqQLIyodUKR6PWA3u0ksSb7VXjgkgWid8+5P0DB2wIDAQAB-----END PUBLIC KEY-----";

        //校验Jwt: JwtHelper.decodeAndVerify(token,new RsaVerifier(publickey))
        Jwt jwt = JwtHelper.decodeAndVerify(token, new RsaVerifier(publicKey));

        //获取Jwt原始内容
        String claims = jwt.getClaims();
        System.out.println(claims);
        //jwt令牌
        String encoded = jwt.getEncoded();
        System.out.println(encoded);
    }
}
