import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.text.DecimalFormat;

public class Model {
  private final String password;

  private boolean status;
  private DecimalFormat decimalFormater = new DecimalFormat("0.00");

  // NullVotes
  private int nullPresidentVotes = 0;
  private int nullFederalDeputyVotes = 0;
  private int nullDeputadoEstadualVotes = 0;
  private int nullGovernadorVotes = 0;
  private int nullSenadorVotes = 0;
  private int nullPrefeitoVotes = 0;
  private int nullVereadorVotes = 0;

  // ProtestVotes
  private int presidentProtestVotes = 0;
  private int federalDeputyProtestVotes = 0;
  private int deputadoEstadualProtestVotes = 0;
  private int governadorProtestVotes = 0;
  private int senadorProtestVotes = 0;
  private int prefeitoProtestVotes = 0;
  private int vereadorProtestVotes = 0;

  // Na prática guardaria uma hash do eleitor
  private Map<Voter, Integer> votersPresident = new HashMap<Voter, Integer>();
  private Map<Voter, Integer> votersFederalDeputy = new HashMap<Voter, Integer>();
  private Map<Voter, Integer> votersDeputadoEstadual = new HashMap<Voter, Integer>();
  private Map<Voter, Integer> votersGovernador = new HashMap<Voter, Integer>();
  private Map<Voter, Integer> votersSenador = new HashMap<Voter, Integer>();
  private Map<Voter, Integer> votersPrefeito = new HashMap<Voter, Integer>();
  private Map<Voter, Integer> votersVereador = new HashMap<Voter, Integer>();

  private Map<Integer, President> presidentCandidates = new HashMap<Integer, President>();

  private Map<String, FederalDeputy> federalDeputyCandidates = new HashMap<String, FederalDeputy>();

  private Map<String, Senador> senadorCandidates = new HashMap<String, Senador>();

  private Map<String, Governador> governadorCandidates = new HashMap<String, Governador>();

  private Map<String, DeputadoEstadual> deputadoEstadualCandidates = new HashMap<String, DeputadoEstadual>();

  private Map<String, Prefeito> prefeitoCandidates = new HashMap<String, Prefeito>();

  private Map<String, Vereador> vereadorCandidates = new HashMap<String, Vereador>();

  private Map<String, Partido> partidos = new HashMap<String, Partido>();

  private Map<Voter, FederalDeputy> tempFDVote = new HashMap<Voter, FederalDeputy>();
  private Map<Voter, Vereador> tempVVote = new HashMap<Voter, Vereador>();
  private Map<Voter, DeputadoEstadual> tempDEVote = new HashMap<Voter, DeputadoEstadual>();



  public static class Builder {
    protected String password;

    public Builder password(String password) {
      this.password = password;
      return this;
    }

    public Model build() {
      if (password == null)
        throw new IllegalArgumentException("password mustn't be null");

      if (password.isEmpty())
        throw new IllegalArgumentException("password mustn't be empty");

      return new Model(this.password);
    }
  }

  protected Model(
      String password) {
    this.password = password;
    this.status = false;
    this.nullFederalDeputyVotes = 0;
    this.nullPresidentVotes = 0;
    this.presidentProtestVotes = 0;
    this.federalDeputyProtestVotes = 0;
  }

  private Boolean isValid(String password) {
    return this.password.equals(password);
  }

