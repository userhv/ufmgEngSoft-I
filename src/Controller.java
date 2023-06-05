import java.util.HashMap;
import java.util.Scanner;
import java.util.Map;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.File;
import static java.lang.System.exit;

public class Controller {
  private static final BufferedReader scanner = new BufferedReader(new InputStreamReader(System.in));

  private static boolean exit = false;

  private static final Map<String, TSEProfessional> TSEMap = new HashMap<>();

  private static final Map<String, Voter> VoterMap = new HashMap<>();

  // visão e modelo
  private static View view;
  private static Model model;

  public Controller(View view) {
    this.view = view;
  }

  private static String readString() {
    try {
      return scanner.readLine();
    } catch (Exception e) {
      view.printReadError();
      return readString();
      // return "";
    }
  }

  private static int readInt() {
    try {
      return Integer.parseInt(readString());
    } catch (Exception e) {
      view.printReadError();
      return readInt();
      // return -1;
    }
  }

  private static void startMenu() {
    try {
      while (!exit) {

        view.printStartMenu();
        int command = readInt();
        switch (command) {
          case 1 -> voterMenu();
          case 2 -> tseMenu();
          case 0 -> exit = true;
          default -> view.printInvalidCommand();
        }
        view.printSeparator();
      }
    } catch (Exception e) {
      view.printUnexpectedError();
    }
  }

  private static Voter getVoter() {
    view.askVoterNumber();
    String electoralCard = readString();
    Voter voter = VoterMap.get(electoralCard);
    if (voter == null) {
      view.printVoterNotFound();
    } else {
      view.askVoterInfo(voter.name, voter.state);
      view.printConfirmationPrompt();
      int command = readInt();
      if (command == 1)
        return voter;
      else if (command == 2)
        view.printMenuRedirection();
      else {
        view.printInvalidInput();
        return getVoter();
      }
    }
    return null;
  }

  private static boolean voteMajorityElection(Voter voter, String type) {

    view.printExit();
    view.askCandidato(type);
    String vote = readString();
    if (vote.equals("ext"))
      throw new StopTrap("Saindo da votação");
    // Branco
    else if (vote.equals("br")) {
      view.printBlankVote();
      view.printVoteConfirmationPrompt();
      int confirm = readInt();
      if (confirm == 1) {
        voter.vote(0, model, type, true);
        return true;
      } else {
        return voteMajorityElection(voter, type);
      }
    } else {
      try {
        int voteNumber = Integer.parseInt(vote);
        // Nulo
        if (voteNumber == 0) {
          view.printNullVote();
          view.printVoteConfirmationPrompt();
          int confirm = readInt();
          if (confirm == 1) {
            voter.vote(0, model, type, false);
            return true;
          } else {
            return voteMajorityElection(voter, type);
          }
        }
        // Normal
        Candidate candidate = model.getCandidateByNumber(voter.state, voteNumber, type);

        if (candidate == null) {
          view.printCandidateNotFound();
          view.printSeparator();
          return voteMajorityElection(voter, type);
        }
        view.print(candidate.name + " do " + candidate.party + "\n");

        view.printVoteConfirmationPrompt();
        int confirm = readInt();
        if (confirm == 1) {
          voter.vote(voteNumber, model, type, false);
          return true;
        } else if (confirm == 2) {
          return voteMajorityElection(voter, type);
        } else {
          view.printInvalidCommand();
          return voteMajorityElection(voter, type);
        }
      } catch (Warning e) {
        view.print(e.getMessage());
        return voteMajorityElection(voter, type);
      } catch (Error e) {
        view.print(e.getMessage());
        throw e;
      } catch (Exception e) {
        view.printUnexpectedError();
        return false;
      }
    }
  }

