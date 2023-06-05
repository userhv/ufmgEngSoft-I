## Dependencias

- [Java Developer Kit (JDK) 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
- Make

## Comandos make

- `make prod_1` ou `make prod_2` : compilar e executar o produto
- `make clean`: Limpa os arquivos `.class` gerados no build

## Como rodar

- Na root do repositório use o comando `make prod_1` ou `make prod_2` para buildar e executar o programa.

`make prod_1`: gera um produto para votação dos cargos de:

    Presidente
    Senador
    Governador
    Deputado Federal
    Deputado Estadual

`make prod_2`: gera um produto para votação dos cargos de:

    Prefeito
    Vereador



## Como utilizar

OBS:

- O sistema já vem inicializado com candidatos de cada cargo respectivamente relacionado ao seu produto
- O sistema já vem com os dois gestores (de sessão e de candidaturas)
- O sistema já vem com todos os eleitores possíveis para utilizá-los basta checar o arquivo `voterLoad.txt`, além disso é possível adicionar novos eleitores.

No menu inicial para gerenciar candidatos e eleição siga pela opção 2:

- User: `emp` , Password: `12345` -> Cadastro e remoção de candidatos da eleição
- User: `cert` , Password: `54321` -> Inicialização/finalização da eleição (liberar pra poder votar) e mostrar o resultado ao final da eleição.

Além da senha de usuário é necessário a senha da eleição para completar operações relacionadas a gestão da eleição ou candidatos. Essa senha é a palavra `password`

Para votar também existe um eleitor com o título de eleitor nº 123456789012 que pode votar nos candidatos pré-cadastrados

## Execução teste

Para uma execução teste podemos seguir o seguinte passo:

- Ao iniciar a aplicação selecionar a opção 2 e logar com o user `cert`
- Escolher a opção 1 e inserir a senha da urna (`password`) para iniciar a votação
- Escolher a opção 0 para voltar ao menu inicial
- Escolher votar (opção 1) e inserir o nº `123456789012` do eleitor de teste
- Apos isso, você pode seguir as instruções da tela para realizar a sua votação nos cargos de acordo com o produto escolhido.