  public void computeVote(Candidate candidate, Voter voter) {
    if (candidate instanceof President) {
      if (votersPresident.get(voter) != null && votersPresident.get(voter) >= 1)
        throw new StopTrap("Você não pode votar mais de uma vez para presidente");

      candidate.numVotes++;
      votersPresident.put(voter, 1);
    } else if (candidate instanceof FederalDeputy) {
      if (votersFederalDeputy.get(voter) != null && votersFederalDeputy.get(voter) >= 2)
        throw new StopTrap("Você não pode votar mais de uma vez para deputado federal");

      if (tempFDVote.get(voter) != null && tempFDVote.get(voter).equals(candidate))
        throw new Warning("Você não pode votar mais de uma vez em um mesmo candidato");

      candidate.numVotes++;
      if (votersFederalDeputy.get(voter) == null) {
        votersFederalDeputy.put(voter, 1);
        tempFDVote.put(voter, (FederalDeputy) candidate);
      } else {
        votersFederalDeputy.put(voter, this.votersFederalDeputy.get(voter) + 1);
        tempFDVote.remove(voter);
      }
    } else if (candidate instanceof Senador) {
      if (votersSenador.get(voter) != null && votersSenador.get(voter) >= 1)
        throw new StopTrap("Você não pode votar mais de uma vez para senador");
      candidate.numVotes++;
      votersSenador.put(voter, 1);
    } else if (candidate instanceof Governador) {
      if (votersGovernador.get(voter) != null && votersGovernador.get(voter) >= 1)
        throw new StopTrap("Você não pode votar mais de uma vez para governador");
      candidate.numVotes++;
      votersGovernador.put(voter, 1);
    } else if (candidate instanceof DeputadoEstadual) {
      if (votersDeputadoEstadual.get(voter) != null && votersDeputadoEstadual.get(voter) >= 2)
        throw new StopTrap("Você não pode votar mais de uma vez para deputado estadual");

      if (tempDEVote.get(voter) != null && tempDEVote.get(voter).equals(candidate))
        throw new Warning("Você não pode votar mais de uma vez em um mesmo candidato");

      candidate.numVotes++;
      if (votersDeputadoEstadual.get(voter) == null) {
        votersDeputadoEstadual.put(voter, 1);
        tempDEVote.put(voter, (DeputadoEstadual) candidate);
      } else {
        votersDeputadoEstadual.put(voter, this.votersDeputadoEstadual.get(voter) + 1);
        tempDEVote.remove(voter);
      }
    } else if (candidate instanceof Prefeito) {
      if (votersPrefeito.get(voter) != null && votersPrefeito.get(voter) >= 1)
        throw new StopTrap("Você não pode votar mais de uma vez para prefeito");
      candidate.numVotes++;
      votersPrefeito.put(voter, 1);

    } else if (candidate instanceof Vereador) {
      if (votersVereador.get(voter) != null && votersVereador.get(voter) >= 2)
        throw new StopTrap("Você não pode votar mais de uma vez para vereador");

      if (tempVVote.get(voter) != null && tempVVote.get(voter).equals(candidate))
        throw new Warning("Você não pode votar mais de uma vez em um mesmo candidato");

      candidate.numVotes++;
      if (votersVereador.get(voter) == null) {
        votersVereador.put(voter, 1);
        tempVVote.put(voter, (Vereador) candidate);
      } else {
        votersVereador.put(voter, this.votersVereador.get(voter) + 1);
        tempVVote.remove(voter);
      }
    }
  }

  public void computeNullVote(String type, Voter voter) {
    if (type.equals("President")) {
      if (this.votersPresident.get(voter) != null && votersPresident.get(voter) >= 1)
        throw new StopTrap("Você não pode votar mais de uma vez para presidente");

      this.nullPresidentVotes++;
      votersPresident.put(voter, 1);
    } else if (type.equals("FederalDeputy")) {
      if (this.votersFederalDeputy.get(voter) != null && this.votersFederalDeputy.get(voter) >= 2)
        throw new StopTrap("Você não pode votar mais de uma vez para deputado federal");

      this.nullFederalDeputyVotes++;
      if (this.votersFederalDeputy.get(voter) == null)
        votersFederalDeputy.put(voter, 1);
      else
        votersFederalDeputy.put(voter, this.votersFederalDeputy.get(voter) + 1);
    }
  }

  public void computeProtestVote(String type, Voter voter) {
    if (type.equals("President")) {
      if (this.votersPresident.get(voter) != null && votersPresident.get(voter) >= 1)
        throw new StopTrap("Você não pode votar mais de uma vez para presidente");

      this.presidentProtestVotes++;
      votersPresident.put(voter, 1);
    } else if (type.equals("FederalDeputy")) {
      if (this.votersFederalDeputy.get(voter) != null && this.votersFederalDeputy.get(voter) >= 2)
        throw new StopTrap("Você não pode votar mais de uma vez para deputado federal");

      this.federalDeputyProtestVotes++;
      if (this.votersFederalDeputy.get(voter) == null)
        votersFederalDeputy.put(voter, 1);
      else
        votersFederalDeputy.put(voter, this.votersFederalDeputy.get(voter) + 1);
    }
  }

  public boolean getStatus() {
    return this.status;
  }

  public void start(String password) {
    if (!isValid(password))
      throw new Warning("Senha inválida");

    this.status = true;
  }

  public void finish(String password) {
    if (!isValid(password))
      throw new Warning("Senha inválida");

    this.status = false;
  }

  public Partido getPartidoBySigla(String sigla) {
    return this.partidos.get(sigla);
  }

  public void addPartido(Partido partido, String password) {
    if (!isValid(password))
      throw new Warning("Senha inválida");

    if (this.partidos.get(partido.sigla) != null)
      throw new Warning("Sigla do partido indisponível.");

    this.partidos.put(partido.sigla, partido);
  }

