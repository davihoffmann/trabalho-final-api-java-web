package br.edu.unidavi.trabalhofinalapi;

import org.springframework.beans.BeansException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 *
 * Created by Davi on 30/11/18.
 */
@SpringBootApplication
public class Application implements ApplicationContextAware {

	private static ApplicationContext ctx;

	public static <T> T getBean(Class<T> tClass) {
		return ctx.getBean(tClass);
	}

	public static <T> T getBean(String name, Class<T> tClass) {
		return ctx.getBean(name, tClass);
	}

	public static void main(String[] args) {
		ctx = SpringApplication.run(Application.class, args);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		ctx = applicationContext;
	}
}