  private static boolean voteFederalDeputy(Voter voter) {
    view.printExit();
    view.askDeputyCandidateNumber();
    String vote = readString();
    if (vote.equals("ext"))
      throw new StopTrap("Saindo da votação");
    // Branco
    if (vote.equals("br")) {
      view.printBlankVote();
      view.printVoteConfirmationPrompt();
      int confirm = readInt();
      if (confirm == 1) {
        voter.vote(0, model, "FederalDeputy", true);
        return true;
      } else
        return voteFederalDeputy(voter);
    } else {
      try {
        int voteNumber = Integer.parseInt(vote);
        // Nulo
        if (voteNumber == 0) {
          view.printNullVote();
          view.printVoteConfirmationPrompt();
          int confirm = readInt();
          if (confirm == 1) {
            voter.vote(0, model, "FederalDeputy", false);
            return true;
          } else
            return voteFederalDeputy(voter);
        }

        // Normal
        FederalDeputy candidate = model.getFederalDeputyByNumber(voter.state, voteNumber);
        if (candidate == null) {
          view.printCandidateNotFound();
          view.printSeparator();
          return voteFederalDeputy(voter);
        }
        view.print(candidate.name + " do " + candidate.party + "(" + candidate.state + ")\n");
        view.printVoteConfirmationPrompt();
        int confirm = readInt();
        if (confirm == 1) {
          voter.vote(voteNumber, model, "FederalDeputy", false);
          return true;
        } else if (confirm == 2)
          return voteFederalDeputy(voter);
      } catch (Warning e) {
        view.print(e.getMessage());
        return voteFederalDeputy(voter);
      } catch (Error e) {
        view.print(e.getMessage());
        throw e;
      } catch (Exception e) {
        view.printUnexpectedError();
        return false;
      }
    }
    return true;
  }

  private static boolean voteDeputadoEstadual(Voter voter) {
    view.printExit();
    view.askDeputadoEstadualCandidateNumber();
    String vote = readString();
    if (vote.equals("ext"))
      throw new StopTrap("Saindo da votação");
    // Branco
    if (vote.equals("br")) {
      view.printBlankVote();
      view.printVoteConfirmationPrompt();
      int confirm = readInt();
      if (confirm == 1) {
        voter.vote(0, model, "DeputadoEstadual", true);
        return true;
      } else
        return voteDeputadoEstadual(voter);
    } else {
      try {
        int voteNumber = Integer.parseInt(vote);
        // Nulo
        if (voteNumber == 0) {
          view.printNullVote();
          view.printVoteConfirmationPrompt();
          int confirm = readInt();
          if (confirm == 1) {
            voter.vote(0, model, "DeputadoEstadual", false);
            return true;
          } else
            return voteDeputadoEstadual(voter);
        }

        // Normal
        DeputadoEstadual candidate = model.getDeputadoEstadualByNumber(voter.state, voteNumber);
        if (candidate == null) {
          view.printCandidateNotFound();
          view.printSeparator();
          return voteDeputadoEstadual(voter);
        }
        view.print(candidate.name + " do " + candidate.party + "(" + candidate.state + ")\n");
        view.printVoteConfirmationPrompt();
        int confirm = readInt();
        if (confirm == 1) {
          voter.vote(voteNumber, model, "DeputadoEstadual", false);
          return true;
        } else if (confirm == 2)
          return voteDeputadoEstadual(voter);
      } catch (Warning e) {
        view.print(e.getMessage());
        return voteDeputadoEstadual(voter);
      } catch (Error e) {
        view.print(e.getMessage());
        throw e;
      } catch (Exception e) {
        view.printUnexpectedError();
        return false;
      }
    }
    return true;
  }

  private static boolean voteVereador(Voter voter) {
    view.printExit();
    view.askVereadorCandidateNumber();
    String vote = readString();
    if (vote.equals("ext"))
      throw new StopTrap("Saindo da votação");
    // Branco
    if (vote.equals("br")) {
      view.printBlankVote();
      view.printVoteConfirmationPrompt();
      int confirm = readInt();
      if (confirm == 1) {
        voter.vote(0, model, "Vereador", true);
        return true;
      } else
        return voteVereador(voter);
    } else {
      try {
        int voteNumber = Integer.parseInt(vote);
        // Nulo
        if (voteNumber == 0) {
          view.printNullVote();
          view.printVoteConfirmationPrompt();
          int confirm = readInt();
          if (confirm == 1) {
            voter.vote(0, model, "Vereador", false);
            return true;
          } else
            return voteVereador(voter);
        }

        // Normal
        Vereador candidate = model.getVereadorByNumber(voter.state, voteNumber);
        if (candidate == null) {
          view.printCandidateNotFound();
          view.printSeparator();
          return voteVereador(voter);
        }
        view.print(candidate.name + " do " + candidate.party + "(" + candidate.state + ")\n");
        view.printVoteConfirmationPrompt();
        int confirm = readInt();
        if (confirm == 1) {
          voter.vote(voteNumber, model, "Vereador", false);
          return true;
        } else if (confirm == 2)
          return voteVereador(voter);
      } catch (Warning e) {
        view.print(e.getMessage());
        return voteVereador(voter);
      } catch (Error e) {
        view.print(e.getMessage());
        throw e;
      } catch (Exception e) {
        view.printUnexpectedError();
        return false;
      }
    }
    return true;

  }

