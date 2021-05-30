package parser;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
public @interface Comment {

	String make() default "";
	String ret() default "";
	String[] exampleInput() default {};
	String exampleOutput() default "";
	int i_exampleOutput() default -1;
	String contain() default "";
}
