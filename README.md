### 📘 Projeto 3 – Autenticação JWT para APIs REST

```markdown
# Projeto 3 – API REST Segura com JWT (JSON Web Token)

Este projeto transforma a aplicação em uma **API REST segura**, utilizando autenticação baseada em **JWT (stateless)** em vez de sessões.

## 🎯 Objetivo

- Criar um endpoint de login (`/auth/login`)
- Gerar e assinar um token JWT
- Validar o token JWT em cada requisição
- Proteger endpoints REST com Spring Security e filtros

## 🧪 Fluxo de autenticação

1. Cliente envia `POST localhost:8080/auth/login` com JSON:
   ```json
   {
     "username": "joao",
     "password": "12345"
   }
2. Resposta:
{
  "username": "joao",
  "role": "USER",
  "token": "<jwt-token>"
}
3. Copie o Token de reposta e faça uma requisição `GET localhost:8080/api/produtos`:
Lembre de configurar a autenticação para via token, no menu Authorization, do Postman, como mostrada no documento do projeto, colando o token gerado no campo Token.
4. Tudo dando certo a requisição retornará uma lista vazia: [] Afinal, não tem nenhum produto cadastrado.

Gostou? Adquira o Curso de Criação de API Java com Spring Boot (Spring MVC, Data JPA, Security, H2 e MySQL) aqui: [https://pay.kiwify.com.br/XVcZbZy](https://pay.kiwify.com.br/XVcZbZy) Ou, adquira apenas os projetos Documentados com Spring Security aqui: [https://pay.kiwify.com.br/1BOIOP9](https://pay.kiwify.com.br/1BOIOP9)