  private static void voterMenu() {
    try {
      view.printSeparator();
      if (!model.getStatus()) {
        view.printModelNotStarted();
        return;
      }

      Voter voter = getVoter();
      if (voter == null)
        return;
      view.printSeparator();

      view.printStartMessage();

      view.print(
          "OBS:\n");

      if (Produtos.BRANCO) {
        view.print(
            "Caso você queira votar branco você deve escrever br\n");
      }

      if (Produtos.NULO) {
        view.print(
            "Caso você queira votar nulo, você deve usar um numero composto de 0 (00 e 0000)\n");
      }
      view.printSeparator();

      if (Produtos.PRODUTO_1) {
        // President | Governador | Senador | FederalDeputy | DeputadoEstadual

        if (voteMajorityElection(voter, "President"))
          view.printSuccessfulVote();
        view.printSeparator();

        if (voteMajorityElection(voter, "Senador"))
          view.printSuccessfulVote();
        view.printSeparator();

        if (voteMajorityElection(voter, "Governador"))
          view.printSuccessfulVote();
        view.printSeparator();

        if (voteFederalDeputy(voter))
          view.printSuccessfulVote();
        view.printSeparator();

        if (voteDeputadoEstadual(voter))
          view.printSuccessfulVote();
        view.printSeparator();

      } else if (Produtos.PRODUTO_2) {

        if (voteMajorityElection(voter, "Prefeito")) {
          view.printSuccessfulVote();
          view.printSeparator();
        }
        if (voteVereador(voter)) {
          view.printSuccessfulVote();
          view.printSeparator();
        }
      }

    } catch (Warning e) {
      view.print(e.getMessage());
    } catch (StopTrap e) {
      view.print(e.getMessage());
    } catch (Exception e) {
      view.printUnexpectedError();
    }
  }

  private static TSEProfessional getTSEProfessional() {
    view.askUser();
    String user = readString();
    TSEProfessional tseProfessional = TSEMap.get(user);
    if (tseProfessional == null) {
      view.printTSEEmployeeNotFound();
    } else {
      view.askPassword();
      String password = readString();
      // Deveria ser um hash na pratica
      if (tseProfessional.password.equals(password))
        return tseProfessional;
      view.printInvalidPassword();
      view.printSeparator();
    }
    return null;
  }

  private static boolean validateNumberCandidate(int tipoCandidato, int numeroCandidato) {
    String validaNumero = Integer.toString(numeroCandidato);
    if (tipoCandidato == 1) {
      if (validaNumero.length() == 2)
        return true;
      else
        return false;
    } else if (tipoCandidato == 2) {
      if (validaNumero.length() == 5)
        return true;
      else
        return false;
    } else if (tipoCandidato == 3) {
      if (validaNumero.length() == 4)
        return true;
      else
        return false;
    } else {
      view.printOfficeNotFound();
      return false;
    }
  }

  private static void addPresident(TSEEmployee tseProfessional) {
    view.askCandidateName();
    String name = readString();

    view.askCandidateParty();
    String party = readString();

    view.askCandidateNumber();
    boolean exit = true;
    int number = 0;
    while (exit) {
      number = readInt();
      if (validateNumberCandidate(1, number)) {
        exit = false;
      } else {
        view.printWrongNumber();
      }
    }
    Candidate candidate = new President.Builder()
        .name(name)
        .number(number)
        .party(party)
        .build();

    view.askPresidentInfo(candidate.name, candidate.number, candidate.party);
    insertCandidateTSE(tseProfessional, candidate);
  }

  private static void addSenador(TSEEmployee tseProfessional) {
    view.askCandidateName();
    String name = readString();

    view.askCandidateParty();
    String party = readString();

    view.askCandidateState();
    String state = readString();

    view.askTwoNumber();

    boolean exit = true;
    int number = 0;
    while (exit) {
      number = readInt();
      if (validateNumberCandidate(1, number)) {
        exit = false;
      } else {
        view.printWrongSenadorNumber();
      }
    }
    Candidate candidate = new Senador.Builder()
        .name(name)
        .number(number)
        .party(party)
        .state(state)
        .build();

    view.askCandidateInfo("senador", name, number, party, state);
    insertCandidateTSE(tseProfessional, candidate);
  }

  private static void addFederalDeputy(TSEEmployee tseProfessional) {
    view.askCandidateName();
    String name = readString();

    view.askCandidateParty();
    String party = readString();

    view.askCandidateState();
    String state = readString();
    view.askFiveNumber();

    boolean exit = true;
    int number = 0;
    while (exit) {
      number = readInt();
      if (validateNumberCandidate(2, number)) {
        exit = false;
      } else {
        view.printWrongDeputyNumber();
      }
    }
    Candidate candidate = new FederalDeputy.Builder()
        .name(name)
        .number(number)
        .party(party)
        .state(state)
        .build();

    view.askCandidateInfo("deputado federal", name, number, party, state);
    insertCandidateTSE(tseProfessional, candidate);
  }

