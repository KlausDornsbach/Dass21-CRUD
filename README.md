## DASS-21 Spring Boot CRUD

to run this project you will need docker and docker-compose, the postgreSQL DB runs on it.

If you are using linux and ubuntu with gnome-terminal:

$ sudo apt-get install docker-compose \
$ sudo docker-compose up 

after your db is running, to run the actual spring CRUD you should use: \
$ ./gradlew clean run 

or use your own IDE run environment

### checklist:
- [x] 1 - Utilize o modelo para suas entidades
- [x] 2 - Construa o back-end em Spring
- [x] 3 - Conecte com DB de sua escolha (PostgreSQL)
- [x] 4 - Crie um método /POST, para fazer novas inserções no banco de dados enviando como parâmetro o usuário, bem como suas respostas de acordo com a estrutura existente no banco
- [x] 5 - Crie um método /GET, que retorna uma lista, com todos os usuários cadastrados no banco de dados, bem como suas respectivas pontuações e escalas. Atente-se ao fato de que precisará adicionar a lógica das escalas, da forma que considerar ideal para este cenário.
- [ ] 6 - Crie um método /PUT para alterar dados de usuarios (participantes)
- [ ] 7 - Crie um método /GET retornando um usuario especifico.
- [ ] 8 - Testar

### Author: Klaus de Freitas Dornsbach