  public void removePartido(Partido partido, String password) {
    if (!isValid(password))
      throw new Warning("Senha inválida");

      String mensagem = "Já existem candidatos cadastrados nesse partido";
      if(Produtos.PRODUTO_1){
        for (Map.Entry<Integer, President> candidateEntry : presidentCandidates.entrySet()) {
          President candidate = candidateEntry.getValue();
          if(candidate.party == partido.sigla){
            throw new Warning(mensagem);
          }
        }
        for (Map.Entry<String, FederalDeputy> candidateEntry : federalDeputyCandidates.entrySet()) {
          FederalDeputy candidate = candidateEntry.getValue();
          if(candidate.party == partido.sigla){
            throw new Warning(mensagem);
          }
        }
        for (Map.Entry<String, DeputadoEstadual> candidateEntry : deputadoEstadualCandidates.entrySet()) {
          DeputadoEstadual candidate = candidateEntry.getValue();
          if(candidate.party == partido.sigla){
            throw new Warning(mensagem);
          }
        }
        for (Map.Entry<String, Governador> candidateEntry : governadorCandidates.entrySet()) {
          Governador candidate = candidateEntry.getValue();
          if(candidate.party == partido.sigla){
            throw new Warning(mensagem);
          }
        }
        for (Map.Entry<String, Senador> candidateEntry : senadorCandidates.entrySet()) {
          Senador candidate = candidateEntry.getValue();
          if(candidate.party == partido.sigla){
            throw new Warning(mensagem);
          }
        }
      }else if(Produtos.PRODUTO_2){
        for (Map.Entry<String, Prefeito> candidateEntry : prefeitoCandidates.entrySet()) {
          Prefeito candidate = candidateEntry.getValue();
          if(candidate.party == partido.sigla){
            throw new Warning(mensagem);
          }
        }
        for (Map.Entry<String, Vereador> candidateEntry : vereadorCandidates.entrySet()) {
          Vereador candidate = candidateEntry.getValue();
          if(candidate.party == partido.sigla){
            throw new Warning(mensagem);
          }
        }
      }
      this.partidos.remove(partido.sigla);
    }

  public void addPresidentCandidate(President candidate, String password) {
    if (!isValid(password))
      throw new Warning("Senha inválida");

    if (this.presidentCandidates.get(candidate.number) != null)
      throw new Warning("Numero de candidato indisponível");

    if (this.partidos.get(candidate.party) == null)
      throw new Warning("O partido do candidato não existe.");

    this.presidentCandidates.put(candidate.number, candidate);

  }

  public void removePresidentCandidate(President candidate, String password) {
    if (!isValid(password))
      throw new Warning("Senha inválida");

    this.presidentCandidates.remove(candidate.number);
  }

  public void removePrefeitoCandidate(Prefeito candidate, String password) {
    if (!isValid(password))
      throw new Warning("Senha inválida");

    this.prefeitoCandidates.remove(candidate.state + candidate.number);
  }

  public void removeVereadorCandidate(Vereador candidate, String password) {
    if (!isValid(password))
      throw new Warning("Senha inválida");

    this.vereadorCandidates.remove(candidate.state + candidate.number);
  }


  public Candidate getCandidateByNumber(String state, int number, String type) {

    switch (type) {
      case "President": 
        return getPresidentByNumber(number);
      case "Senador": 
        return getSenadorByNumber(state, number);
      case "Governador": 
        return getGovernadorByNumber(state, number);
      case "FederalDeputy": 
        return getFederalDeputyByNumber(state, number);
      case "DeputadoEstadual": 
        return getDeputadoEstadualByNumber(state, number);
      case "Prefeito": 
        return getPrefeitoByNumber(state, number);
      case "Vereador": 
        return getVereadorByNumber(state, number);
      default: return null;
    }

  }

  public President getPresidentByNumber(int number) {
    return this.presidentCandidates.get(number);
  }

  public FederalDeputy getFederalDeputyByNumber(String state, int number) {
    return this.federalDeputyCandidates.get(state + number);
  }

  public Senador getSenadorByNumber(String state, int number) {
    return this.senadorCandidates.get(state + number);
  }


  public Governador getGovernadorByNumber(String state, int number) {
    return this.governadorCandidates.get(state + number);
  }

  public DeputadoEstadual getDeputadoEstadualByNumber(String state, int number) {
    return this.deputadoEstadualCandidates.get(state + number);
  }

  public Prefeito getPrefeitoByNumber(String state, int number) {
    return this.prefeitoCandidates.get(state + number);
  }