  private static void addGovernador(TSEEmployee tseProfessional) {
    view.askCandidateName();
    String name = readString();

    view.askCandidateParty();
    String party = readString();

    view.askCandidateState();
    String state = readString();
    view.askTwoNumber();

    boolean exit = true;
    int number = 0;
    while (exit) {
      number = readInt();
      if (validateNumberCandidate(1, number)) {
        exit = false;
      } else {
        view.printWrongDeputyNumber();
      }
    }
    Candidate candidate = new Governador.Builder()
        .name(name)
        .number(number)
        .party(party)
        .state(state)
        .build();

    view.askCandidateInfo("governador", name, number, party, state);
    insertCandidateTSE(tseProfessional, candidate);
  }

  private static void addDeputadoEstadual(TSEEmployee tseProfessional) {
    view.askCandidateName();
    String name = readString();

    view.askCandidateParty();
    String party = readString();

    view.askCandidateState();
    String state = readString();
    view.askFourNumber();

    boolean exit = true;
    int number = 0;
    while (exit) {
      number = readInt();
      if (validateNumberCandidate(3, number)) {
        exit = false;
      } else {
        view.printWrongDeputadoEstadualNumber();
      }
    }
    Candidate candidate = new DeputadoEstadual.Builder()
        .name(name)
        .number(number)
        .party(party)
        .state(state)
        .build();

    view.askCandidateInfo("deputado estadual", name, number, party, state);
    insertCandidateTSE(tseProfessional, candidate);
  }

  private static void addPrefeito(TSEEmployee tseProfessional) {
    view.askCandidateName();
    String name = readString();

    view.askCandidateParty();
    String party = readString();

    view.askCandidateState();
    String state = readString();

    view.askTwoNumber();

    boolean exit = true;
    int number = 0;
    while (exit) {
      number = readInt();
      if (validateNumberCandidate(1, number)) {
        exit = false;
      } else {
        view.printWrongSenadorNumber();
      }
    }
    Candidate candidate = new Prefeito.Builder()
        .name(name)
        .number(number)
        .party(party)
        .state(state)
        .build();

    view.askCandidateInfo("prefeito", name, number, party, state);
    insertCandidateTSE(tseProfessional, candidate);
  }

  private static void addVereador(TSEEmployee tseProfessional) {
    view.askCandidateName();
    String name = readString();

    view.askCandidateParty();
    String party = readString();

    view.askCandidateState();
    String state = readString();

    view.askFourNumber();

    boolean exit = true;
    int number = 0;
    while (exit) {
      number = readInt();
      if (validateNumberCandidate(3, number)) {
        exit = false;
      } else {
        view.printWrongDeputadoEstadualNumber();
      }
    }
    Candidate candidate = new Vereador.Builder()
        .name(name)
        .number(number)
        .party(party)
        .state(state)
        .build();

    view.askCandidateInfo("vereador", name, number, party, state);
    insertCandidateTSE(tseProfessional, candidate);
  }

  private static void insertCandidateTSE(TSEEmployee tseProfessional, Candidate candidate) {
    view.printConfirmationPrompt();
    int save = readInt();
    if (save == 1) {
      view.askBallotPassword();
      String pwd = readString();
      if (Produtos.PRODUTO_1) {
        tseProfessional.addCandidate(candidate, model, pwd);
      } else if (Produtos.PRODUTO_2) {
        tseProfessional.addCandidate_prod_2(candidate, model, pwd);
      }
    } else if (save == 2) {
      view.printCandidateNotRecorded();
    }
  }

  private static void insertPartidoTSE(TSEEmployee tseProfessional, Partido partido) {
    view.printConfirmationPrompt();
    int save = readInt();
    if (save == 1) {
      view.askBallotPassword();
      String pwd = readString();
      tseProfessional.addPartido(partido, model, pwd);
    } else if (save == 2) {
      view.printPartidoNotRecorded();
    }
  }

  private static void addPartido(TSEEmployee tseProfessional) {
    view.askPartidoNome();
    String nome = readString();

    view.askPartidoSigla();
    String sigla = readString();

    Partido partido = new Partido.Builder()
        .nome(nome)
        .sigla(sigla)
        .build();

    view.askPartidoInfo(partido.nome, partido.sigla);
    insertPartidoTSE(tseProfessional, partido);
  }

