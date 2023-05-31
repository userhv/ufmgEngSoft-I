// Gerencia a preparação do ambiente (candidatos)
public class TSEEmployee extends TSEProfessional {
  public void addCandidate(Candidate candidate, Model Model, String password) {
    if (candidate instanceof President)
      Model.addPresidentCandidate((President) candidate, password);
    else if (candidate instanceof FederalDeputy)
      Model.addFederalDeputyCandidate((FederalDeputy) candidate, password);
    else if (candidate instanceof Senador)
      Model.addSenadorCandidate((Senador) candidate, password);
    else if (candidate instanceof DeputadoEstadual)
      Model.addDeputadoEstadualCandidate((DeputadoEstadual) candidate, password);
    else if (candidate instanceof Governador)
      Model.addGovernadorCandidate((Governador) candidate, password);
  }

  public void addCandidate_prod_2(Candidate candidate, Model Model, String password) {
    if (candidate instanceof Prefeito)
      Model.addPrefeitoCandidate((Prefeito) candidate, password);
    else if (candidate instanceof Vereador)
      Model.addVereadorCandidate((Vereador) candidate, password);
  }

  public void removeCandidate_prod_1(Candidate candidate, Model Model, String password) {
    if (candidate instanceof President)
      Model.removePresidentCandidate((President) candidate, password);
    else if (candidate instanceof Senador)
      Model.removeSenadorCandidate((Senador) candidate, password);
    else if (candidate instanceof Governador)
      Model.removeGovernadorCandidate((Governador) candidate, password);
    else if (candidate instanceof FederalDeputy)
      Model.removeFederalDeputyCandidate((FederalDeputy) candidate, password);
    else if (candidate instanceof DeputadoEstadual)
      Model.removeDeputadoEstadualCandidate((DeputadoEstadual) candidate, password);
  }

  public void removeCandidate_prod_2(Candidate candidate, Model Model, String password) {
    if (candidate instanceof President)
      Model.removePrefeitoCandidate((Prefeito) candidate, password);
    else if (candidate instanceof Senador)
      Model.removeVereadorCandidate((Vereador) candidate, password);
  }

  public void addPartido(Partido partido, Model Model, String password) {
    Model.addPartido(partido, password);
  }

  public void removePartido(Partido partido, Model Model, String password) {
    Model.removePartido(partido, password);

  }

  public static class Builder {
    protected String user;
    protected String password;

    public Builder user(String user) {
      this.user = user;
      return this;
    }

    public Builder password(String password) {
      this.password = password;
      return this;
    }

    public TSEEmployee build() {
      if (user == null)
        throw new IllegalArgumentException("user mustn't be null");

      if (user.isEmpty())
        throw new IllegalArgumentException("user mustn't be empty");

      if (password == null)
        throw new IllegalArgumentException("password mustn't be null");

      if (password.isEmpty())
        throw new IllegalArgumentException("password mustn't be empty");

      return new TSEEmployee(
          this.user,
          this.password);
    }
  }

  protected TSEEmployee(
      String user,
      String password) {
    super(user, password);
  }
}