  public Vereador getVereadorByNumber(String state, int number) {
    return this.vereadorCandidates.get(state + number);
  }

  public void addFederalDeputyCandidate(FederalDeputy candidate, String password) {
    if (!isValid(password))
      throw new Warning("Senha inválida");

    if (this.federalDeputyCandidates.get(candidate.state + candidate.number) != null)
      throw new Warning("Numero de candidato indisponível");

    if (this.partidos.get(candidate.party) == null)
      throw new Warning("O partido do candidato não existe.");

    this.federalDeputyCandidates.put(candidate.state + candidate.number, candidate);
  }

  public void addSenadorCandidate(Senador candidate, String password) {
    if (!isValid(password))
      throw new Warning("Senha inválida");

    if (this.senadorCandidates.get(candidate.state + candidate.number) != null)
      throw new Warning("Numero de candidato indisponível");

    if (this.partidos.get(candidate.party) == null)
      throw new Warning("O partido do candidato não existe.");

    this.senadorCandidates.put(candidate.state + candidate.number, candidate);
  }

  public void addPrefeitoCandidate(Prefeito candidate, String password) {
    if (!isValid(password))
      throw new Warning("Senha inválida");

    if (this.prefeitoCandidates.get(candidate.state + candidate.number) != null)
      throw new Warning("Numero de candidato indisponível");

    if (this.partidos.get(candidate.party) == null)
      throw new Warning("O partido do candidato não existe.");

    this.prefeitoCandidates.put(candidate.state + candidate.number, candidate);
  }

  public void addVereadorCandidate(Vereador candidate, String password) {
    if (!isValid(password))
      throw new Warning("Senha inválida");

    if (this.vereadorCandidates.get(candidate.state + candidate.number) != null)
      throw new Warning("Numero de candidato indisponível");

    if (this.partidos.get(candidate.party) == null)
      throw new Warning("O partido do candidato não existe.");

    this.vereadorCandidates.put(candidate.state + candidate.number, candidate);
  }

  public void addDeputadoEstadualCandidate(DeputadoEstadual candidate, String password) {
    if (!isValid(password))
      throw new Warning("Senha inválida");

    if (this.deputadoEstadualCandidates.get(candidate.state + candidate.number) != null)
      throw new Warning("Numero de candidato indisponível");

    if (this.partidos.get(candidate.party) == null)
      throw new Warning("O partido do candidato não existe.");

    this.deputadoEstadualCandidates.put(candidate.state + candidate.number, candidate);
  }

  public void addGovernadorCandidate(Governador candidate, String password) {
    if (!isValid(password))
      throw new Warning("Senha inválida");

    if (this.governadorCandidates.get(candidate.state + candidate.number) != null)
      throw new Warning("Numero de candidato indisponível");

    if (this.partidos.get(candidate.party) == null)
      throw new Warning("O partido do candidato não existe.");

    this.governadorCandidates.put(candidate.state + candidate.number, candidate);
  }

  public void removeSenadorCandidate(Senador candidate, String password) {
    if (!isValid(password))
      throw new Warning("Senha inválida");

    this.senadorCandidates.remove(candidate.state + candidate.number);
  }

  public void removeGovernadorCandidate(Governador candidate, String password) {
    if (!isValid(password))
      throw new Warning("Senha inválida");

    this.governadorCandidates.remove(candidate.state + candidate.number);
  }

  public void removeDeputadoEstadualCandidate(DeputadoEstadual candidate, String password) {
    if (!isValid(password))
      throw new Warning("Senha inválida");

    this.deputadoEstadualCandidates.remove(candidate.state + candidate.number);
  }

  public void removeFederalDeputyCandidate(FederalDeputy candidate, String password) {
    if (!isValid(password))
      throw new Warning("Senha inválida");

    this.federalDeputyCandidates.remove(candidate.state + candidate.number);
  }

  public String getResults(String password) {
    if (!isValid(password))
      throw new Warning("Senha inválida");

    if (this.status)
      throw new StopTrap("Eleição ainda não finalizou, não é possível gerar o resultado");

    var builder = new StringBuilder();
    builder.append("\n=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=\n\n");
    builder.append("Resultado da eleicao:\n");

    if (Produtos.PRODUTO_1) {
      // President
      // Governador
      // Senador
      // FederalDeputy
      // DeputadoEstadual
      getResultsProd_1(builder);
    } else if (Produtos.PRODUTO_2) {
      // Prefeito
      // Vereador
      getResultsProd_2(builder);
    }

    return builder.toString();
  }

  // public void getResultsMajorityelection(StringBuilder builder) {
  //   builder.append("  Votos para presidente\n"); // Possível refatoração
  // }

