package com.endsoul.fully_security;

import com.fasterxml.jackson.databind.Module;
import io.vavr.jackson.datatype.VavrModule;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class FullySecurityApplication {

  public static void main(String[] args) {
    SpringApplication.run(FullySecurityApplication.class, args);
  }

  // @RestController 에서 HTTP 요청과 응답에 Vavr 자료형을 사용했을 때 정상적으로 converting 해준다.
  @Bean
  Module vavrModule() {
    return new VavrModule();
  }
}
