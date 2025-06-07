package app;

import com.estoque.model.Categoria;
import com.estoque.model.Produto;
import com.estoque.dao.CategoriaDAO;
import com.estoque.dao.ProdutoDAO;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;
import java.util.InputMismatchException;

public class Principal {
    private static Scanner scanner = new Scanner(System.in);
    private static CategoriaDAO categoriaDAO = new CategoriaDAO();
    private static ProdutoDAO produtoDAO = new ProdutoDAO();

    public static void main(String[] args) {
        int opcao;
        do {
            exibirMenuPrincipal();
            opcao = lerOpcao();
            switch (opcao) {
                case 1:
                    gerenciarCategorias();
                    break;
                case 2:
                    gerenciarProdutos();
                    break;
                case 0:
                    System.out.println("Saindo do sistema...");
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
            if (opcao != 0) {
                pressioneEnterParaContinuar();
            }
        } while (opcao != 0);
        scanner.close();
    }

    private static void exibirMenuPrincipal() {
        System.out.println("\n--- Sistema de Controle de Estoque ---");
        System.out.println("1. Gerenciar Categorias");
        System.out.println("2. Gerenciar Produtos");
        System.out.println("0. Sair");
        System.out.print("Escolha uma opção: ");
    }

    private static void gerenciarCategorias() {
        int opcao;
        do {
            System.out.println("\n--- Gerenciar Categorias ---");
            System.out.println("1. Adicionar Categoria");
            System.out.println("2. Listar Categorias");
            System.out.println("3. Atualizar Categoria");
            System.out.println("4. Excluir Categoria");
            System.out.println("0. Voltar ao Menu Principal");
            System.out.print("Escolha uma opção: ");
            opcao = lerOpcao();

            switch (opcao) {
                case 1:
                    adicionarCategoria();
                    break;
                case 2:
                    listarCategorias();
                    break;
                case 3:
                    atualizarCategoria();
                    break;
                case 4:
                    excluirCategoria();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opção inválida.");
            }
             if (opcao != 0) {
                pressioneEnterParaContinuar();
            }
        } while (opcao != 0);
    }

    private static void adicionarCategoria() {
        System.out.print("Digite o nome da nova categoria: ");
        String nome = scanner.nextLine();
        if (nome.trim().isEmpty()) {
            System.out.println("Nome da categoria não pode ser vazio.");
            return;
        }
        if (categoriaDAO.buscarPorNome(nome) != null) {
            System.out.println("Erro: Já existe uma categoria com este nome.");
            return;
        }
        Categoria novaCategoria = new Categoria(nome);
        categoriaDAO.adicionar(novaCategoria);
    }

    private static void listarCategorias() {
        System.out.println("\n--- Lista de Categorias ---");
        List<Categoria> categorias = categoriaDAO.listar();
        if (categorias.isEmpty()) {
            System.out.println("Nenhuma categoria cadastrada.");
        } else {
            for (Categoria c : categorias) {
                System.out.println(c);
            }
        }
    }

    private static void atualizarCategoria() {
        listarCategorias();
        System.out.print("Digite o ID da categoria que deseja atualizar: ");
        int id = lerInteiro();
        if (id == -1) return;

        Categoria categoriaExistente = categoriaDAO.buscarPorId(id);
        if (categoriaExistente == null) {
            System.out.println("Categoria não encontrada com o ID: " + id);
            return;
        }

        System.out.print("Digite o novo nome para a categoria '" + categoriaExistente.getNome() + "' (ou enter para manter): ");
        String novoNome = scanner.nextLine();
        
        if (!novoNome.trim().isEmpty()) {
             if (categoriaDAO.buscarPorNome(novoNome) != null && !novoNome.equalsIgnoreCase(categoriaExistente.getNome())) {
                System.out.println("Erro: Já existe outra categoria com o nome '" + novoNome + "'.");
                return;
            }
            categoriaExistente.setNome(novoNome);
        } else {
            System.out.println("Nenhuma alteração feita no nome.");
            return; // Nenhuma alteração se o novo nome for vazio
        }
        
        categoriaDAO.atualizar(categoriaExistente);
    }

    private static void excluirCategoria() {
        listarCategorias();
        System.out.print("Digite o ID da categoria que deseja excluir: ");
        int id = lerInteiro();
         if (id == -1) return;

        Categoria categoriaParaExcluir = categoriaDAO.buscarPorId(id);
         if (categoriaParaExcluir == null) {
            System.out.println("Categoria não encontrada com o ID: " + id);
            return;
        }

        System.out.print("Tem certeza que deseja excluir a categoria '" + categoriaParaExcluir.getNome() + "' (ID: " + id + ")? (s/N): ");
        String confirmacao = scanner.nextLine().trim().toLowerCase();
        if (confirmacao.equals("s")) {
            categoriaDAO.excluir(id);
        } else {
            System.out.println("Exclusão cancelada.");
        }
    }


    private static void gerenciarProdutos() {
        int opcao;
        do {
            System.out.println("\n--- Gerenciar Produtos ---");
            System.out.println("1. Adicionar Produto");
            System.out.println("2. Listar Todos os Produtos");
            System.out.println("3. Listar Produtos por Categoria");
            System.out.println("4. Atualizar Produto");
            System.out.println("5. Excluir Produto");
            System.out.println("0. Voltar ao Menu Principal");
            System.out.print("Escolha uma opção: ");
            opcao = lerOpcao();

            switch (opcao) {
                case 1:
                    adicionarProduto();
                    break;
                case 2:
                    listarTodosProdutos();
                    break;
                case 3:
                    listarProdutosPorCategoria();
                    break;
                case 4:
                    atualizarProduto();
                    break;
                case 5:
                    excluirProduto();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opção inválida.");
            }
             if (opcao != 0) {
                pressioneEnterParaContinuar();
            }
        } while (opcao != 0);
    }

    private static void adicionarProduto() {
        System.out.print("Nome do produto: ");
        String nome = scanner.nextLine();
        System.out.print("Descrição do produto: ");
        String descricao = scanner.nextLine();
        
        BigDecimal preco = BigDecimal.ZERO;
        boolean precoValido = false;
        while(!precoValido) {
            System.out.print("Preço do produto (ex: 10.99): ");
            try {
                preco = scanner.nextBigDecimal();
                scanner.nextLine(); // Consumir nova linha
                if (preco.compareTo(BigDecimal.ZERO) < 0) {
                    System.out.println("Preço não pode ser negativo.");
                } else {
                    precoValido = true;
                }
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida para preço. Use ponto como separador decimal.");
                scanner.nextLine(); // Limpar buffer
            }
        }

        int quantidade = -1;
        while (quantidade < 0) {
            System.out.print("Quantidade em estoque: ");
            quantidade = lerInteiroPositivo();
            if (quantidade < 0 && quantidade != -1) { // -1 é o caso de erro de leitura já tratado por lerInteiroPositivo
                System.out.println("Quantidade não pode ser negativa.");
            } else if (quantidade == -1) { // Erro de input
                return; // Sai da função se a leitura do inteiro falhar
            }
        }


        System.out.println("Selecione a categoria do produto:");
        listarCategorias();
        List<Categoria> categoriasDisponiveis = categoriaDAO.listar();
        if (categoriasDisponiveis.isEmpty()) {
            System.out.println("Nenhuma categoria cadastrada. Cadastre uma categoria primeiro.");
            return;
        }
        System.out.print("Digite o ID da categoria: ");
        int idCategoria = lerInteiro();
        if (idCategoria == -1) return;

        Categoria categoriaSelecionada = categoriaDAO.buscarPorId(idCategoria);
        if (categoriaSelecionada == null) {
            System.out.println("Categoria não encontrada com o ID: " + idCategoria);
            return;
        }

        Produto novoProduto = new Produto(nome, descricao, preco, quantidade, categoriaSelecionada);
        produtoDAO.adicionar(novoProduto);
    }

    private static void listarTodosProdutos() {
        System.out.println("\n--- Lista de Todos os Produtos ---");
        List<Produto> produtos = produtoDAO.listar();
        if (produtos.isEmpty()) {
            System.out.println("Nenhum produto cadastrado.");
        } else {
            for (Produto p : produtos) {
                System.out.println(p);
            }
        }
    }

    private static void listarProdutosPorCategoria() {
        System.out.println("Selecione a categoria para listar os produtos:");
        listarCategorias();
        List<Categoria> categoriasDisponiveis = categoriaDAO.listar();
        if (categoriasDisponiveis.isEmpty()) {
            System.out.println("Nenhuma categoria cadastrada.");
            return;
        }
        System.out.print("Digite o ID da categoria: ");
        int idCategoria = lerInteiro();
        if (idCategoria == -1) return;

        Categoria categoriaSelecionada = categoriaDAO.buscarPorId(idCategoria);
        if (categoriaSelecionada == null) {
            System.out.println("Categoria não encontrada com o ID: " + idCategoria);
            return;
        }

        System.out.println("\n--- Produtos da Categoria: " + categoriaSelecionada.getNome() + " ---");
        List<Produto> produtos = produtoDAO.listarPorCategoriaId(idCategoria);
        if (produtos.isEmpty()) {
            System.out.println("Nenhum produto cadastrado nesta categoria.");
        } else {
            for (Produto p : produtos) {
                System.out.println(p);
            }
        }
    }

    private static void atualizarProduto() {
        listarTodosProdutos();
        System.out.print("Digite o ID do produto que deseja atualizar: ");
        int id = lerInteiro();
        if (id == -1) return;

        Produto produtoExistente = produtoDAO.buscarPorId(id);
        if (produtoExistente == null) {
            System.out.println("Produto não encontrado com o ID: " + id);
            return;
        }

        System.out.print("Novo nome (" + produtoExistente.getNome() + ") ou enter para manter: ");
        String nome = scanner.nextLine();
        if (!nome.trim().isEmpty()) produtoExistente.setNome(nome);

        System.out.print("Nova descrição (" + produtoExistente.getDescricao() + ") ou enter para manter: ");
        String descricao = scanner.nextLine();
        if (!descricao.trim().isEmpty()) produtoExistente.setDescricao(descricao);

        System.out.print("Novo preço (" + produtoExistente.getPreco() + ") ou enter para manter: ");
        String precoStr = scanner.nextLine();
        if (!precoStr.trim().isEmpty()) {
            try {
                BigDecimal novoPreco = new BigDecimal(precoStr);
                 if (novoPreco.compareTo(BigDecimal.ZERO) < 0) {
                    System.out.println("Preço não pode ser negativo. Mantendo o anterior.");
                } else {
                    produtoExistente.setPreco(novoPreco);
                }
            } catch (NumberFormatException e) {
                System.out.println("Formato de preço inválido. Mantendo o anterior.");
            }
        }

        System.out.print("Nova quantidade (" + produtoExistente.getQuantidade() + ") ou enter para manter: ");
        String quantidadeStr = scanner.nextLine();
        if (!quantidadeStr.trim().isEmpty()) {
            try {
                int novaQuantidade = Integer.parseInt(quantidadeStr);
                if (novaQuantidade < 0) {
                    System.out.println("Quantidade não pode ser negativa. Mantendo a anterior.");
                } else {
                     produtoExistente.setQuantidade(novaQuantidade);
                }
            } catch (NumberFormatException e) {
                System.out.println("Formato de quantidade inválido. Mantendo a anterior.");
            }
        }

        System.out.println("Categoria atual: " + produtoExistente.getCategoria().getNome() + " (ID: " + produtoExistente.getCategoria().getId() +")");
        System.out.print("Deseja alterar a categoria? (s/N): ");
        if (scanner.nextLine().trim().equalsIgnoreCase("s")) {
            listarCategorias();
            System.out.print("Digite o ID da nova categoria: ");
            int idNovaCategoria = lerInteiro();
            if (idNovaCategoria != -1) {
                Categoria novaCategoria = categoriaDAO.buscarPorId(idNovaCategoria);
                if (novaCategoria != null) {
                    produtoExistente.setCategoria(novaCategoria);
                } else {
                    System.out.println("Nova categoria não encontrada. Mantendo a anterior.");
                }
            }
        }
        
        produtoDAO.atualizar(produtoExistente);
    }

    private static void excluirProduto() {
        listarTodosProdutos();
        System.out.print("Digite o ID do produto que deseja excluir: ");
        int id = lerInteiro();
        if (id == -1) return;

        Produto produtoParaExcluir = produtoDAO.buscarPorId(id);
         if (produtoParaExcluir == null) {
            System.out.println("Produto não encontrado com o ID: " + id);
            return;
        }

        System.out.print("Tem certeza que deseja excluir o produto '" + produtoParaExcluir.getNome() + "' (ID: " + id + ")? (s/N): ");
        String confirmacao = scanner.nextLine().trim().toLowerCase();
        if (confirmacao.equals("s")) {
            produtoDAO.excluir(id);
        } else {
            System.out.println("Exclusão cancelada.");
        }
    }

    private static int lerOpcao() {
        try {
            int opcao = scanner.nextInt();
            scanner.nextLine(); // Consumir a nova linha
            return opcao;
        } catch (InputMismatchException e) {
            System.out.println("Entrada inválida. Por favor, digite um número.");
            scanner.nextLine(); // Limpar o buffer do scanner
            return -1; // Retorna um valor inválido para repetir o loop
        }
    }

    private static int lerInteiro() {
        try {
            int valor = scanner.nextInt();
            scanner.nextLine(); 
            return valor;
        } catch (InputMismatchException e) {
            System.out.println("Entrada inválida. Por favor, digite um número inteiro.");
            scanner.nextLine(); 
            return -1; 
        }
    }
    
    private static int lerInteiroPositivo() {
        try {
            int valor = scanner.nextInt();
            scanner.nextLine(); 
            if (valor < 0) {
                System.out.println("O valor deve ser positivo.");
                return -2; // Código para indicar que foi negativo mas válido numericamente
            }
            return valor;
        } catch (InputMismatchException e) {
            System.out.println("Entrada inválida. Por favor, digite um número inteiro.");
            scanner.nextLine(); 
            return -1; // Código para indicar erro de tipo de input
        }
    }

    private static void pressioneEnterParaContinuar() {
        System.out.print("\nPressione Enter para continuar...");
        scanner.nextLine();
    }
}