  public void getResultsPresident(StringBuilder builder) {
    var presidentRank = new ArrayList<President>();
    int totalVotesP = presidentProtestVotes + nullPresidentVotes;
    for (Map.Entry<Integer, President> candidateEntry : presidentCandidates.entrySet()) {
      President candidate = candidateEntry.getValue();
      totalVotesP += candidate.numVotes;
      presidentRank.add(candidate);
    }
    var sortedPresidentRank = presidentRank.stream()
    .sorted((o1, o2) -> o1.numVotes == o2.numVotes ? 0 : o1.numVotes < o2.numVotes ? 1 : -1)
    .collect(Collectors.toList());

    double generalVotes = ((double) totalVotesP * 100);

    builder.append("  Votos para presidente\n");
    builder.append("  Total: " + totalVotesP + "\n");
    builder.append("  Votos nulos: " + nullPresidentVotes + " ("
        + decimalFormater.format(generalVotes == 0 ? 0 : (double) nullPresidentVotes / (double) totalVotesP * 100)
        + "%)\n");
    builder.append("  Votos brancos: " + presidentProtestVotes + " ("
        + decimalFormater.format(generalVotes == 0 ? 0 : (double) presidentProtestVotes / (double) totalVotesP * 100)
        + "%)\n");
    builder.append("\tNumero - Partido - Nome  - Votos  - % dos votos totais\n");
    for (President candidate : sortedPresidentRank) {
      builder.append("\t" + candidate.number + " - " + candidate.party + " - " + candidate.name + " - "
          + candidate.numVotes + " - "
          + decimalFormater.format(generalVotes == 0 ? 0 : (double) candidate.numVotes / (double) totalVotesP * 100)
          + "%\n");
    }

    President electPresident = sortedPresidentRank.get(0);
    builder.append("\n\n  Presidente eleito:\n");
    if (generalVotes == 0)
      builder.append("  Não houveram votos para que algum candidato fosse eleito.\n");
    else {
      builder.append("  " + electPresident.name + " do " + electPresident.party + " com "
          + decimalFormater.format(
              generalVotes == 0 ? 0 : (double) electPresident.numVotes / (double) totalVotesP * 100)
          + "% dos votos\n");
    }
  }
  
  public void getResultsFederalDeputy(StringBuilder builder) {
    var federalDeputyRank = new ArrayList<FederalDeputy>();
    int totalVotesFD = federalDeputyProtestVotes + nullFederalDeputyVotes;
    for (Map.Entry<String, FederalDeputy> candidateEntry : federalDeputyCandidates.entrySet()) {
      FederalDeputy candidate = candidateEntry.getValue();
      totalVotesFD += candidate.numVotes;
      federalDeputyRank.add(candidate);
    }
    var sortedFederalDeputyRank = federalDeputyRank.stream()
        .sorted((o1, o2) -> o1.numVotes == o2.numVotes ? 0 : o1.numVotes < o2.numVotes ? 1 : -1)
        .collect(Collectors.toList());

    double generalVotes = ((double) totalVotesFD * 100);

    builder.append("\n=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=\n\n");
    builder.append("\n\n  Votos para deputado federal\n");
    builder.append("  Total: " + totalVotesFD + "\n");
    builder.append("  Votos nulos: " + nullFederalDeputyVotes + " ("
        + decimalFormater.format(generalVotes == 0 ? 0 : (double) nullFederalDeputyVotes / (double) totalVotesFD * 100)
        + "%)\n");
    builder.append("  Votos brancos: " + federalDeputyProtestVotes + " ("
        + decimalFormater
            .format(generalVotes == 0 ? 0 : (double) federalDeputyProtestVotes / (double) totalVotesFD * 100)
        + "%)\n");
    builder.append("\tNumero - Partido - Nome - Estado - Votos - % dos votos totais\n");
    for (FederalDeputy candidate : sortedFederalDeputyRank) {
      builder.append(
          "\t" + candidate.number + " - " + candidate.party + " - " + candidate.state + " - " + candidate.name + " - "
              + candidate.numVotes + " - "
              + decimalFormater
                  .format(generalVotes == 0 ? 0 : (double) candidate.numVotes / (double) totalVotesFD * 100)
              + "%\n");
    }
    FederalDeputy firstDeputy = sortedFederalDeputyRank.get(0);
    FederalDeputy secondDeputy = sortedFederalDeputyRank.get(1);
    builder.append("\n\n  Deputados eleitos:\n");
    if (generalVotes == 0)
      builder.append("  Não houveram votos para que algum candidato fosse eleito.\n");
    else {
      builder.append("  1º " + firstDeputy.name + " do " + firstDeputy.party + " com "
          + decimalFormater.format(generalVotes == 0 ? 0 : (double) firstDeputy.numVotes / (double) totalVotesFD * 100)
          + "% dos votos\n");
      builder.append("  2º " + secondDeputy.name + " do " + secondDeputy.party + " com "
          + decimalFormater.format(generalVotes == 0 ? 0 : (double) secondDeputy.numVotes / (double) totalVotesFD * 100)
          + "% dos votos\n");
    }
  }

