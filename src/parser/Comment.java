package parser;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention( RetentionPolicy.RUNTIME )
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER, ElementType.CONSTRUCTOR})
public @interface Comment {

	boolean includeTest() default false;
	int testId() default -1;
	String make() default "";
	String ret() default "";
	String[] exampleInput() default {};
	String exampleOutput() default "";
	int i_exampleOutput() default -1;
	String contain() default "";
}
