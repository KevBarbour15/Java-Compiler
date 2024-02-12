clean:
	@echo "*** Deleting all class files...\n"
	rm -rf target
	find . -name "*.class" -type f -delete
	@echo "\n*** Done cleaning.\n"

compile: clean
	@echo "*** Compiling...\n"
	javac  compiler/Compiler.java
	@echo "\n*** Done compiling.\n"

run: compile
	@echo "*** Running...\n"
	java compiler.Compiler
	@echo "\n*** Done running.\n"