  private static void removePartidoTSE(TSEEmployee tseProfessional, Partido partido) {
    view.printConfirmationPrompt();
    int remove = readInt();
    if (remove == 1) {
      view.askBallotPassword();
      String pwd = readString();
      tseProfessional.removePartido(partido, model, pwd);
      view.printPartidoRemoved();
    } else {
      view.printPartidoNotRemoved();
    }
  }

  private static void removePartido(TSEEmployee tseProfessional) {
    view.askPartidoSigla();
    String sigla = readString();
    Partido partido = null;
    partido = model.getPartidoBySigla(sigla);
    if (partido == null) {
      view.printPartidoNotFound();
      removePartido(tseProfessional);
    } else {
      view.printRemovePartido(partido.nome, partido.sigla);
      removePartidoTSE(tseProfessional, partido);
    }
  }

  private static void addCandidate_prod_1(TSEEmployee tseProfessional) {
    boolean back = false;
    while (!back) {
      view.printSeparator();
      view.askCandidateType_prod_1();
      int command = readInt();
      switch (command) {
        case 1 -> {
          addPresident(tseProfessional);
          back = true;
        }
        case 2 -> {
          addSenador(tseProfessional);
          back = true;
        }
        case 3 -> {
          addGovernador(tseProfessional);
          back = true;
        }
        case 4 -> {
          addFederalDeputy(tseProfessional);
          back = true;
        }
        case 5 -> {
          addDeputadoEstadual(tseProfessional);
          back = true;
        }
        case 0 -> back = true;
        default -> view.printInvalidCommand();
      }
    }
  }

  private static void addCandidate_prod_2(TSEEmployee tseProfessional) {
    boolean back = false;
    while (!back) {
      view.printSeparator();
      view.askCandidateType_prod_2();
      int command = readInt();
      switch (command) {
        case 1 -> {
          addPrefeito(tseProfessional);
          back = true;
        }
        case 2 -> {
          addVereador(tseProfessional);
          back = true;
        }
        case 0 -> back = true;
        default -> view.printInvalidCommand();
      }
    }
  }

  private static void verifyCandidateForRemove_prod_1(TSEEmployee tseProfessional, String candidateType) {
    view.askCandidateNumber();
    int number = readInt();
    Candidate candidate = null;
    if (candidateType == HashMapCandidate.hashMapCandidate.get("presidente")) {
      candidate = model.getPresidentByNumber(number);
      if (candidate == null) {
        view.printCandidateNotFound();
        removeCandidate_prod_1(tseProfessional);
      } else {
        view.printRemovePresidentialCandidate(candidate.name, candidate.number, candidate.party);
        removeCandidateTSE(tseProfessional, candidate);
      }
    } else if (candidateType == HashMapCandidate.hashMapCandidate.get("senador")) {
      view.askCandidateState();
      String state = readString();
      candidate = model.getSenadorByNumber(state, number);
      if (candidate == null) {
        view.printCandidateNotFound();
        removeCandidate_prod_1(tseProfessional);
      } else {
        view.printRemoveCandidate("senador", candidate.name, candidate.number, candidate.party,
            ((Senador) candidate).state);
        removeCandidateTSE(tseProfessional, candidate);
      }
    } else if (candidateType == HashMapCandidate.hashMapCandidate.get("governador")) {
      view.askCandidateState();
      String state = readString();
      candidate = model.getGovernadorByNumber(state, number);
      if (candidate == null) {
        view.printCandidateNotFound();
        removeCandidate_prod_1(tseProfessional);
      } else {
        view.printRemoveCandidate(candidateType, candidate.name, candidate.number, candidate.party,
            ((Governador) candidate).state);
        removeCandidateTSE(tseProfessional, candidate);
      }
    } else if (candidateType == HashMapCandidate.hashMapCandidate.get("df")) {
      view.askCandidateState();
      String state = readString();
      candidate = model.getFederalDeputyByNumber(state, number);
      if (candidate == null) {
        view.printCandidateNotFound();
        removeCandidate_prod_1(tseProfessional);
      } else {
        view.printRemoveCandidate(candidateType, candidate.name, candidate.number, candidate.party,
            ((FederalDeputy) candidate).state);
        removeCandidateTSE(tseProfessional, candidate);
      }
    } else if (candidateType == HashMapCandidate.hashMapCandidate.get("de")) {
      view.askCandidateState();
      String state = readString();
      candidate = model.getDeputadoEstadualByNumber(state, number);
      if (candidate == null) {
        view.printCandidateNotFound();
        removeCandidate_prod_1(tseProfessional);
      } else {
        view.printRemoveCandidate(candidateType, candidate.name, candidate.number, candidate.party,
            ((DeputadoEstadual) candidate).state);
        removeCandidateTSE(tseProfessional, candidate);
      }
    }
  }

