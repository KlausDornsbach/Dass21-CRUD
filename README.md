## DASS-21 Spring Boot CRUD

to run this project you will need docker and docker-compose, the postgreSQL DB runs on it.

If you are using linux and ubuntu with gnome-terminal:

$ sudo apt install gnome-terminal \
$ sudo apt-get install docker-compose

after your db is running, to run the actual spring CRUD you should use: \
$ ./gradlew clean run 

or use your own IDE run environment

### checklist:
- [x] 1
- [x] 2
- [x] 3
- [x] 4 - Crie um método /POST, para fazer novas inserções no banco de dados enviando como parâmetro o usuário, bem como suas respostas de acordo com a estrutura existente no banco
- [x] 5 - Crie um método /GET, que retorna uma lista, com todos os usuários cadastrados no banco de dados, bem como suas respectivas pontuações e escalas. Atente-se ao fato de que precisará adicionar a lógica das escalas, da forma que considerar ideal para este cenário.
- [ ] 6
- [ ] 7
- [ ] testing

### Author: Klaus de Freitas Dornsbach