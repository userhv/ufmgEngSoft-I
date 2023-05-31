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
  
  //visão e modelo
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

  private static boolean votePresident(Voter voter) {
    view.printExit();
    view.askPresident();
    String vote = readString();
    if (vote.equals("ext"))
      throw new StopTrap("Saindo da votação");
    // Branco
    else if (vote.equals("br")) {
      view.printBlankVote();
      view.printVoteConfirmationPrompt();
      int confirm = readInt();
      if (confirm == 1) {
        voter.vote(0, model, "President", true);
        return true;
      } else
        votePresident(voter);
    } else {
      try {
        int voteNumber = Integer.parseInt(vote);
        // Nulo
        if (voteNumber == 0) {
          view.printNullVote();
          view.printVoteConfirmationPrompt();
          int confirm = readInt();
          if (confirm == 1) {
            voter.vote(0, model, "President", false);
            return true;
          } else
            votePresident(voter);
        }

        // Normal
        President candidate = model.getPresidentByNumber(voteNumber);
        if (candidate == null) {
          view.printCandidateNotFound();
          view.printSeparator();
          return votePresident(voter);
        }
        view.print(candidate.name + " do " + candidate.party + "\n");
        view.printVoteConfirmationPrompt();
        int confirm = readInt();
        if (confirm == 1) {
          voter.vote(voteNumber, model, "President", false);
          return true;
        } else if (confirm == 2)
          return votePresident(voter);
      } catch (Warning e) {
        view.print(e.getMessage());
        return votePresident(voter);
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

  private static boolean voteFederalDeputy(Voter voter, int counter) {
    view.printExit();
    view.askDeputyCandidateNumber(counter);
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
        return voteFederalDeputy(voter, counter);
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
            return voteFederalDeputy(voter, counter);
        }

        // Normal
        FederalDeputy candidate = model.getFederalDeputyByNumber(voter.state, voteNumber);
        if (candidate == null) {
          view.printCandidateNotFound();
          view.printSeparator();
          return voteFederalDeputy(voter, counter);
        }
        view.print(candidate.name + " do " + candidate.party + "(" + candidate.state + ")\n");
        view.printVoteConfirmationPrompt();
        int confirm = readInt();
        if (confirm == 1) {
          voter.vote(voteNumber, model, "FederalDeputy", false);
          return true;
        } else if (confirm == 2)
          return voteFederalDeputy(voter, counter);
      } catch (Warning e) {
        view.print(e.getMessage());
        return voteFederalDeputy(voter, counter);
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
      view.print("OBS:\n- Caso você queira votar nulo, você deve usar um numero composto de 0 (00 e 0000)\n- caso você queira votar branco você deve escrever br\n");
      view.printSeparator();

      if (votePresident(voter))
        view.printSuccessfulVote();
      view.printSeparator();

      if (voteFederalDeputy(voter, 1))
        view.printSuccessfulVote();
      view.printSeparator();

      if (voteFederalDeputy(voter, 2))
        view.printSuccessfulVote();
      view.printSeparator();

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

  private static boolean validateNumberCandidate(int tipoCandidato, int numeroCandidato){
    String validaNumero = Integer.toString(numeroCandidato);
    if(tipoCandidato == 1){
      if(validaNumero.length() == 2)
        return true;
      else
        return false;
    }else if(tipoCandidato == 2){
      if(validaNumero.length() == 5)
        return true;
      else
        return false;
    }else{
      view.printOfficeNotFound();
      return false;
    }
  }

  private static void addPresident(TSEEmployee tseProfessional){
    view.askCandidateName();
    String name = readString();
    
	view.askCandidateParty();
    String party = readString();
    
	view.askCandidateNumber();
    boolean exit = true;
    int number = 0;
    while (exit) {
      number = readInt();
      if(validateNumberCandidate(1,number)){
        exit = false;
      }else{
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

  private static void addFederalDeputy(TSEEmployee tseProfessional){
    view.askCandidateName();
    String name = readString();
    
	view.askCandidateParty();
    String party = readString();
    
	view.askCandidateState();
    String state = readString();
    view.askDeputyCandidateNumber();
    
	boolean exit = true;
    int number = 0;
    while (exit) {
      number = readInt();
      if(validateNumberCandidate(2,number)){
              exit = false;
      }else{
        view.printWrongDeputyNumber();
      }
    }
    Candidate candidate = new FederalDeputy.Builder()
    .name(name)
    .number(number)
    .party(party)
    .state(state)
    .build();

    view.askFederalDeputyInfo(name, number, party, state);
    insertCandidateTSE(tseProfessional, candidate);
  }

  private static void insertCandidateTSE(TSEEmployee tseProfessional, Candidate candidate){
    view.printConfirmationPrompt();
    int save = readInt();
    if (save == 1) {
      view.askBallotPassword();
      String pwd = readString();
      tseProfessional.addCandidate(candidate, model, pwd);
    }else if(save == 2){
      view.printCandidateNotRecorded();
    }
  }

  private static void addCandidate(TSEEmployee tseProfessional) {
    boolean back = false;
    while(!back){
      view.printSeparator();
	  view.askCandidateType();
      int command = readInt();
      switch (command) {
        case 1 -> {
          addPresident(tseProfessional);
          back = true;
        }
        case 2 -> {
          addFederalDeputy(tseProfessional);
          back = true;
        }
        case 0 -> back = true;
        default -> view.printInvalidCommand();
      }
    }
  }

  private static void verifyCandidateForRemove(TSEEmployee tseProfessional, String candidateType){
    view.askCandidateNumber();
    int number = readInt();
    Candidate candidate = null;
    if(candidateType == HashMapCandidate.hashMapCandidate.get("presidente")){
      candidate = model.getPresidentByNumber(number);
      if (candidate == null) {
        view.printCandidateNotFound();
        removeCandidate(tseProfessional);
      }else{
        view.printRemovePresidentialCandidate(candidate.name, candidate.number, candidate.party);
        removeCandidateTSE(tseProfessional, candidate);
      }
    }else if(candidateType == HashMapCandidate.hashMapCandidate.get("df")){
      view.askCandidateState();
      String state = readString();
      candidate = model.getFederalDeputyByNumber(state, number);
      if (candidate == null) {
        view.printCandidateNotFound();
        removeCandidate(tseProfessional);
      }else{
		view.printRemoveFederalDeputyCandidate(candidate.name, candidate.number, candidate.party, ((FederalDeputy) candidate).state);
        removeCandidateTSE(tseProfessional, candidate);
      }
    }
  }

  private static void removeCandidateTSE(TSEEmployee tseProfessional, Candidate candidate){
    view.printConfirmationPrompt();
    int remove = readInt();
    if (remove == 1) {
      view.askBallotPassword();
      String pwd = readString();
      tseProfessional.removeCandidate(candidate, model, pwd);
      view.printCandidateRemoved();
    }else{
      view.printCandidateNotRemoved();
    }
  }

  private static void removeCandidate(TSEEmployee tseProfessional) {
    boolean back = false;
    while(!back){
      view.printSeparator();
	  view.askCandidateType();
      int command = readInt();

      switch (command) {
        case 1 -> {
          verifyCandidateForRemove(tseProfessional, HashMapCandidate.hashMapCandidate.get("presidente"));
          back = true;
        }
        case 2 -> {
          verifyCandidateForRemove(tseProfessional, HashMapCandidate.hashMapCandidate.get("df"));
          back = true;
        }
        case 0 -> back = true;
        default -> view.printInvalidCommand();
      }
    }
  }

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

  private static void tseMenu() {
    try {
      TSEProfessional tseProfessional = getTSEProfessional();
      if (tseProfessional == null)
        return;
      view.printSeparator();
      boolean back = false;
      while (!back) {
        if (tseProfessional instanceof TSEEmployee) {
          if(model.getStatus()){
            back = true;
            view.printCannotAddCandidate();
          }
          else{
			view.printTSEMenu1();
            int command = readInt();
            switch (command) {
              case 0 -> back = true;
              case 1 -> addCandidate((TSEEmployee) tseProfessional);
              case 2 -> removeCandidate((TSEEmployee) tseProfessional);
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

  private static void loadTSEProfessionals() {
    TSEMap.put("fulano", new CertifiedProfessional.Builder()
        .user("a")
        .password("a")
        .build());
    TSEMap.put("a", new TSEEmployee.Builder()
        .user("a")
        .password("a")
        .build());
  }

  public static void startSession() {

    // Startup the current Model instance
    String ModelPassword = "password";

    model = new Model.Builder()
        .password(ModelPassword)
        .build();

    President presidentCandidate1 = new President.Builder().name("João").number(12).party("PDS1").build();
    model.addPresidentCandidate(presidentCandidate1, ModelPassword);
    President presidentCandidate2 = new President.Builder().name("Maria").number(14).party("ED").build();
    model.addPresidentCandidate(presidentCandidate2, ModelPassword);
    FederalDeputy federalDeputyCandidate1 = new FederalDeputy.Builder().name("Carlos").number(12345).party("PDS1")
        .state("MG").build();
    model.addFederalDeputyCandidate(federalDeputyCandidate1, ModelPassword);
    FederalDeputy federalDeputyCandidate2 = new FederalDeputy.Builder().name("Cleber").number(54321).party("PDS2")
        .state("MG").build();
    model.addFederalDeputyCandidate(federalDeputyCandidate2, ModelPassword);
    FederalDeputy federalDeputyCandidate3 = new FederalDeputy.Builder().name("Sofia").number(11211).party("IHC")
        .state("MG").build();
    model.addFederalDeputyCandidate(federalDeputyCandidate3, ModelPassword);

    // Startar todo os eleitores e profissionais do TSE
    loadVoters();
    loadTSEProfessionals();

    startMenu();
  }
}
