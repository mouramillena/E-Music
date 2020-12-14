package br.iesb.poo

import br.iesb.poo.loaders.CSVLoader

import br.iesb.poo.resources.crud.Crud

import br.iesb.poo.resources.mundoMusical.Musica
import br.iesb.poo.resources.mundoMusical.Artista
import br.iesb.poo.resources.mundoMusical.Album
import br.iesb.poo.resources.mundoMusical.MundoMusical

import br.iesb.poo.resources.schemas.MusicaSchema
import br.iesb.poo.resources.schemas.ArtistaSchema
import br.iesb.poo.resources.schemas.AlbumSchema
import br.iesb.poo.resources.schemas.UsersSchema

import br.iesb.poo.resources.user.Login

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.http.*

import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime

import java.io.File


//FUNCAO ARTISTA
fun createArtistaHash(map: HashMap<String, String>): Artista {
   return Artista(
        map["code"]!!.toInt(),
        map["name"]!!,
        map["idade"]!!,
        map["sexo"]!!
    )

//FUNCAO MUSICA
fun createMusicaHash(map: HashMap<String, String>): Musica {
    return Musica(
        map["code"]!!.toInt(),
        map["name"]!!,
        map["duracao"]!!,
        map["album"]!!,
        map["artista"]!!,
        map["genero"]!!
    )

//FUNCAO ALBUM
fun createAlbumHash(map: HashMap<String, String>): Album {
    return Album(
        map["code"]!!.toInt(),
        map["name"]!!,
        map["artista"]!!,
        map["ano"]!!
    )


// MODULO DO CORPO DA API
fun Application.myapp(){

    // INSTALA O CONVERSOR DE OBJETOS PARA JSON PARA O SERVER
    install(ContentNegotiation) {
        gson {
            setPrettyPrinting()
        }
    }

    // CONEXÃO COM DATABASE H2
    Database.connect("jdbc:h2:mem:regular;DB_CLOSE_DELAY=-1;", "org.h2.Driver")


    // TRANSAÇÕES INICIAIS COM O DB
    transaction {

        // CRIAÇÃO DO SCHEMA ARTISTA
        SchemaUtils.create(ArtistaSchema)

        // POPULAR DB COM ARTISTAS
        val artistaLoader = CSVLoader("./dataset/cases/artista.csv")
        artistaLoader.readDataFromFile()

        for (row in 0 until artistaLoader.size) {
            var artistaMap = HashMap<String, String>()
            for (column in artistaLoader.columns) {
                artistaMap[column] = artistaLoader.data[column]!![row]
            }
                val artista = createArtistaHash(artistaMap)

                ArtistaSchema.insert {
                    it[code] = artista.code!!
                    it[name] = artista.name!!
                    it[idade] = artista.idade!!
                    it[sexo] = artista.sexo!!
                }
        }

        // CRIAÇÃO DO SCHEMA MUSICA
        SchemaUtils.create(MusicaSchema)

        // POPULAR DB COM MUSICAS
        val musicaLoader = CSVLoader("./dataset/cases/musica.csv")
        musicaLoader.readDataFromFile()

        for (row in 0 until musicaLoader.size) {
            var musicaMap = HashMap<String, String>()
            for (column in musicaLoader.columns) {
                musicaMap[column] = musicaLoader.data[column]!![row]
            }
//            println("\n\n AQUIIIII ${musicaMap}") - TESTANDO
            val musica = createMusicaHash(musicaMap)

            MusicaSchema.insert {
                it[code] = musica.code!!
                it[name] = musica.name!!
                it[duracao] = musica.duracao!!
                it[album] = musica.album!!
                it[artista] = musica.artista!!
                it[genero] = musica.genero!!
            }
        }

        // CRIAÇÃO DO SCHEMA ALBUM
        SchemaUtils.create(AlbumSchema)

        // POPULAR DB COM ALBUNS
        val albumLoader = CSVLoader("./dataset/cases/album.csv")
        albumLoader.readDataFromFile()

        for (row in 0 until albumLoader.size) {
            var albumMap = HashMap<String, String>()
            for (column in albumLoader.columns) {
                albumMap[column] = albumLoader.data[column]!![row]
            }
//            println("\n\n AQUIIIII ${albumMap}") - TESTANDO
            val album = createAlbumHash(albumMap)

            AlbumSchema.insert {
                it[code] = album.code!!
                it[name] = album.name!!
                it[artista] = album.artista!!
                it[ano] = album.ano!!
            }
        }

        //CRIAÇÃO DO SCHEMA USERS
        SchemaUtils.create(UsersSchema)

        //POPULAR DB COM LOGINS
        val userLoader = CSVLoader("./dataset/users/login.csv")
        userLoader.readDataFromFile()

        println("\n\n${userLoader.size}")

        for (row in 0 until userLoader.size) {
            var userMap = HashMap<String, String>()
            for (column in userLoader.columns) {
                userMap[column] = userLoader.data[column]!![row]
            }
//            val user = createUserHash(userMap)
            val user = Login(null, null, null, null, null, userMap)
            UsersSchema.insert {
                it[email] = user.email!!
                it[cpf] = user.cpf!!
                it[password] = user.password!!
                it[tipo_log] = user.tipo_log!!
                it[date] = DateTime.parse(user.date)!!
            }
        }

    }

    var login: Login? = null
    // INICIALIZAÇÃO DE ROTAS DA API
     install(Routing) {
        get("/") {
                val html = File("./src/templates/index.html").readText()
                call.respondText(html, ContentType.Text.Html)
        }

        //CONSULTA TODOS USUÁRIOS CADASTRADOS
        get("/all-users"){
            if (login == null) {
                call.respondText("Faça login em uma conta de administrador.")
            }
            if (login!!.tipo_log!! == 1) {
                val user = transaction {
                    UsersSchema.selectAll().map {
                        UsersSchema.toObject(it)
                    }
                }
                call.respond(user)
            } else {
                call.respondText("Sem permissão de administrador")
            }
        }

        //LOGOUT
        get("/logout"){
            login = null
            call.respondText("Pode sair do sistema com segurança")
        }

        //LOGIN
        post("/login") {
            val post_login = call.receive<Login>()

            val login_query = transaction {
                UsersSchema.select {
                    UsersSchema.email eq post_login.email!! and (UsersSchema.password eq post_login.password!!)
                }.map {
                    UsersSchema.toObject(it)
                }
            }
            if (login_query.size == 1){
                login = login_query[0]
                call.respondText("Login Efetuado com sucesso!")
            }
            else {
                call.respondText("Email/Senha inválidos")            }
        }

        //INSERINDO NOVO USUÁRIO (SÓ O ADMIN)
        post("/login/cadastro") {
            val post_login = call.receive<Login>()

            if (login == null) {
                call.respondText("Você não está devidamente logado, faça o login!")
            }
            if (login!!.tipo_log!! == 1) {
                val login_query = transaction {
                    UsersSchema.select {
                        UsersSchema.email eq post_login.email!!
                    }.map {
                        UsersSchema.toObject(it)
                    }
                }

                if (login_query.size == 0) {
                    transaction {
                        UsersSchema.insert {
                            it[email] = post_login.email!!
                            it[cpf] = post_login.cpf!!
                            it[password] = post_login.password!!
                            it[tipo_log] = 3
                            it[date] = DateTime.now()
                        }
                    }
                    call.respondText("Cadastro Realizado com sucesso")
                }
                call.respondText("E-mail já cadastrado.")
            } else {
                call.respondText("Você não tem permissão de administrador")
            }
        }

        //ALTERAÇÃO DE SENHA
        post("/mudar-senha") {
            val post_login = call.receive<Login>()
            val login_query = transaction {
                UsersSchema.update ({
                    UsersSchema.email eq post_login.email!!
                }) {
                   it[UsersSchema.password] = post_login.password!!
                }
            }
            call.respondText("Senha alterada para ${post_login.password}")
        }

        //MUDANDO O TIPO DE LOG (SÓ O ADMIN)
        post("/mudar-log") {
            val post_login = call.receive<Login>()

            if (login == null) {
                call.respondText("Você não está devidamente logado, faça o login!")
            }
            if (login!!.tipo_log!! == 1) {
                val login_query = transaction {
                    UsersSchema.update({
                        UsersSchema.email eq post_login.email!!
                    }) {
                        it[UsersSchema.tipo_log] = post_login.tipo_log!!
                    }
                }
                call.respondText("Tipo de log do usuário ${post_login.email} alterado para ${post_login.tipo_log}")
            } else {
                call.respondText("Você não tem permissão de administrador")
            }
        }

        //INFO DO USER
        post("/informacoes-usuario") {
            if (login == null){
                call.respondText("Faça login primeiro")
            } else {
                call.respond(login!!)
            }

        }


         //CADASTRO DE MUSICA
         post("/musica/cadastro") {
             if (login == null) {
                 call.respondText("Faça login primeiro")
             }

                 val retorno = Musica.insert()
                 if (retorno == "SUCESSO"){
                     call.respondText("Musica Cadatrada com sucesso!")
                 } else {
                     call.respondText("Música já cadastrado(a).")
                }

         }

         //ALTERAR MUSICA
         post("/musica/update") {
             if (login == null) {
                 call.respondText("Faça login primeiro")
             }

             val retorno = Musica.update()
             if (retorno == "SUCESSO"){
                 call.respondText("Musica Alterada com sucesso!")
             } else {
                 call.respondText("Música não encontrada.")
             }

         }

         //REMOVER MUSICA
         post("/musica/delete") {
             if (login == null) {
                 call.respondText("Faça login primeiro")
             }

             val retorno = Musica.delete()
             if (retorno == "SUCESSO"){
                 call.respondText("Música excluída com sucesso!")
             } else {
                 call.respondText("Músicaa não encontrada.")
             }

         }

         //CADASTRO DE ARTISTA
         post("/artista/cadastro") {
             if (login == null) {
                 call.respondText("Faça login primeiro")
             }
             val retorno = Artista.insert()
             if (retorno == "SUCESSO"){
                 call.respondText("Artista Cadastrado com sucesso!")
             } else {
                 call.respondText("Artista já cadastrado.")
             }

         }

         //ALTERAR ARTISTA
         post("/artista/update") {
             if (login == null) {
                 call.respondText("Faça login primeiro")
             }

             val retorno = Artista.update()
             if (retorno == "SUCESSO"){
                 call.respondText("Artista Alterado com sucesso!")
             } else {
                 call.respondText("Artista não encontrado.")
             }

         }


         //REMOVER ARTISTA
         post("/artista/delete") {
             if (login == null) {
                 call.respondText("Faça login primeiro")
             }
             val retorno = Artista.delete()
             if (retorno == "SUCESSO"){
                 call.respondText("Artista excluído com sucesso!")
             } else {
                 call.respondText("Artista não encontrado.")
             }

         }

         //CADASTRAR DE ALBUM
         post("/album/cadastro") {
             if (login == null) {
                 call.respondText("Faça login primeiro")
             }
             val retorno = Album.insert()
             if (retorno == "SUCESSO"){
                 call.respondText("Álbum Cadastrado com sucesso!")
             } else {
                 call.respondText("Álbum já cadastrado.")
             }

         }

         //ALTERAR ALBUM
         post("/album/update") {
             if (login == null) {
                 call.respondText("Faça login primeiro")
             }

             val retorno = Album.update()
             if (retorno == "SUCESSO"){
                 call.respondText("Album Alterado com sucesso!")
             } else {
                 call.respondText("Album não encontrado.")
             }

         }

         //REMOVER ALBUM
         post("/album/delete") {
             if (login == null) {
                 call.respondText("Faça login primeiro")
             }
             val retorno = Album.delete()
             if (retorno == "SUCESSO"){
                 call.respondText("Álbum excluído com sucesso!")
             } else {
                 call.respondText("Álbum não encontrado.")
             }

         }


         //MOSTRANDO MUSICAS
         get("/musica/all-musicas"){
             if (login == null) {
                 call.respondText("Faça login primeiramente.")
             }
                   val musica = transaction {
                     MusicaSchema.selectAll().map {
                         MusicaSchema.toObject(it)
                     }
                 }
                 call.respond(musica)
         }


         //MOSTRANDO ARTISTAS
         get("/artista/all-artistas"){
             if (login == null) {
                 call.respondText("Faça login primeiramente.")
             }
             val artista = transaction {
                 ArtistaSchema.selectAll().map {
                     ArtistaSchema.toObject(it)
                 }
             }
             call.respond(artista)
         }


         //PESQUISAR MUSICA POR GENERO
         post("/musica/list-genero") {
             if (login == null) {
                 call.respondText("Faça login primeiramente.")
             }

             val post_musica = call.receive<Musica>()

             val musica_query = transaction {
                 MusicaSchema.select {
                     MusicaSchema.genero eq post_musica.genero!!
                 }.map {
                     MusicaSchema.toObject(it)
                 }
             }

             if (musica_query.size == 0) {
                 transaction {
                     MusicaSchema.select {
                         it[name] = post_musica.name!!
                         it[duracao] = post_musica.duracao!!
                         it[album] = post_musica.album!!
                         it[artista] = post_musica.artista!!
                         it[genero] = post_musica.genero!!
                     }
                 }
                 return call.respondText(musica_query[0])
             } else {
                 return call.respondText("ERRO")
             }
         }

         //PESQUISAR MUSICA POR NOME
         post("/musica/list-nome") {
             if (login == null) {
                 call.respondText("Faça login primeiramente.")
             }
             val post_musica = call.receive<Musica>()

             val musica_query = transaction {
                 MusicaSchema.select {
                     MusicaSchema.name eq post_musica.name!!
                 }.map {
                     MusicaSchema.toObject(it)
                 }
             }

             if (musica_query.size == 0) {
                 transaction {
                     MusicaSchema.select {
                         it[name] = post_musica.name!!
                         it[duracao] = post_musica.duracao!!
                         it[album] = post_musica.album!!
                         it[artista] = post_musica.artista!!
                         it[genero] = post_musica.genero!!
                     }
                 }
                 return call.respondText(musica_query[0])
             } else {
                 return call.respondText("ERRO")
             }
         }

         //PESQUISAR MUSICA POR ARTISTA
         post("/musica/list-artista") {
             if (login == null) {
                 call.respondText("Faça login primeiramente.")
             }
             val post_musica = call.receive<Musica>()

             val musica_query = transaction {
                 MusicaSchema.select {
                     MusicaSchema.artista eq post_musica.artista!!
                 }.map {
                     MusicaSchema.toObject(it)
                 }
             }

             if (musica_query.size == 0) {
                 transaction {
                     MusicaSchema.select {
                         it[name] = post_musica.name!!
                         it[duracao] = post_musica.duracao!!
                         it[album] = post_musica.album!!
                         it[artista] = post_musica.artista!!
                         it[genero] = post_musica.genero!!
                     }
                 }
                 return call.respondText(musica_query[0])
             } else {
                 return call.respondText("ERRO")
             }
         }

         //PESQUISAR ARTISTA
         post("/artista/pesquisa") {
             if (login == null) {
                 call.respondText("Faça login primeiramente.")
             }
             val post_artista = call.receive<Artista>()

             val artista_query = transaction {
                 ArtistaSchema.select {
                     ArtistaSchema.name eq post_artista.name!!
                 }.map {
                     ArtistaSchema.toObject(it)
                 }
             }
             if (artista_query.size == 1){
                 call.respond(artista_query[0])
             } else {
                 call.respondText("Artista ${post_artista.name} NÃO encontrado(a).")

             }
         }

         //PESQUISAR MUSICA
         post("/musica/pesquisa") {
             if (login == null) {
                 call.respondText("Faça login primeiramente.")
             }
             val post_musica = call.receive<Musica>()

             val musica_query = transaction {
                 MusicaSchema.select {
                     MusicaSchema.name eq post_musica.name!! and (MusicaSchema.artista eq post_musica.artista!!)
                 }.map {
                     MusicaSchema.toObject(it)
                 }
             }
             if (musica_query.size == 1){
                 call.respond(musica_query[0])
             } else {
                 call.respondText("Música ${post_musica.name} de ${post_musica.artista} NÃO encontrada.")
             }
         }

         //LISTAR ALBUM DE ARTISTA
         post("/album/list-artista") {
             if (login == null) {
                 call.respondText("Faça login primeiramente.")
             }
             val post_album = call.receive<Album>()

             val album_query = transaction {
                 AlbumSchema.select {
                     AlbumSchema.artista eq post_album.artista!!
                 }.map {
                     AlbumSchema.toObject(it)
                 }
             }

             if (album_query.size == 0) {
                 transaction {
                     AlbumSchema.select {
                         it[name] = post_album.name!!
                         it[artista] = post_album.artista!!
                         it[ano] = post_album.genero!!
                     }
                 }
                 return call.respondText(album_query[0])
             } else {
                 return call.respondText("ERRO")
             }
         }

         //LISTAR MUSICA DE ALBUM
         get("/musica/list-album") {
             if (login == null) {
                 call.respondText("Faça login primeiramente.")
             }
             val post_musica = call.receive<Musica>()

             val musica_query = transaction {
                 MusicaSchema.select {
                     MusicaSchema.album eq post_musica.album!! and (MusicaSchema.artista eq post_musica.artista!!)
                 }.map {
                     MusicaSchema.toObject(it)
                 }
             }

             if (musica_query.size == 0) {
                 transaction {
                     MusicaSchema.select {
                         it[name] = post_musica.name!!
                         it[duracao] = post_musica.duracao!!
                         it[artista] = post_musica.artista!!
                         it[genero] = post_musica.genero!!
                     }
                 }
                 return call.respondText(musica_query[0])
             } else {
                 return call.respondText("ERRO")
             }
         }


    }
}


fun main(){

    // SUBINDO O SERVER NA PORTA 8080
    embeddedServer(
        Netty,
        watchPaths = listOf("E-Music"),
        module=Application::myapp,
        port=8080
    ).start(wait = true)
}