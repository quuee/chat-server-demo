package cn.quuee.chatServer.config;

import cn.quuee.chatServer.util.PEMKeyUtil;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.RSAKey;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtSignerAndVerifierConfig {

    @SneakyThrows // SneakyThrows作用：消除要强制抛出异常的声明
    @Bean(name = "RsaSigner")
    public JWSSigner jwsSigner(){
        // 读取私钥内容

        String pemEncodedRSAPrivateKey = PEMKeyUtil.readKeyAsString("rsa/private-key.pem");
        RSAKey rsaKey = (RSAKey) JWK.parseFromPEMEncodedObjects(pemEncodedRSAPrivateKey);
        return new RSASSASigner(rsaKey.toRSAPrivateKey());
    }

    @SneakyThrows
    @Bean(name = "RsaVerifier")
    public JWSVerifier jwsVerifier() {
        // 读取公钥内容
        String pemEncodedRSAPublicKey = PEMKeyUtil.readKeyAsString("rsa/publish-key.pem");
        RSAKey rsaPublicKey = (RSAKey) JWK.parseFromPEMEncodedObjects(pemEncodedRSAPublicKey);
        return new RSASSAVerifier(rsaPublicKey);
    }

}
