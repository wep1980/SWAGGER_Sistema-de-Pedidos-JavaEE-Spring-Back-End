package br.com.waldirep.springionicmc.services.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * Classe de anotação customizada
 * código padrão do framework
 * 
 * Nome da anotação é @ClienteUpdate
 * 
 * @Constraint(validatedBy = ClienteUpdateValidator.class) => Classe que implementa o validator, onde esta as regras de negócio
 * @author Waldir
 *
 */
@Constraint(validatedBy = ClienteUpdateValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ClienteUpdate {
	
	String message() default "Erro de validação";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
	
}
