full:
	make build
	make run

build: 
	cd "%cd%/src"  && javac Main.java

run: 
	cd "%cd%/src" && java Main

clean:
	cd "%cd%/src" && del *.class