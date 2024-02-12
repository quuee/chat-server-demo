package com.demo.im.web.controller;

import com.demo.im.common.Result;
import com.demo.im.common.enums.IMTerminalType;
import com.demo.im.util.JwtClaimsSetUtil;
import com.demo.im.web.model.UserDO;
import com.demo.im.web.params.LoginParam;
import com.demo.im.web.service.UserService;
import com.nimbusds.jose.JOSEObjectType;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jwt.SignedJWT;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping("generate")
public class TokenController {


    @Autowired
    @Qualifier("RsaSigner")
    private JWSSigner rsaSigner;

    @Autowired
    private JwtClaimsSetUtil jwtClaimsSetUtil;

    @Autowired
    private UserService userService;

    @PostMapping("login")
    @SneakyThrows
    public Result<UserDO> generateRSAToken(@RequestBody LoginParam loginParam) {

        UserDO user = userService.findUserByAccountPasswd(loginParam.getAccount(), loginParam.getPassword());
        if(user == null){
            return Result.error("can not found user");
        }

        SignedJWT signedJWT = new SignedJWT(new JWSHeader.Builder(JWSAlgorithm.RS256).type(JOSEObjectType.JWT).build(), jwtClaimsSetUtil.buildJWTClaimsSet(user.getUserId().toString(), IMTerminalType.APP.code()));
        signedJWT.sign(rsaSigner);
        String result = signedJWT.serialize();
        log.info("token is: {}" , result);
        user.setToken(result);
        return Result.ok(user);
    }
}
