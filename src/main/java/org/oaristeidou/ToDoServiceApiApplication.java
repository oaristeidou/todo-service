package org.oaristeidou;

import java.util.List;
import org.oaristeidou.exception.ToDoAPIHandlerExceptionResolver;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class ToDoServiceApiApplication implements WebMvcConfigurer{

  public static void main(String[] args) {
    SpringApplication.run(ToDoServiceApiApplication.class, args);
  }

  /**
   * Implement a custom HandlerExceptionResolver for always showing the
   * best http status according to the user inputs
   * @param resolvers
   */
  @Override
  public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> resolvers) {
    resolvers.add(0, new ToDoAPIHandlerExceptionResolver());
  }
}
