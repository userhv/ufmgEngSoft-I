public class Partido{
    protected final String nome;
    protected final String sigla;
    public static class Builder {
      protected String nome;
      protected String sigla;
  
      public Builder nome(String name) {
        this.nome = name;
        return this;
      }
  
      public Builder sigla(String sigla) {
        this.sigla = sigla;
        return this;
      }
  

      public Partido build() {  
        if (nome == null)
          throw new IllegalArgumentException("name mustn't be null");
  
        if (nome.isEmpty())
          throw new IllegalArgumentException("name mustn't be empty");
  
        if (sigla == null)
          throw new IllegalArgumentException("party mustn't be null");
  
        if (sigla.isEmpty())
          throw new IllegalArgumentException("party mustn't be empty");
  
        return new Partido(this.nome, this.sigla);
      }
    }
  
    protected Partido(String nome, String sigla) {
      this.nome = nome;
      this.sigla = sigla;
    }
  }