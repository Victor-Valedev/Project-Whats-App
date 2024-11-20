# Clone de WhatsApp - Aplicativo de Mensagens

Este projeto é um **clone do WhatsApp** desenvolvido para Android, com foco na criação de uma aplicação funcional de mensagens instantâneas. O app utiliza **Kotlin** como linguagem de programação e é integrado ao **Firebase** para gerenciamento de autenticação, banco de dados e armazenamento de mídias. O objetivo do projeto é proporcionar uma experiência semelhante à do WhatsApp, incluindo troca de mensagens em tempo real, personalização do perfil do usuário e suporte ao modo claro/escuro.

## Funcionalidades:
- **Cadastro e Login de Usuários**:  
  O app permite que os usuários se registrem e façam login através do **Firebase Authentication**, garantindo segurança no processo de autenticação.
  
- **Tela de Conversas**:  
  Exibição das conversas de usuários com contatos, mostrando a última mensagem e horário de envio. As mensagens são armazenadas em tempo real no **Firestore**, permitindo atualizações dinâmicas na interface.

- **Perfil do Usuário**:  
  Os usuários podem editar seu perfil, incluindo foto e nome, com integração ao **Firebase Storage** para upload e armazenamento de imagens de perfil.

- **Alternância de Tema (Claro/Escuro)**:  
  O aplicativo oferece a opção de mudar entre o tema claro e escuro, armazenando a preferência do usuário usando **SharedPreferences**.

- **Armazenamento de Mídias**:  
  O Firebase Storage é utilizado para armazenar imagens de perfil e fotos enviadas nas conversas.

## Tecnologias Utilizadas:
- **Kotlin**: Linguagem de programação principal para o desenvolvimento Android.
- **Firebase**:  
  - **Firebase Authentication**: Gerenciamento de login e cadastro de usuários.  
  - **Firestore**: Banco de dados em tempo real para o armazenamento de mensagens e dados dos usuários.  
  - **Firebase Storage**: Armazenamento de fotos de perfil e imagens em mensagens.
- **SharedPreferences**: Armazenamento local das preferências do usuário, como a seleção do tema.
- **Glide/Picasso (opcional)**: Biblioteca para carregamento eficiente de imagens no aplicativo.
- **Material Design**: Para criar uma interface de usuário moderna e responsiva, utilizando os componentes do Material Design.

## Como Executar o Projeto:
1. Clone o repositório:
   ```bash
   git clone https://github.com/Victor-Valedev/Project-Whats-App.git