  private static void verifyCandidateForRemove_prod_2(TSEEmployee tseProfessional, String candidateType) {
    view.askCandidateNumber();
    int number = readInt();
    Candidate candidate = null;
    if (candidateType == HashMapCandidate.hashMapCandidate.get("prefeito")) {
      view.askCandidateState();
      String state = readString();
      candidate = model.getPrefeitoByNumber(state, number);
      if (candidate == null) {
        view.printCandidateNotFound();
        removeCandidate_prod_1(tseProfessional);
      } else {
        view.printRemoveCandidate(candidateType, candidate.name, candidate.number, candidate.party,
            ((Prefeito) candidate).state);
        removeCandidateTSE(tseProfessional, candidate);
      }
    } else if (candidateType == HashMapCandidate.hashMapCandidate.get("vereador")) {
      view.askCandidateState();
      String state = readString();
      candidate = model.getVereadorByNumber(state, number);
      if (candidate == null) {
        view.printCandidateNotFound();
        removeCandidate_prod_1(tseProfessional);
      } else {
        view.printRemoveCandidate(candidateType, candidate.name, candidate.number, candidate.party,
            ((Vereador) candidate).state);
        removeCandidateTSE(tseProfessional, candidate);
      }
    }
  }

  private static void removeCandidateTSE(TSEEmployee tseProfessional, Candidate candidate) {
    view.printConfirmationPrompt();
    int remove = readInt();
    if (remove == 1) {
      view.askBallotPassword();
      String pwd = readString();
      if (Produtos.PRODUTO_1) {
        tseProfessional.removeCandidate_prod_1(candidate, model, pwd);
      } else if (Produtos.PRODUTO_2) {
        tseProfessional.removeCandidate_prod_2(candidate, model, pwd);
      }
      view.printCandidateRemoved();
    } else {
      view.printCandidateNotRemoved();
    }
  }

  private static void removeCandidate_prod_1(TSEEmployee tseProfessional) {
    boolean back = false;
    while (!back) {
      view.printSeparator();
      view.askCandidateType_prod_1();
      int command = readInt();

      switch (command) {
        case 1 -> {
          verifyCandidateForRemove_prod_1(tseProfessional, HashMapCandidate.hashMapCandidate.get("presidente"));
          back = true;
        }
        case 2 -> {
          verifyCandidateForRemove_prod_1(tseProfessional, HashMapCandidate.hashMapCandidate.get("governador"));
          back = true;
        }
        case 3 -> {
          verifyCandidateForRemove_prod_1(tseProfessional, HashMapCandidate.hashMapCandidate.get("senador"));
          back = true;
        }
        case 4 -> {
          verifyCandidateForRemove_prod_1(tseProfessional, HashMapCandidate.hashMapCandidate.get("df"));
          back = true;
        }
        case 5 -> {
          verifyCandidateForRemove_prod_1(tseProfessional, HashMapCandidate.hashMapCandidate.get("de"));
          back = true;
        }
        case 0 -> back = true;
        default -> view.printInvalidCommand();
      }
    }
  }

  private static void removeCandidate_prod_2(TSEEmployee tseProfessional) {
    boolean back = false;
    while (!back) {
      view.printSeparator();
      view.askCandidateType_prod_2();
      int command = readInt();

      switch (command) {
        case 1 -> {
          verifyCandidateForRemove_prod_2(tseProfessional, HashMapCandidate.hashMapCandidate.get("prefeito"));
          back = true;
        }
        case 2 -> {
          verifyCandidateForRemove_prod_2(tseProfessional, HashMapCandidate.hashMapCandidate.get("vereador"));
          back = true;
        }
        case 0 -> back = true;
        default -> view.printInvalidCommand();
      }
    }
  }
  // #endif

  private static void startSession(CertifiedProfessional tseProfessional) {
    try {
      view.askBallotPassword();
      String pwd = readString();
      tseProfessional.startSession(model, pwd);
      view.printStartedSession();
      view.printSeparator();
    } catch (Warning e) {
      view.print(e.getMessage());
    }
  }

  private static void endSession(CertifiedProfessional tseProfessional) {
    try {
      view.askBallotPassword();
      String pwd = readString();
      tseProfessional.endSession(model, pwd);
      view.printTerminatedSession();
      view.printSeparator();
    } catch (Warning e) {
      view.print(e.getMessage());
    }
  }

