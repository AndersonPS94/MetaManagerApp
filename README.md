# GoalFlow: Seu Planejamento de Metas com o Poder da IA

![GoalFlow Logo Placeholder](https://img.shields.io/badge/GoalFlow-AI_Powered_Goal_Planner-4B0082?style=for-the-badge&logo=openai&logoColor=white)
![Status](https://img.shields.io/badge/Status-Em%20Desenvolvimento%20Avançado-blueviolet?style=for-the-badge)
![Tecnologia](https://img.shields.io/badge/Plataforma-Android%20(Kotlin)-green?style=for-the-badge&logo=android)
![Backend](https://img.shields.io/badge/Backend-Firebase%20(Firestore)-orange?style=for-the-badge&logo=firebase)
![Inteligência Artificial](https://img.shields.io/badge/IA-OpenAI%20API-success?style=for-the-badge&logo=openai)

## 🎯 Sobre o Projeto

**GoalFlow** é um aplicativo mobile inovador projetado para transformar grandes objetivos em planos de ação diários e gerenciáveis. Utilizando o poder da Inteligência Artificial da **OpenAI**, o app quebra metas complexas em uma sequência de micro-tarefas, fornecendo ao usuário um caminho claro para o sucesso. O gerenciamento de dados é realizado de forma robusta e remota através do **Firebase Firestore**, garantindo persistência e sincronização.

O projeto está em uma fase avançada de desenvolvimento, com todas as funcionalidades centrais de planejamento e acompanhamento já implementadas.

## 📷 Screenshots
<img width="200" height="3104" alt="Screenshot_20251019_144255" src="https://github.com/user-attachments/assets/002573bc-883c-4aaf-872f-2d8c5bb19b6d" />
<img width="200" height="3104" alt="Screenshot_20251019_144411" src="https://github.com/user-attachments/assets/abb85157-804d-4bb5-bc18-6eae4c5753fb" />
<img width="200" height="3104" alt="Screenshot_20251019_144219" src="https://github.com/user-attachments/assets/bd994515-e90e-4ada-b04e-30487daa8389" />
<img width="200" height="3104" alt="Screenshot_20251019_144359" src="https://github.com/user-attachments/assets/ed6e23ca-ef98-49e4-83d3-d7d0dc9a3efe" />
<img width="200" height="3104" alt="Screenshot_20251019_144148" src="https://github.com/user-attachments/assets/91a32ef4-cf4b-46ac-bc7a-aa3749b4ca9e" />
<img width="200" height="3104" alt="Screenshot_20251019_144337" src="https://github.com/user-attachments/assets/2a24fb4f-555d-4220-b06d-83c0cec0725c" />
<img width="200" height="3104" alt="Screenshot_20251019_144324" src="https://github.com/user-attachments/assets/bc4dff09-824d-473a-ab55-8f6ee96eb19b" />
<img width="200" height="3104" alt="Screenshot_20251019_144702" src="https://github.com/user-attachments/assets/e366017e-5f81-42d9-9584-ef60856182a6" />


## ✨ Funcionalidades Implementadas

As capturas de tela e a análise do projeto confirmam a implementação bem-sucedida das seguintes funcionalidades:

| Funcionalidade | Descrição | Status |
| :--- | :--- | :--- |
| **Cadastro de Metas** | Criação de metas com título e definição de uma **data alvo** (prazo). | ✅ Completo |
| **Geração de Plano com IA** | O usuário insere a meta e o prazo, e a IA da OpenAI gera um **plano diário de micro-tarefas** estruturado ("Dia 1...", "Dia 2...", etc.). | ✅ Completo |
| **Revisão e Aceitação do Plano** | Tela de pré-visualização do plano gerado, permitindo ao usuário **Aceitar** ou **Regerar/Ajustar** o plano antes do salvamento. | ✅ Completo |
| **Persistência Remota (CRUD)** | Salva, lê, atualiza e exclui metas e seus respectivos planos diários no **Firebase Firestore**. | ✅ Completo |
| **Acompanhamento e Progresso** | Tela principal de listagem de metas com **indicadores visuais de progresso** (percentual de conclusão) e prazo. | ✅ Completo |
| **Checklist Diário** | Visualização detalhada do plano com a capacidade de marcar tarefas como concluídas (checklist por dia). | ✅ Completo |
| **Edição de Plano** | Interface para **ajustar o plano** manualmente, permitindo inserir, remover ou reordenar tarefas. | ✅ Completo |
| **Exclusão de Metas** | Funcionalidade para remover metas do sistema com confirmação de segurança. | ✅ Completo |
| **Compartilhamento** | Opção para exportar o plano de leitura como texto para compartilhamento externo. | ✅ Completo |
| **Insights de Progresso** | Tela de **Histórico e Analytics** exibindo o progresso total e métricas como "Dias Seguidos". | ✅ Completo |
| **Assistente de Replanejamento** | Botão "Preciso de Ajuda" que solicita uma descrição do problema (ex: atraso) para que a IA possa sugerir um replanejamento ou dicas. | Desenvolvendo |
| **Offline-first:** |Implementação de cache local para permitir o uso completo do aplicativo sem conexão, sincronizando os dados assim que a conectividade for restaurada. | ✅ Completo |

## 🛠️ Stack Tecnológica

O GoalFlow é construído com uma arquitetura moderna e escalável, utilizando as seguintes tecnologias:

*   **Linguagem:** Kotlin
*   **Plataforma:** Android
*   **Gerenciamento de Estado:** ViewModel e LiveData ou StateFlow (Conforme requisitos, garantindo reatividade e ciclo de vida)
*   **Comunicação de API:** Retrofit (Para requisições RESTful, incluindo a API da OpenAI)
*   **Banco de Dados/Backend:** Firebase Firestore (Para persistência remota e em tempo real)
*   **Inteligência Artificial:** OpenAI API (Para a geração e replanejamento de planos)
*   **Testes:** Testes Unitários (Para garantir a confiabilidade da lógica de negócios)


## 📄 Licença

Este projeto está licenciado sob a Licença MIT. Veja o arquivo `LICENSE` para mais detalhes.

***


