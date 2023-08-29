package vn.com.ids.myachef.dao;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import vn.com.ids.myachef.dao.repository.extended.GenericRepositoryImpl;

@EnableJpaAuditing
@SpringBootApplication
@EnableJpaRepositories(repositoryBaseClass = GenericRepositoryImpl.class)
public class DaoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DaoApplication.class);
    }
}