  private static void showResults(CertifiedProfessional tseProfessional) {
    try {
      view.askBallotPassword();
      String pwd = readString();
      view.print(tseProfessional.getFinalResult(model, pwd));
      view.printSeparator();
    } catch (Warning e) {
      view.print(e.getMessage());
    }
  }

  private static void addEleitor(TSEEmployee tseProfessional) {
    view.askEleitorTitulo();
    String titulo = "";
    boolean exit = true;
    while (exit) {
      titulo = readString();
      if (titulo.length() == 12) {
        exit = false;
      } else {
        view.printWrongTituloEleitor();
      }
    }
    view.askEleitorNome();
    String nome = readString();
    view.askEleitorEstado();
    String estado = readString();
    if (VoterMap.get(titulo) != null) {
      view.eleitorNaoCadastrado();
      tseMenuCadastro((TSEEmployee) tseProfessional);
    }

    VoterMap.put(titulo, new Voter.Builder().electoralCard(titulo).name(nome).state(estado).build());
    view.eleitorCadastrado();
  }

  private static void tseMenuCadastro(TSEEmployee tseProfessional) {
    boolean back = false;
    while (!back) {
      view.printTSEMenuCadastro();
      int command = readInt();
      switch (command) {
        case 0 -> back = true;
        case 1 -> {
          if (Produtos.PRODUTO_1) {
            addCandidate_prod_1((TSEEmployee) tseProfessional);
          } else if (Produtos.PRODUTO_2) {
            addCandidate_prod_2((TSEEmployee) tseProfessional);
          }
        }
        case 2 -> addPartido((TSEEmployee) tseProfessional);
        case 3 -> addEleitor((TSEEmployee) tseProfessional);
        default -> view.printInvalidCommand();
      }
    }
  }

  private static void tseMenuRemocao(TSEEmployee tseProfessional) {
    boolean back = false;
    while (!back) {
      view.printTSEMenuRemocao();
      int command = readInt();
      switch (command) {
        case 0 -> back = true;
        case 1 -> {
          if (Produtos.PRODUTO_1) {
            removeCandidate_prod_1((TSEEmployee) tseProfessional);
          } else if (Produtos.PRODUTO_2) {
            removeCandidate_prod_2((TSEEmployee) tseProfessional);
          }
        }
        case 2 -> removePartido((TSEEmployee) tseProfessional);
        default -> view.printInvalidCommand();
      }
    }
  }

  private static void tseMenu() {
    try {
      TSEProfessional tseProfessional = getTSEProfessional();
      if (tseProfessional == null)
        return;
      view.printSeparator();
      boolean back = false;
      while (!back) {
        if (tseProfessional instanceof TSEEmployee) {
          if (model.getStatus()) {
            back = true;
            view.printCannotAddCandidate();
          } else {
            view.printTSEMenuInicial();
            int command = readInt();
            switch (command) {
              case 0 -> back = true;
              case 1 -> tseMenuCadastro((TSEEmployee) tseProfessional);
              case 2 -> tseMenuRemocao((TSEEmployee) tseProfessional);
              default -> view.printInvalidCommand();
            }
          }
        } else if (tseProfessional instanceof CertifiedProfessional) {
          view.printTSEMenu2();
          int command = readInt();
          switch (command) {
            case 1 -> startSession((CertifiedProfessional) tseProfessional);
            case 2 -> endSession((CertifiedProfessional) tseProfessional);
            case 3 -> showResults((CertifiedProfessional) tseProfessional);
            case 0 -> back = true;
            default -> view.printInvalidCommand();
          }
        }
      }
    } catch (Warning e) {
      view.print(e.getMessage());
    } catch (Exception e) {
      view.printUnexpectedError();
    }
  }

  private static void loadVoters() {
    try {
      File myObj = new File("voterLoad.txt");
      Scanner myReader = new Scanner(myObj);
      while (myReader.hasNextLine()) {
        String data = myReader.nextLine();
        var voterData = data.split(",");
        VoterMap.put(voterData[0],
            new Voter.Builder().electoralCard(voterData[0]).name(voterData[1]).state(voterData[2]).build());
      }
      myReader.close();
    } catch (Exception e) {
      view.printDataError();
      exit(1);
    }
  }

