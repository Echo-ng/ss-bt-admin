package com.echostack.project.infra.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.echostack.project.model.entity.SysRole;
import com.echostack.project.model.entity.SysUser;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: Echo
 * @Date: 2019/3/23 17:16
 * @Description:
 */
@Component
@Slf4j
@Data
public class JwtTokenUtil {
    private String secret; //秘钥

    @Value("${jwt.issuer}")
    private String issuer;

    @Value("${jwt.expired}")
    private Long expired;

    @Value("${jwt.header.key}")
    private String headerKey;

    @Value("${jwt.token.head}")
    private String tokenHead;

    @Value("${app.login.online}")
    private int online;

    @Autowired
    RedisTemplate redisTemplate;

    public JwtTokenUtil(){
//        this.secret = UUID.randomUUID().toString();
        this.secret = "travel";
    }


//    public SecretKey generalKey(){
//        String stringKey = this.secret;//本地配置文件中加密的密文7786df7fc3a34e26a61c034d5ec8245d
//        byte[] encodedKey = Base64Utils.decodeFromString(stringKey);//本地的密码解码[B@152f6e2
//        System.out.println(encodedKey);//
//        SecretKey key = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");// 根据给定的字节数组使用AES加密算法构造一个密钥，使用 encodedKey中的始于且包含 0 到前 leng 个字节这是当然是所有。（后面的文章中马上回推出讲解Java加密和解密的一些算法）
//        return key;
//    }

    public Map<String, Claim> parseToken(String token) {
        DecodedJWT jwt = JWT.decode(token);
        return jwt.getClaims();
    }

    public String createToken(SysUser sysUser) throws JWTCreationException{
        String token;
        //失效日期
        Date now = new Date();
        Date expiredDate = new Date(now.getTime() + expired);
        //设置秘钥
        Algorithm algorithm = Algorithm.HMAC256(this.secret);
        //获取角色清单
        List<String> roleList = new ArrayList<>();
        if(sysUser.getSysRoles() != null && sysUser.getSysRoles().size() > 0){
            roleList = sysUser.getSysRoles().parallelStream()
                    .map(SysRole::getName)
                    .collect(Collectors.toList());
        }
        String[] roles = new String[roleList.size()];
        roleList.toArray(roles);
        //生成token
        token =JWT.create()
                .withIssuer(this.issuer)
                .withClaim("userId", sysUser.getId())
                .withClaim("username", sysUser.getUsername())
                .withClaim("email", sysUser.getEmail())
                .withClaim("mobile", sysUser.getMobile())
                .withArrayClaim("sysRoles",roles)
                .withIssuedAt(now)
                .withExpiresAt(expiredDate)
                .sign(algorithm);
        return token;
    }

    public void validate(String token) {

        //设置秘钥
        Algorithm algorithm = Algorithm.HMAC256(this.secret);
        DecodedJWT jwt = JWT.require(algorithm)
                            .acceptLeeway(1)
                            .build().verify(token);
        //从缓存判断是否存在
//            redisTemplate.hasKey()
    }

    public void delToken(String authToken,String username) {
        Deque<Serializable> deque = this.listOnline(username);
        if(!deque.isEmpty()){
            deque.remove(authToken);
        }
    }

    public void delTokenDq(String username){
        redisTemplate.delete(username);
    }

    public Deque<Serializable> listOnline(String username){
        Deque<Serializable> deque = (Deque<Serializable>) redisTemplate.opsForValue().get(username);
        if(deque == null) {
            deque = new LinkedList<Serializable>();
            redisTemplate.opsForValue().set(username,deque);
        }
        return deque;
    }

    public void putToken(String token,String username){
        Deque<Serializable> deque = this.listOnline(username);
        //未登录
        if(deque.isEmpty()){
            deque.push(token);
        }else{
            //不包含当前token才存入
            if(!deque.contains(token)){
                //当前超过在线设备数限制，挤出第一个设备
                if(deque.size() >= online){
                    deque.removeFirst();
                    deque.push(token);
                }else{
                    deque.push(token);
                }
            }
        }
        redisTemplate.opsForValue().set(username,deque);
    }
}
