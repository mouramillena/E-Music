# API COVID-19

## Objetivo:
     - Analisar os dados epidemiológicos sobre o avanço do COVID-19;
     - Auxiliar na tomada de decisão a cerca do isolamento social;
     - Solucionar a dificuldade de acesso a informações relevantes sobre o avanço da doença;
     - Auxiliar no processo de tomada de decisão dos gerentes da saúde pública, 
     <br>neste momento atípico de crise na saúde mundial;


## Autor:
```
{
      {
        "aluna": Millena Moura dos Santos,
        "matricula": 1812130012,
        "github": https://github.com/mouramillena 
      }
}
```

## Contexto:
Esse projeto foi desenvolvido como trabalho prático para a disciplina de Programação Orientada a Objeto - POO do IESB, 
ofertado em 2º/2020 pelo professor [Kenniston](https://github.com/kenniston).

## Quickstart:
1. Faça o download da base de dados e a coloque dentro de um diretório chamado `'datasets'` na raiz do projeto.
Seu formato deve ficar como abaixo:
    ```
    dataset/
        cases/
            brasil.csv
            cidade.csv
            regiao.csv
        users/
            login.csv
            nivel_acesso.csv
    
    ```
    > *NOTA:* Para mais informações a respeito de como fazer a aquisição da base de dados, por favor leia sessão 
    > [Base de Dados](#base-de-dados). 

2. Após esse download basta realizar a construção do projeto e a execução do arquivo 
`./src/main/kotlin/br/iesb/poo/Server.kt`

3. Agora basta acessar o endereço [https://localhost:8080/](https://localhost:8080/) em seu navegador e seguir as 
instruções

## Base de Dados:
Para esse projeto utilizaremos a base de dados produzida por [Raphael Fontes](https://www.kaggle.com/unanimad) e 
disponibilizado em seu perfil do Kaggle.

Para construir o banco de dados utilizado nós utilizamos apenas os registros mais recentes de COVID-19 do Brasil.

O download da versão utilizada no projeto foi realizado no dia 17/09/2020 às 21:00 horas UTC-3 e estará disponível 
no Google Drive pelo link a seguir:
- [Google Drive](https://drive.google.com/drive/folders/1gBBZqN3Ffw9QCx7-iz9Qfy5Y62cuRYwZ)


> **Observação:** Foi realizado um pre-processamento nos dados utilizando um script próprio, por isso não é possível a atualização da 
base por enquanto.
>
<!-- Para atualização da base de dados basta faça o download no link abaixo: 
- [Link para Dataset](https://www.kaggle.com/unanimad/corona-virus-brazil) -->
