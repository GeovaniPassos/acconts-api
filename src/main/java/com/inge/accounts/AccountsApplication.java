package com.inge.accounts;

import com.inge.accounts.entity.Category;
import com.inge.accounts.enums.TransactionType;
import com.inge.accounts.services.CategoryService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AccountsApplication {

	public static void main(String[] args) {
		SpringApplication.run(AccountsApplication.class, args);
	}

    CategoryService categoryService;

}