  private static void loadCandidates_prod_1(String ModelPassword) {
    President presidentCandidate1 = new President.Builder().name("João").number(12).party("PDS1").build();
    model.addPresidentCandidate(presidentCandidate1, ModelPassword);
    President presidentCandidate2 = new President.Builder().name("Maria").number(14).party("ED").build();
    model.addPresidentCandidate(presidentCandidate2, ModelPassword);

    Senador senador1 = new Senador.Builder().name("Hugo").number(12).party("PDS1").state("MG").build();
    model.addSenadorCandidate(senador1, ModelPassword);
    Senador senador2 = new Senador.Builder().name("Carolina").number(14).party("ED").state("MG").build();
    model.addSenadorCandidate(senador2, ModelPassword);

    Governador gov1 = new Governador.Builder().name("Kalil").number(12).party("PDS1").state("MG").build();
    model.addGovernadorCandidate(gov1, ModelPassword);
    Governador gov2 = new Governador.Builder().name("Neres").number(14).party("ED").state("MG").build();
    model.addGovernadorCandidate(gov2, ModelPassword);

    FederalDeputy federalDeputyCandidate1 = new FederalDeputy.Builder().name("Carlos").number(12345).party("PDS1")
        .state("MG").build();
    model.addFederalDeputyCandidate(federalDeputyCandidate1, ModelPassword);
    FederalDeputy federalDeputyCandidate2 = new FederalDeputy.Builder().name("Cleber").number(54321).party("PDS2")
        .state("MG").build();
    model.addFederalDeputyCandidate(federalDeputyCandidate2, ModelPassword);
    FederalDeputy federalDeputyCandidate3 = new FederalDeputy.Builder().name("Sofia").number(11211).party("IHC")
        .state("MG").build();
    model.addFederalDeputyCandidate(federalDeputyCandidate3, ModelPassword);

    DeputadoEstadual de1 = new DeputadoEstadual.Builder().name("Carlos Junior").number(1234).party("PDS1")
        .state("MG").build();
    model.addDeputadoEstadualCandidate(de1, ModelPassword);
    DeputadoEstadual de2 = new DeputadoEstadual.Builder().name("Marcos").number(5432).party("PDS2")
        .state("MG").build();
    model.addDeputadoEstadualCandidate(de2, ModelPassword);
    DeputadoEstadual de3 = new DeputadoEstadual.Builder().name("Solange").number(1121).party("IHC")
        .state("MG").build();
    model.addDeputadoEstadualCandidate(de3, ModelPassword);
  }

  private static void loadCandidates_prod_2(String ModelPassword) {
    Prefeito prefeito1 = new Prefeito.Builder().name("Fuad").number(12).party("PDS1").state("MG").build();
    model.addPrefeitoCandidate(prefeito1, ModelPassword);
    Prefeito prefeito2 = new Prefeito.Builder().name("Marina").number(14).party("ED").state("MG").build();
    model.addPrefeitoCandidate(prefeito2, ModelPassword);

    Vereador vereador1 = new Vereador.Builder().name("Marciel").number(1234).party("PDS1").state("MG").build();
    model.addVereadorCandidate(vereador1, ModelPassword);
    Vereador vereador2 = new Vereador.Builder().name("Carolina").number(4321).party("ED").state("MG").build();
    model.addVereadorCandidate(vereador2, ModelPassword);
  }

  private static void loadTSEProfessionals() {
    TSEMap.put("cert", new CertifiedProfessional.Builder()
        .user("cert")
        .password("54321")
        .build());
    TSEMap.put("emp", new TSEEmployee.Builder()
        .user("emp")
        .password("12345")
        .build());
  }

  public static void startSession() {

    // Startup the current Model instance
    String ModelPassword = "password";

    model = new Model.Builder()
        .password(ModelPassword)
        .build();

    Partido partido1 = new Partido.Builder().nome("Pratica e Dev 1").sigla("PDS1").build();
    model.addPartido(partido1, ModelPassword);
    Partido partido2 = new Partido.Builder().nome("Pratica e Dev2").sigla("PDS2").build();
    model.addPartido(partido2, ModelPassword);
    Partido partido3 = new Partido.Builder().nome("Estrutura de Dados").sigla("ED").build();
    model.addPartido(partido3, ModelPassword);
    Partido partido4 = new Partido.Builder().nome("Interação Humano Computador").sigla("IHC").build();
    model.addPartido(partido4, ModelPassword);

    if (Produtos.PRODUTO_1) {
      loadCandidates_prod_1(ModelPassword);
    } else if (Produtos.PRODUTO_2) {
      loadCandidates_prod_2(ModelPassword);
    }

    // Startar todo os eleitores e profissionais do TSE
    loadVoters();
    loadTSEProfessionals();

    startMenu();
  }
}