  public void getResultsGovernador(StringBuilder builder) {
    var governadorRank = new ArrayList<Governador>();

    int totalVotesG = governadorProtestVotes + nullGovernadorVotes;
    for (Map.Entry<String, Governador> candidateEntry : governadorCandidates.entrySet()) {
      Governador candidate = candidateEntry.getValue();
      totalVotesG += candidate.numVotes;
      governadorRank.add(candidate);
    }

    var sortedGovernadorRank = governadorRank.stream()
    .sorted((o1, o2) -> o1.numVotes == o2.numVotes ? 0 : o1.numVotes < o2.numVotes ? 1 : -1)
    .collect(Collectors.toList());

    double generalVotes = ((double) totalVotesG * 100);

    builder.append("\n=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=\n\n");
    builder.append("  Votos para governador\n");
    builder.append("  Total: " + totalVotesG + "\n");
    builder.append("  Votos nulos: " + nullGovernadorVotes + " ("
        + decimalFormater.format(generalVotes == 0 ? 0 : (double) nullGovernadorVotes / (double) totalVotesG * 100)
        + "%)\n");
    builder.append("  Votos brancos: " + presidentProtestVotes + " ("
        + decimalFormater.format(generalVotes == 0 ? 0 : (double) presidentProtestVotes / (double) totalVotesG * 100)
        + "%)\n");
    builder.append("\tNumero - Partido - Nome  - Votos  - % dos votos totais\n");
    for (Governador candidate : sortedGovernadorRank) {
      builder.append("\t" + candidate.number + " - " + candidate.party + " - " + candidate.name + " - "
          + candidate.numVotes + " - "
          + decimalFormater.format(generalVotes == 0 ? 0 : (double) candidate.numVotes / (double) totalVotesG * 100)
          + "%\n");
    }

    Governador electGovernador = sortedGovernadorRank.get(0);
    builder.append("\n\n  Governador eleito:\n");
    if (generalVotes == 0)
      builder.append("  Não houveram votos para que algum candidato fosse eleito.\n");
    else {
      builder.append("  " + electGovernador.name + " do " + electGovernador.party + " com "
          + decimalFormater.format(
              generalVotes == 0 ? 0 : (double) electGovernador.numVotes / (double) totalVotesG * 100)
          + "% dos votos\n");
    }
  }

  public void getResultsSenador(StringBuilder builder) {
    var senadorRank = new ArrayList<Senador>();

    int totalVotesS = senadorProtestVotes + nullSenadorVotes;
    for (Map.Entry<String, Senador> candidateEntry : senadorCandidates.entrySet()) {
      Senador candidate = candidateEntry.getValue();
      totalVotesS += candidate.numVotes;
      senadorRank.add(candidate);
    }

    var sortedSenadorRank = senadorRank.stream()
    .sorted((o1, o2) -> o1.numVotes == o2.numVotes ? 0 : o1.numVotes < o2.numVotes ? 1 : -1)
    .collect(Collectors.toList());

    double generalVotes = ((double) totalVotesS * 100);

    builder.append("\n=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=\n\n");
    builder.append("  Votos para senador\n");
    builder.append("  Total: " + totalVotesS + "\n");
    builder.append("  Votos nulos: " + nullSenadorVotes + " ("
        + decimalFormater.format(generalVotes == 0 ? 0 : (double) nullSenadorVotes / (double) totalVotesS * 100)
        + "%)\n");
    builder.append("  Votos brancos: " + presidentProtestVotes + " ("
        + decimalFormater.format(generalVotes == 0 ? 0 : (double) presidentProtestVotes / (double) totalVotesS * 100)
        + "%)\n");
    builder.append("\tNumero - Partido - Nome  - Votos  - % dos votos totais\n");
    for (Senador candidate : sortedSenadorRank) {
      builder.append("\t" + candidate.number + " - " + candidate.party + " - " + candidate.name + " - "
          + candidate.numVotes + " - "
          + decimalFormater.format(generalVotes == 0 ? 0 : (double) candidate.numVotes / (double) totalVotesS * 100)
          + "%\n");
    }

    Senador electSenador = sortedSenadorRank.get(0);
    builder.append("\n\n  Senador eleito:\n");
    if (generalVotes == 0)
      builder.append("  Não houveram votos para que algum candidato fosse eleito.\n");
    else {
      builder.append("  " + electSenador.name + " do " + electSenador.party + " com "
          + decimalFormater.format(
              generalVotes == 0 ? 0 : (double) electSenador.numVotes / (double) totalVotesS * 100)
          + "% dos votos\n");
    }
  }

