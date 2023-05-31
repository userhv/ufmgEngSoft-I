CLASSES_BASE=Candidate.java Cargos.java CertifiedProfessional.java Controller.java HashMapCandidate.java Main.java Model.java Partido.java StopTrap.java TSEEmployee.java TSEProfessional.java View.java Voter.java Warning.java

CLASSES_PROD_1=FederalDeputy.java President.java Senador.java DeputadoEstadual.java Governador.java

CLASSES_PROD_2=Prefeito.java Vereador.java


# prod_1:
# 	cd "$(shell pwd)/src"  && javac Main.java

# full:
# 	make build
# 	make run

prod_1: 
	cd "$(shell pwd)/src"  && javac Main.java
	cd "$(shell pwd)/src" && java Main PROD_1

prod_2: 
	cd "$(shell pwd)/src"  && javac Main.java
	cd "$(shell pwd)/src" && java Main PROD_2

# run: 
# 	cd "$(shell pwd)/src" && java Main PROD_1

clean:
	rm **/*.class