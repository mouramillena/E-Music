<div align="center">
<img  width="407"  alt="Logo" src="https://user-images.githubusercontent.com/70728316/102001820-0ab0ed80-3cd5-11eb-94e8-36d99af2fb70.JPG">
<h2 align="center"> E-Music </h2> 
<p align="center"> SUA API MUSICAL </p>
</div>

## .: OBJETIVO DO PROJETO :.

<p align="justify"> A API tem como objetivo trazer de forma clara e objetiva músicas, álbuns e informações dos seus artistas favoritos.
   <br> Este é um projeto para a disciplina de Programação Orientada a Objetos - POO do curso de Ciência da Computação do IESB - 2/2020.
   <br> Foi exigido os conceitos básicos da POO (Herança, Encapsulamento e Polimorfismo), além dos conceitos de programação S.O.L.I.D </p>

## .: CONCEITOS P.O.O :.
<p align="justify">
<ul>  
   <li><b>HERANÇA</b> -> Princípio que permite que classes compartilhes atributos e métodos. </li>
   <li><b>POLIMORFISMO</b> -> Permite que tipos de classes abstratas representem o comportamento das classes concretas que referenciam.</li>
   <li><b>ENCAPSULAMENTO</b> -> Conceito que liga os dados e funções, os mantendo seguros de interferências externas e má utilizações, tornando as informações privadas apenas a quem as possui.</li>
</ul>
</p>

## .: CONCEITOS S.O.L.I.D :.
<p align="justify">
<ul>  
   <li><b>S</b> -> Princípio da Responsabilidade Única </li>
   <li><b>O</b> -> Aberto - Fechado </li>
   <li><b>L</b> -> Substituição Liskov </li>
   <li><b>I</b> -> Segragação de Interface </li>
   <li><b>D</b> -> Inversão de Dependências</li>  
</ul>
</p>

## .: FUNCIONALIDADES IMPLEMENTADAS :.
<p align="justify">Para Acessar as funcionalidades da API o usuário precisa efetuar seu <b>Login</b> </p>

<p align="justify">
<ul>
   <li><b>CADASTRAR/ ALTERAR/ REMOVER</b>     
    <ul>
      <li>MÚSICA</li>
      <li>ARTISTA</li>
      <li>ÁLBUM</li>
    </ul> </li>   
   
  <li><b>LISTAR</b>      
    <ul>
      <li>MÚSICA</li>
      <li>ARTISTA</li>
    </ul> </li> 
    
   <li><b>PESQUISAR MÚSICA</b>      
    <ul>
     <li>POR NOME</li>
     <li>POR ARTISTA</li>
     <li>POR GÊNERO</li>
    </ul> </li>
    
   <li><b>DETALHAR</b>      
    <ul>
      <li>MÚSICA</li>
      <li>ARTISTA</li>
    </ul> </li> 
     
  <li><b>LISTAR MÚSICAS DE UM ARTISTA</b></li>
  <li><b>LISTAR ÁLBUNS DE UM ARTISTA</b></li>

</ul>
</p>

<p align="justify"><b> :warning: &nbsp;&nbsp; Observação:</b> Para realizar o cadastro o usuário deverá inserir seu email <b>(nome@gmail.com)</b> e senha <b>(123456)</b>
	<br> Temos cadastrados os usuários <b>kenniston</b> e <b>millena</b>.</p>
<br><br>

## .: FERRAMENTAS UTILIZADAS :.
<ul>
   <li><b>LINGUAGEM UTILIZADA:</b> Kotlin </li>
   <li><b>IDE:</b> IntelliJ IDEA (Ambiente de Desenvolvimento Integrado - JetBrains) </li>
   <li><b>ARMAZENAMENTO DE DADOS:</b> Banco H2 </li> 
   <li><b>TESTE DE FUNCIONALIDADES:</b> Postman </li>
</ul>

## .: RODANDO A API :. 
<p align="justify"> Para rodar uma amostra do projeto, comece fazendo checkout da Branch Pricipal (<b>Master</b>) e abra o diretório raiz no IntelliJ IDEA.</p>

### ..: CLONANDO O PROJETO :..

<b>1ºPASSO.</b> Clone o repositório:

```
git clone https://github.com/mouramillena/E-Music
```

<b>2ºPASSO.</b> Esta etapa é realizada para confirmar que você esteja na Branch Principal(<b>Master</b>). 

```
git checkout master
```

<b> :warning: &nbsp;&nbsp; Nota:</b> Se você desejar alterar para uma Branch diferente, substitua "master" pelo nome da Branch que deseja visualizar.
<br>

<b>3ºPASSO.</b> Abra o projeto no <b>IntelliJ</b>.

<b>4ºPASSO.</b> Faça o download dos arquivos da base de dados e a coloque dentro de um diretório chamado `'databases'` como no exemplo abaixo.
Seu formato deve ficar como abaixo:

    ```
    database/
        cases/
            artista.csv
            musica.csv
            album.csv
	        genero.csv
        users/
            login.csv
	    nivel_acesso.csv
    ```

<br><br>
<b>5ºPASSO.</b> Abra o arquivo (...) 
<br>Aperte o <b>Build</b> para construção do projeto, e em seguida <b>Start</b> para a execução do arquivo.
<br>

<b>6ºPASSO.</b> Agora basta acessar o endereço [https://localhost:8080/](https://localhost:8080/) em seu navegador e seguir as 
instruções descritas

<b>7ºPASSO.</b> Abra o <b>POSTMAN</b> para execução das requisições no Banco de Dados.
<br>

## .: BASE DE DADOS :.
<p align="justify"> Utilizaremos a base de dados disponibilizada no link do <a href="https://drive.google.com/drive/folders/1OfR3AooJ7tN007kLtx9ve4AC5xG9gPx_">GOOGLE DRIVE</a>.</p>

<br> Esta base de dados contém <b>4 TABELAS</b>

```
musica.csv
```
<br>

```
artista.csv
```
<br>

```
genero.csv
```
<br>

```
album.csv
```
<br>

## .: PROFESSOR RESPONSÁVEL :.
<p align="justify"> :tophat: <a href="https://github.com/kenniston"> Kenniston </a> </p>

## .: DESENVOLVEDOR :.
<p align="justify"> :octocat: <a href="https://github.com/mouramillena"> Millena Moura </a> </p>
