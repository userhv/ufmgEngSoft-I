import java.util.HashMap;
import java.util.Scanner;
import java.util.Map;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.File;
import static java.lang.System.exit;


class View {
	private static View INSTANCE;
	private View() {}
	
	public static View getInstance() {
		if (INSTANCE == null)
			INSTANCE = new View();
		return INSTANCE;
	}
	
	//printar mensagens diversas
    public void print(String s) {
		System.out.println(s);
    }
	
	public void printStartMenu() {
		System.out.println("Escolha uma opção:\n");
        System.out.println("(1) Entrar (Eleitor)");
        System.out.println("(2) Entrar (TSE)");
        System.out.println("(0) Fechar aplicação");
	}
	
	public void printSeparator() {
		System.out.println("\n=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=\n\n");
	}
	
	public void printReadError() {
		System.out.println("\nErro na leitura de entrada, digite novamente");
	}
	
	public void printInvalidCommand() {
		System.out.println("Comando invalido");
	}
	
	public void printUnexpectedError() {
		System.out.println("Erro inesperado");
	}
	
	public void printVoterNotFound() {
		System.out.println("Eleitor não encontrado, por favor confirme se a entrada está correta e tente novamente");
	}
	
	public void printInvalidInput() {
		System.out.println("Entrada invalida, tente novamente");
	}
	
	public void printConfirmationPrompt() {
		System.out.println("(1) Sim\n(2) Não");
	}
	
	public void printMenuRedirection() {
		System.out.println("Ok, você será redirecionado para o menu inicial");
	}

	public void printBlankVote() {
		System.out.println("Você está votando branco");
	}
	
	public void printNullVote() {
		System.out.println("Você está votando nulo");
	}

	public void printCandidateNotFound() {
		System.out.println("Nenhum candidato encontrado com este número, tente novamente");
	}
	
	public void printPartidoNotFound() {
		System.out.println("Nenhum partido encontrado com esta sigla, tente novamente");
	}
	
	public void printInvalidPassword() {
		System.out.println("Senha inválida, tente novamente");
	}

	public void printVoteConfirmationPrompt() {
		System.out.println("(1) Confirmar\n(2) Mudar voto");
	}
	
	public void printModelNotStarted() {
		System.out.println("A eleição ainda não foi inicializada, verifique com um funcionário do TSE");
	}
	
	public void printTSEEmployeeNotFound() {
		System.out.println("Funcionário do TSE não encontrado, por favor confirme se a entrada está correta e tente novamente");
	}

	public void printWrongNumber() {
		System.out.println("O número do candidato precisa ter 2 dígitos");
	}	
	
	public void printWrongDeputyNumber() {
		System.out.println("O número do candidato precisa ter 5 dígitos");
	}

	public void printWrongSenadorNumber() {
		System.out.println("O número do candidato precisa ter 2 dígitos");
	}

	public void printWrongDeputadoEstadualNumber() {
		System.out.println("O número do candidato precisa ter 4 dígitos");
	}

	public void printWrongTituloEleitor() {
		System.out.println("O titulo de eleitor precisa ter 12 dígitos");
	}

	public void printCandidateNotRecorded() {
		System.out.println("Candidato não foi cadastrado");
	}
	
	public void printPartidoNotRecorded() {
		System.out.println("Partido não foi cadastrado");
	}
	
	public void printCandidateRemoved() {
		System.out.println("Candidato removido com sucesso");
	}
	
	public void printCandidateNotRemoved() {
		System.out.println("O candidato não foi removido.");
	}

	public void printPartidoRemoved() {
		System.out.println("Partido removido com sucesso");
	}

	public void printPartidoNotRemoved() {
		System.out.println("O partido não foi removido.");
	}
	
	public void printTSEMenuInicial() {
		System.out.println("Escolha uma opção:");
		System.out.println("(1) Cadastros");
		System.out.println("(2) Remoções");
		System.out.println("(0) Sair");
	}

	public void printTSEMenuCadastro() {
		System.out.println("Escolha uma opção:");
		System.out.println("(1) Cadastrar candidato");
		System.out.println("(2) Cadastrar partido");
		System.out.println("(3) Cadastrar eleitor");
		System.out.println("(0) Sair");
	}
	public void printTSEMenuRemocao() {
		System.out.println("Escolha uma opção:");
		System.out.println("(1) Remover candidato");
		System.out.println("(2) Remover partido");
		System.out.println("(0) Sair");
	}
	
	public void printTSEMenu2() {
		System.out.println("(1) Iniciar sessão");
		System.out.println("(2) Finalizar sessão");
		System.out.println("(3) Mostrar resultados");
		System.out.println("(0) Sair");
	}
	
	public void printExit() {
		System.out.println("(ext) Desistir");
		
	}
	
	public void printStartMessage() {
		System.out.println("Vamos começar!\n");
	}
	
	public void printSuccessfulVote() {
		System.out.println("Voto registrado com sucesso");
	}
	
	public void printOfficeNotFound() {
		System.out.println("Não foi encontrado o cargo a ser disputado.");
	}

	public void printCannotAddCandidate() {
		System.out.println("Você não pode adicionar candidatos, pois a eleição já começou.");
	}

	public void printStartedSession() {
		System.out.println("Sessão inicializada");
	}

	public void printTerminatedSession() {
		System.out.println("Sessão finalizada com sucesso");
	}
	

