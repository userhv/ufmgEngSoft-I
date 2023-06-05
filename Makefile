prod_1: 
	rm **/*.class
	cd "$(shell pwd)/src"  && javac Main.java
	cd "$(shell pwd)/src" && java Main PROD_1

prod_2: 
	rm **/*.class
	cd "$(shell pwd)/src"  && javac Main.java
	cd "$(shell pwd)/src" && java Main PROD_2

clean:
	rm **/*.class