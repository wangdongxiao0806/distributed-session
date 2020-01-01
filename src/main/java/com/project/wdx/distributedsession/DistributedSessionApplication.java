package com.project.wdx.distributedsession;

import com.project.wdx.distributedsession.interceptor.SessionInterceptor;
import com.project.wdx.distributedsession.model.User;
import com.project.wdx.distributedsession.repository.DataRepository;
import com.project.wdx.distributedsession.utils.SerializerUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.UUID;

@SpringBootApplication
@EnableRedisRepositories
@RestController
public class DistributedSessionApplication {

    public static void main(String[] args) {
        SpringApplication.run(DistributedSessionApplication.class, args);
    }

    @Autowired
    private DataRepository dataRepository;

    @RequestMapping("/save")
    public User save(){
        User user = new User();
        user.setName("wdx");
        user.setAge("18");
        String key = UUID.randomUUID().toString();
        dataRepository.save(key,user);

        return dataRepository.queryBySessionId(key,User.class);
    }

    @RequestMapping("/login")
    public String login(){

        return "login";
    }

    @RequestMapping("/afterLogin")
    public User  afterLogin(){
        String sessionId = SessionInterceptor.threadLocal.get();
        return dataRepository.queryBySessionId(sessionId,User.class);
    }

    @Bean
    public RedisTemplate redisTemplate(RedisConnectionFactory factory){
        RedisTemplate redisTemplate = new StringRedisTemplate(factory);
        return redisTemplate;
    }

    @Configuration
    public class InterceptorConfig implements WebMvcConfigurer {
        @Autowired
        private SessionInterceptor sessionInterceptor;
        @Override
        public void addInterceptors(InterceptorRegistry registry) {
            registry.addInterceptor(sessionInterceptor);
        }
        @Bean
        public SessionInterceptor sessionInterceptor(){
            SessionInterceptor sessionInterceptor = new SessionInterceptor(){

                @Override
                protected Object queryUser(String loginName) {
                    User user = new User();
                    user.setName(UUID.randomUUID().toString());
                    user.setAge(user.getName().length()+"");
                    return user;
                }
            };
            sessionInterceptor.addUri("/login");
            return sessionInterceptor;
        }
    }

}