	public void printRemovePresidentialCandidate(String name, int number, String party) {
		System.out.println("Remover o candidato a presidente " + name + " Nº " + number + " do " + party
        + "?");
	}

	public void printRemoveCandidate(String cargo,String name, int number, String party, String state) {
		System.out.println("Remover o candidato a " + cargo  + name + " Nº " + number + " do "
			+ party + "(" + state + ")?");
	}

	public void printRemovePartido(String name, String sigla) {
		System.out.println("Remover o partido " + sigla + ": " + name + "?");
	}

	
	public void printDataError() {
		System.out.println("Erro na inicialização dos dados");
	} 
	//--------------------------------
	
	//perguntas ao usuário
	public void askVoterNumber() {
		System.out.println("Insira seu título de eleitor");
	}
	
	public void askVoterInfo(String name, String state) {
		System.out.println("Olá, você é " + name + " de " + state + "?");
	}
	
	public void askCandidato(String type) {

		switch (type) {
			case "President": 
				System.out.println("Digite o número do candidato escolhido por você para presidente");
				break;
			case "Senador": 
				System.out.println("Digite o número do candidato escolhido por você para senador");
				break;
			case "Governador": 
				System.out.println("Digite o número do candidato escolhido por você para governador");
				break;
			case "FederalDeputy": 
				System.out.println("Digite o número do candidato escolhido por você para deputador federal");
				break;
			case "DeputadoEstadual": 
				System.out.println("Digite o número do candidato escolhido por você para deputado estadual");
				break;
			case "Prefeito": 
				System.out.println("Digite o número do candidato escolhido por você para prefeito");
				break;
			case "Verea": 
				System.out.println("Digite o número do candidato escolhido por você para vereador");
				break;
			default: 
				System.out.println("Digite o número do candidato escolhido por você");
				break;
		}

	}


	public void askStateDeputyCandidateNumber(int counter) {
		System.out.println("Digite o número do " + counter + "º candidato escolhido por você para deputado estadual:\n");
	}
	
	public void askPassword() {
		System.out.println("Insira sua senha");
	}
	
	public void askCandidateType_prod_1() {
		System.out.println("");
		System.out.println("Qual o cargo do candidato?\n");
		System.out.println("(1) Presidente");
		System.out.println("(2) Senador");
		System.out.println("(3) Governador");
		System.out.println("(4) Deputado Federal");
		System.out.println("(5) Deputado Estadual");
		System.out.println("(0) Voltar ao menu anterior");
	}

	public void askCandidateType_prod_2() {
		System.out.println("");
		System.out.println("Qual o cargo do candidato?\n");
		System.out.println("(1) Prefeito");
		System.out.println("(2) Vereador");
		System.out.println("(0) Voltar ao menu anterior");
	}
	
	public void askPartidoNome() {
		System.out.println("Qual o nome do partido?");
	}

	public void askPartidoSigla() {
		System.out.println("Qual a sigla do partido?");
	}
	
	public void askCandidateNumber() {
		System.out.println("Qual o numero do candidato");
	}

	public void askUser() {
		System.out.println("Insira seu usuário:");
	}
	
	public void askBallotPassword() {
		System.out.println("Insira a senha da urna");
	}

	public void askCandidateName() {
		System.out.println("Qual o nome do candidato?");
	}
	
	public void askCandidateParty() {
		System.out.println("Qual o partido do candidato?");
	}
	
	public void askCandidateState() {
		System.out.println("Qual o estado do candidato?");
	}

	public void askTwoNumber() {
		System.out.println("Qual o numero do candidato? (Digite um número de 2 digitos)");
	}

	public void askFourNumber() {
		System.out.println("Qual o numero do candidato? (Digite um número de 4 digitos)");
	}
	
	public void askFiveNumber() {
		System.out.println("Qual o numero do candidato? (Digite um número de 5 digitos)");
	}

	public void askDeputyCandidateNumber() {
		System.out.println("Digite o número do candidato escolhido por você para deputado federal:\n");
	}

	public void askDeputadoEstadualCandidateNumber() {
		System.out.println("Digite o número do candidato escolhido por você para deputado estadual:\n");
	}

	public void askVereadorCandidateNumber() {
		System.out.println("Digite o número do candidato escolhido por você para vereador:\n");
	}
	public void askPresidentInfo(String name, int number, String party) {
		System.out.println("\nCadastrar o candidato a presidente " + name + " Nº " + number + " do " + party + "?");
	}
	
	public void askCandidateInfo(String cargo, String name, int number, String party, String state) {
		System.out.println("\nCadastrar o candidato a " + cargo + ": " + name + " Nº " + number + " do " + party + "(" + state + ")?");
	}

	public void askPartidoInfo(String name, String sigla) {
		System.out.println("\nCadastrar o partido " + sigla + ": " + name + "?");
	}

	public void askEleitorTitulo() {
		System.out.println("Digite seu titulo de eleitor");
	}

	public void askEleitorNome() {
		System.out.println("Digite seu nome");
	}
	
	public void askEleitorEstado() {
		System.out.println("Digite seu estado");
	}

	public void eleitorNaoCadastrado() {
		System.out.println("Esse titulo de eleitor já foi cadastrado.");
	}

	public void eleitorCadastrado() {
		System.out.println("Eleitor cadastrado.");
	}
	//--------------------------------
}
