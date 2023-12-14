## DASS-21 Spring Boot CRUD

to run this project you will need docker and docker-compose, the postgreSQL DB runs on it.

If you are using linux and ubuntu:

$ sudo apt-get install docker-compose \
$ sudo docker-compose up 

after your db is running, to run the actual spring CRUD you should use: \
$ ./gradlew clean build
$ ./gradlew run

or use your own IDE run environment to run the main CrudApplication file

I have taken the liberty to add my postman collection, so you can import it and start accessing the api.\

### Implementation particularities
There are 2 main entities that are tracked by the DB, the Participante and the Resposta entities. Every Participante can have 0 or N Respostas and when queried, it returns the last Resposta registered using the date for sorting.\
The choice to keep N Repostas to 1 Participante was mainly because the system is more flexible and also we can track the User evolution if he were to take the test again periodically.
## API DOCUMENTATION - MAIN ENDPOINTS:
### [POST] /adicionar
adds a new Participant and Response directly
#### example payload:
> {\
"nome": "klaus",\
"idade": 23,\
"genero": "masculino",\
"pontuacaoTotalDepressao": 2,\
"pontuacaoTotalAnsiedade": 2,\
"pontuacaoTotalEstresse" : 2\
}

### [GET] /todos
returns all Participants and their respective Responses 
#### example return:
>[\
{\
"participanteId": 1,\
"idade": 23,\
"genero": "masculino",\
"dataResposta": "2023-12-14T01:28:36.275792",\
"pontuacaoTotalAnsiedade": 16,\
"escalaAnsiedade": "4 - Sintomas muito graves",\
"pontuacaoTotalDepressao": 10,\
"escalaDepressao": "2 - Sintomas moderados",\
"pontuacaoTotalEstresse": 11,\
"escalaEstresse": "2 - Sintomas moderados"\
},\
{\
"participanteId": 2,\
"idade": 49,\
"genero": "feminino",\
"dataResposta": null,\
"pontuacaoTotalAnsiedade": null,\
"escalaAnsiedade": null,\
"pontuacaoTotalDepressao": null,\
"escalaDepressao": null,\
"pontuacaoTotalEstresse": null,\
"escalaEstresse": null\
}\
]

### [PUT] /atualizar/{id}
updates a Participant and/or their Response, if you include at least 1 parameter it will be updated.
#### example payloads:
> {\
"nome": "klaus",\
"pontuacaoTotalEstresse" : 12\
}

> {
"nome": "gertrude",\
// "idade": 10,\
// "genero": "masculino",\
"pontuacaoTotalDepressao": 4,\
"pontuacaoTotalAnsiedade": 10,\
"pontuacaoTotalEstresse" : 5\
}

### [GET] /participante/{id}
gets a specific Participant and their Response, it gets the last response sorted by date, or returns null if the user has no Response
#### example return:
> {\
"participanteId": 1,\
"idade": 23,\
"genero": "masculino",\
"dataResposta": "2023-12-14T01:28:36.275792",\
"pontuacaoTotalAnsiedade": 16,\
"escalaAnsiedade": "4 - Sintomas muito graves",\
"pontuacaoTotalDepressao": 10,\
"escalaDepressao": "2 - Sintomas moderados",\
"pontuacaoTotalEstresse": 11,\
"escalaEstresse": "2 - Sintomas moderados"\
}

### checklist:
- [x] 1 - Utilize o modelo para suas entidades
- [x] 2 - Construa o back-end em Spring
- [x] 3 - Conecte com DB de sua escolha (PostgreSQL)
- [x] 4 - Crie um método /POST, para fazer novas inserções no banco de dados enviando como parâmetro o usuário, bem como suas respostas de acordo com a estrutura existente no banco
- [x] 5 - Crie um método /GET, que retorna uma lista, com todos os usuários cadastrados no banco de dados, bem como suas respectivas pontuações e escalas. Atente-se ao fato de que precisará adicionar a lógica das escalas, da forma que considerar ideal para este cenário.
- [x] 6 - Crie um método /PUT para alterar dados de usuarios (participantes)
- [x] 7 - Crie um método /GET retornando um usuario especifico.
- [x] 8 - Testes spring
- [x] 9 - Documentation

### other endpoints
### [POST] /participante/adicionar
adds a Participant with no Response associated
#### example payload:
> {\
"nome": "claudia",\
"idade": 49,\
"genero": "feminino"\
}
### [POST] /resposta/adicionar
adds a Response associated with a Participant, identified by id
#### example payload:
> {\
"id": 1,\
"pontuacaoTotalDepressao": 10,\
"pontuacaoTotalAnsiedade": 16,\
"pontuacaoTotalEstresse" : 11\
}
### [PUT] /participante/atualizar
updates Participant, can have 1 to 3 of the Participant properties
#### example payload:
> {\
"nome": "claudia",\
"idade": 49,\
// "gender": fem\
}
### [PUT] /resposta/atualizar
updates Response, can include from 1 to 4 of the Response properties
#### example payload:
> { \
"id": 2,\
"pontuacaoTotalDepressao": 0,\
"pontuacaoTotalAnsiedade": 0,\
"pontuacaoTotalEstresse" : 1\
}

### Author: Klaus de Freitas Dornsbach