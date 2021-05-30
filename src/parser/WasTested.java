package parser;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
public @interface WasTested {
	// Methods have this Annotation if they where tested.
}