  public void getResultsDeputadoEstadual(StringBuilder builder) {
    var deputadoEstadualRank = new ArrayList<DeputadoEstadual>();
    int totalVotesFD = deputadoEstadualProtestVotes + nullDeputadoEstadualVotes;
    for (Map.Entry<String, DeputadoEstadual> candidateEntry : deputadoEstadualCandidates.entrySet()) {
      DeputadoEstadual candidate = candidateEntry.getValue();
      totalVotesFD += candidate.numVotes;
      deputadoEstadualRank.add(candidate);
    }
    var sortedDeputadoEstadualRank = deputadoEstadualRank.stream()
        .sorted((o1, o2) -> o1.numVotes == o2.numVotes ? 0 : o1.numVotes < o2.numVotes ? 1 : -1)
        .collect(Collectors.toList());

    double generalVotes = ((double) totalVotesFD * 100);

    builder.append("\n=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=\n\n");
    builder.append("\n\n  Votos para deputados estaduais\n");
    builder.append("  Total: " + totalVotesFD + "\n");
    builder.append("  Votos nulos: " + nullDeputadoEstadualVotes + " ("
        + decimalFormater.format(generalVotes == 0 ? 0 : (double) nullDeputadoEstadualVotes / (double) totalVotesFD * 100)
        + "%)\n");
    builder.append("  Votos brancos: " + deputadoEstadualProtestVotes + " ("
        + decimalFormater
            .format(generalVotes == 0 ? 0 : (double) deputadoEstadualProtestVotes / (double) totalVotesFD * 100)
        + "%)\n");
    builder.append("\tNumero - Partido - Nome - Estado - Votos - % dos votos totais\n");
    for (DeputadoEstadual candidate : sortedDeputadoEstadualRank) {
      builder.append(
          "\t" + candidate.number + " - " + candidate.party + " - " + candidate.state + " - " + candidate.name + " - "
              + candidate.numVotes + " - "
              + decimalFormater
                  .format(generalVotes == 0 ? 0 : (double) candidate.numVotes / (double) totalVotesFD * 100)
              + "%\n");
    }
    DeputadoEstadual firstDeputy = sortedDeputadoEstadualRank.get(0);
    DeputadoEstadual secondDeputy = sortedDeputadoEstadualRank.get(1);
    builder.append("\n\n  Deputados estaduais eleitos:\n");
    if (generalVotes == 0)
      builder.append("  Não houveram votos para que algum candidato fosse eleito.\n");
    else {
      builder.append("  1º " + firstDeputy.name + " do " + firstDeputy.party + " com "
          + decimalFormater.format(generalVotes == 0 ? 0 : (double) firstDeputy.numVotes / (double) totalVotesFD * 100)
          + "% dos votos\n");
      builder.append("  2º " + secondDeputy.name + " do " + secondDeputy.party + " com "
          + decimalFormater.format(generalVotes == 0 ? 0 : (double) secondDeputy.numVotes / (double) totalVotesFD * 100)
          + "% dos votos\n");
    }
  }
  
