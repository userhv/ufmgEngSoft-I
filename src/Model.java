import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.text.DecimalFormat;

public class Model {
  private final String password;

  private boolean status;

  private int nullPresidentVotes;

  private int nullFederalDeputyVotes;

  private int presidentProtestVotes;

  private int federalDeputyProtestVotes;

  // Na prática guardaria uma hash do eleitor
  private Map<Voter, Integer> votersPresident = new HashMap<Voter, Integer>();

  // Na prática guardaria uma hash do eleitor
  private Map<Voter, Integer> votersFederalDeputy = new HashMap<Voter, Integer>();

  private Map<Integer, President> presidentCandidates = new HashMap<Integer, President>();

  private Map<String, FederalDeputy> federalDeputyCandidates = new HashMap<String, FederalDeputy>();
  
  private Map<String, Senador> senadorCandidates = new HashMap<String, Senador>();

  private Map<String, Governador> governadorCandidates = new HashMap<String, Governador>();

  private Map<String, DeputadoEstadual> deputadoEstadualCandidates = new HashMap<String, DeputadoEstadual>();

  private Map<String, Prefeito> prefeitoCandidates = new HashMap<String, Prefeito>();

  private Map<String, Vereador> vereadorCandidates = new HashMap<String, Vereador>();

  private Map<String, Partido> partidos = new HashMap<String, Partido>();

  private Map<Voter, FederalDeputy> tempFDVote = new HashMap<Voter, FederalDeputy>();

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
    }
  };

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

  public President getPresidentByNumber(int number) {
    return this.presidentCandidates.get(number);
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

    var decimalFormater = new DecimalFormat("0.00");
    var presidentRank = new ArrayList<President>();
    var federalDeputyRank = new ArrayList<FederalDeputy>();

    var builder = new StringBuilder();

    builder.append("Resultado da eleicao:\n");

    int totalVotesP = presidentProtestVotes + nullPresidentVotes;
    for (Map.Entry<Integer, President> candidateEntry : presidentCandidates.entrySet()) {
      President candidate = candidateEntry.getValue();
      totalVotesP += candidate.numVotes;
      presidentRank.add(candidate);
    }

    int totalVotesFD = federalDeputyProtestVotes + nullFederalDeputyVotes;
    for (Map.Entry<String, FederalDeputy> candidateEntry : federalDeputyCandidates.entrySet()) {
      FederalDeputy candidate = candidateEntry.getValue();
      totalVotesFD += candidate.numVotes;
      federalDeputyRank.add(candidate);
    }

    var sortedFederalDeputyRank = federalDeputyRank.stream()
        .sorted((o1, o2) -> o1.numVotes == o2.numVotes ? 0 : o1.numVotes < o2.numVotes ? 1 : -1)
        .collect(Collectors.toList());

    var sortedPresidentRank = presidentRank.stream()
        .sorted((o1, o2) -> o1.numVotes == o2.numVotes ? 0 : o1.numVotes < o2.numVotes ? 1 : -1)
        .collect(Collectors.toList());


    double generalVotes = ((double) totalVotesFD * 100);

    builder.append("  Votos para presidente\n");
    builder.append("  Total: " + totalVotesP + "\n");
    builder.append("  Votos nulos: " + nullPresidentVotes + " ("+ decimalFormater.format( generalVotes == 0 ? 0 : (double) nullPresidentVotes / (double) totalVotesFD * 100) + "%)\n");
    builder.append("  Votos brancos: " + presidentProtestVotes + " ("+ decimalFormater.format(generalVotes == 0 ? 0 : (double) presidentProtestVotes / (double) totalVotesFD * 100) + "%)\n");
    builder.append("\tNumero - Partido - Nome  - Votos  - % dos votos totais\n");
    for (President candidate : sortedPresidentRank) {
      builder.append("\t" + candidate.number + " - " + candidate.party + " - " + candidate.name + " - "
          + candidate.numVotes + " - "
          + decimalFormater.format(generalVotes == 0 ? 0 : (double) candidate.numVotes / (double) totalVotesP * 100)
          + "%\n");
    }

    President electPresident = sortedPresidentRank.get(0);
    builder.append("\n\n  Presidente eleito:\n");
    if(generalVotes == 0)
      builder.append("  Não houveram votos para que algum candidato fosse eleito.\n");
    else{
      builder.append("  " + electPresident.name + " do " + electPresident.party + " com "
          + decimalFormater.format(generalVotes == 0 ? 0 : (double) electPresident.numVotes / (double) totalVotesP * 100) + "% dos votos\n");
      }
    builder.append("\n=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=\n\n");

    builder.append("\n\n  Votos para deputado federal\n");
    builder.append("  Total: " + totalVotesFD + "\n");
    builder.append("  Votos nulos: " + nullFederalDeputyVotes + " ("
        + decimalFormater.format(generalVotes == 0 ? 0 : (double) nullFederalDeputyVotes / (double) totalVotesFD * 100) + "%)\n");
    builder.append("  Votos brancos: " + federalDeputyProtestVotes + " ("
        + decimalFormater.format(generalVotes == 0 ? 0 : (double) federalDeputyProtestVotes / (double) totalVotesFD * 100) + "%)\n");
    builder.append("\tNumero - Partido - Nome - Estado - Votos - % dos votos totais\n");
    for (FederalDeputy candidate : sortedFederalDeputyRank) {
      builder.append(
          "\t" + candidate.number + " - " + candidate.party + " - " + candidate.state + " - " + candidate.name + " - "
              + candidate.numVotes + " - "
              + decimalFormater.format(generalVotes == 0 ? 0 : (double) candidate.numVotes / (double) totalVotesFD * 100)
              + "%\n");
    }

    FederalDeputy firstDeputy = sortedFederalDeputyRank.get(0);
    FederalDeputy secondDeputy = sortedFederalDeputyRank.get(1);
    builder.append("\n\n  Deputados eleitos:\n");
    if(generalVotes == 0)
      builder.append("  Não houveram votos para que algum candidato fosse eleito.\n");
    else{
      builder.append("  1º " + firstDeputy.name + " do " + firstDeputy.party + " com "
          + decimalFormater.format(generalVotes == 0 ? 0 : (double) firstDeputy.numVotes / (double) totalVotesFD * 100) + "% dos votos\n");
      builder.append("  2º " + secondDeputy.name + " do " + secondDeputy.party + " com "
          + decimalFormater.format(generalVotes == 0 ? 0 : (double) secondDeputy.numVotes / (double) totalVotesFD * 100) + "% dos votos\n");
    }

    return builder.toString();
  }
}