  public void getResultsPrefeito(StringBuilder builder) {
    var prefeitoRank = new ArrayList<Prefeito>();

    int totalVotesS = prefeitoProtestVotes + nullPrefeitoVotes;
    for (Map.Entry<String, Prefeito> candidateEntry : prefeitoCandidates.entrySet()) {
      Prefeito candidate = candidateEntry.getValue();
      totalVotesS += candidate.numVotes;
      prefeitoRank.add(candidate);
    }

    var sortedPrefeitoRank = prefeitoRank.stream()
    .sorted((o1, o2) -> o1.numVotes == o2.numVotes ? 0 : o1.numVotes < o2.numVotes ? 1 : -1)
    .collect(Collectors.toList());

    double generalVotes = ((double) totalVotesS * 100);

    builder.append("\n=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=\n\n");
    builder.append("  Votos para prefeito\n");
    builder.append("  Total: " + totalVotesS + "\n");
    builder.append("  Votos nulos: " + nullPrefeitoVotes + " ("
        + decimalFormater.format(generalVotes == 0 ? 0 : (double) nullPrefeitoVotes / (double) totalVotesS * 100)
        + "%)\n");
    builder.append("  Votos brancos: " + presidentProtestVotes + " ("
        + decimalFormater.format(generalVotes == 0 ? 0 : (double) presidentProtestVotes / (double) totalVotesS * 100)
        + "%)\n");
    builder.append("\tNumero - Partido - Nome  - Votos  - % dos votos totais\n");
    for (Prefeito candidate : sortedPrefeitoRank) {
      builder.append("\t" + candidate.number + " - " + candidate.party + " - " + candidate.name + " - "
          + candidate.numVotes + " - "
          + decimalFormater.format(generalVotes == 0 ? 0 : (double) candidate.numVotes / (double) totalVotesS * 100)
          + "%\n");
    }

    Prefeito electPrefeito = sortedPrefeitoRank.get(0);
    builder.append("\n\n  Prefeito eleito:\n");
    if (generalVotes == 0)
      builder.append("  Não houveram votos para que algum candidato fosse eleito.\n");
    else {
      builder.append("  " + electPrefeito.name + " do " + electPrefeito.party + " com "
          + decimalFormater.format(
              generalVotes == 0 ? 0 : (double) electPrefeito.numVotes / (double) totalVotesS * 100)
          + "% dos votos\n");
    }    
  }

  public void getResultsVereador(StringBuilder builder) {
    var vereadorRank = new ArrayList<Vereador>();
    int totalVotesFD = vereadorProtestVotes + nullVereadorVotes;
    for (Map.Entry<String, Vereador> candidateEntry : vereadorCandidates.entrySet()) {
      Vereador candidate = candidateEntry.getValue();
      totalVotesFD += candidate.numVotes;
      vereadorRank.add(candidate);
    }
    var sortedVereadorRank = vereadorRank.stream()
        .sorted((o1, o2) -> o1.numVotes == o2.numVotes ? 0 : o1.numVotes < o2.numVotes ? 1 : -1)
        .collect(Collectors.toList());

    double generalVotes = ((double) totalVotesFD * 100);

    builder.append("\n=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=\n\n");
    builder.append("\n\n  Votos para vereador\n");
    builder.append("  Total: " + totalVotesFD + "\n");
    builder.append("  Votos nulos: " + nullVereadorVotes + " ("
        + decimalFormater.format(generalVotes == 0 ? 0 : (double) nullVereadorVotes / (double) totalVotesFD * 100)
        + "%)\n");
    builder.append("  Votos brancos: " + vereadorProtestVotes + " ("
        + decimalFormater
            .format(generalVotes == 0 ? 0 : (double) vereadorProtestVotes / (double) totalVotesFD * 100)
        + "%)\n");
    builder.append("\tNumero - Partido - Nome - Estado - Votos - % dos votos totais\n");
    for (Vereador candidate : sortedVereadorRank) {
      builder.append(
          "\t" + candidate.number + " - " + candidate.party + " - " + candidate.state + " - " + candidate.name + " - "
              + candidate.numVotes + " - "
              + decimalFormater
                  .format(generalVotes == 0 ? 0 : (double) candidate.numVotes / (double) totalVotesFD * 100)
              + "%\n");
    }
    Vereador firstVereador = sortedVereadorRank.get(0);
    Vereador secondVereador = sortedVereadorRank.get(1);
    builder.append("\n\n  Veradores:\n");
    if (generalVotes == 0)
      builder.append("  Não houveram votos para que algum candidato fosse eleito.\n");
    else {
      builder.append("  1º " + firstVereador.name + " do " + firstVereador.party + " com "
          + decimalFormater.format(generalVotes == 0 ? 0 : (double) firstVereador.numVotes / (double) totalVotesFD * 100)
          + "% dos votos\n");
      builder.append("  2º " + secondVereador.name + " do " + secondVereador.party + " com "
          + decimalFormater.format(generalVotes == 0 ? 0 : (double) secondVereador.numVotes / (double) totalVotesFD * 100)
          + "% dos votos\n");
    }
  }

  public void getResultsProd_1(StringBuilder builder) {
      // President
      // Governador
      // Senador
      // FederalDeputy
      // DeputadoEstadual
    getResultsPresident(builder);
    getResultsGovernador(builder);
    getResultsSenador(builder);
    getResultsFederalDeputy(builder);
    getResultsDeputadoEstadual(builder);
  }

  public void getResultsProd_2(StringBuilder builder) {
    // Prefeito
    // Vereador
    getResultsPrefeito(builder);
    getResultsVereador(builder);
  }

}




